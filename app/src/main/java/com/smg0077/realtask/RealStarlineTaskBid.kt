package com.smg0077.realtask

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.InputFilter
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.session.MySession
import com.smg0077.all_adapter.RealStarlineBidPointsAdapter
import com.smg0077.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RealStarlineTaskBid : AppCompatActivity() {
    lateinit var backBUT: ImageView
    lateinit var adaptercalling: RealStarlineBidPointsAdapter
    lateinit var walletpoint: TextView
    lateinit var proceedbut: Button
    lateinit var submitButton: Button
    lateinit var windowtitle: TextView
    lateinit var currentdateTextView: TextView
    lateinit var opendigitTextView: TextView
    lateinit var openDigitEditText: AutoCompleteTextView
    lateinit var pointsEditText: EditText
    lateinit var getgameid: String
    lateinit var getgamename: String
    lateinit var pana_type: String
    lateinit var recyclerView: RecyclerView
    lateinit var getpana_type: String
    lateinit var progressBar: View
    lateinit var app_key: AppKeyHolderClass
    lateinit var session: MySession
    var main_min_bid_amount = 0
    var main_max_bid_amount = 0
    var total_bid_amount = 0
    var temppointlist: MutableList<String> = ArrayList()
    var tempopendigitList: MutableList<String> = ArrayList()

    lateinit var CheckMyGameStatus: String
    lateinit var dataStringArray: Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_real_starline_game_bid)

        initvalues()
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1, dataStringArray
        )
        openDigitEditText.setAdapter(adapter)

        backBUT.setOnClickListener {
            onBackPressed()
        }

        proceedbut.setOnClickListener(View.OnClickListener {
            val getopendigit = openDigitEditText.text.toString().trim()
            val getpoints = pointsEditText.text.toString().trim()

            if (!dataStringArray.contains(getopendigit)) {
                val snak = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Digit not available!",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snak.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                view.layoutParams = params
                snak.show()
                return@OnClickListener
            }

            if (getopendigit.isEmpty()) {
                openDigitEditText.error = "Enter Open Digit"
                return@OnClickListener
            }
            if (getpoints.isEmpty()) {
                pointsEditText.error = "Enter Points"
                return@OnClickListener
            }
            val castpoint: Int
            castpoint = getpoints.toInt()
            if (castpoint < main_min_bid_amount) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Should be more than minimum $main_min_bid_amount",
                    Snackbar.LENGTH_LONG
                ).show()
                val vibrateservice = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrateservice.vibrate(100)
                return@OnClickListener
            } else {
                var castwallet: Int
                val temppoint = walletpoint.text.toString().replace("\u20B9 ", "")
                castwallet = temppoint.toInt()
                if (castpoint > castwallet) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Should be less than wallet point $temppoint",
                        Snackbar.LENGTH_LONG
                    ).show()
                    val vibrateservice = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibrateservice.vibrate(100)
                    return@OnClickListener
                } else {
                    if (castpoint > main_max_bid_amount) {      //check less than maximum bid amount
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Should be less than max bid amount $main_max_bid_amount",
                            Snackbar.LENGTH_LONG
                        ).show()
                        val vibrateservice =
                            this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        vibrateservice.vibrate(100)
                        return@OnClickListener
                    } else {
                        castwallet = castwallet - castpoint
                        walletpoint.text = "\u20B9 " + castwallet.toString()
                        if (tempopendigitList.contains(getopendigit)) {
                            val index = tempopendigitList.indexOf(getopendigit)
                            val pointsdum = temppointlist.elementAt(index)
                            val castpoint: Int = pointsdum.toInt()
                            castwallet = castwallet + castpoint
                            walletpoint.text = "\u20B9 " + castwallet.toString()
                            tempopendigitList.removeAt(index)
                            temppointlist.removeAt(index)
                            tempopendigitList.add(index, getopendigit)
                            temppointlist.add(index, getpoints)
                        } else {
                            tempopendigitList.add(getopendigit)
                            temppointlist.add(getpoints)
                        }
                        adaptercalling = RealStarlineBidPointsAdapter(
                            applicationContext,
                            tempopendigitList,
                            temppointlist,
                            submitButton,
                            walletpoint
                        )
                        recyclerView.adapter = adaptercalling
                        openDigitEditText.text.clear()
                        pointsEditText.text.clear()
                        openDigitEditText.requestFocus()
                    }
                }
            }
        })

        submitButton.setOnClickListener {
            total_bid_amount = 0
            retrofitCheckGameStatus()
            var i = 0
            var j = 0
            while (j < tempopendigitList.size) {
                var castpointstoint = 0
                castpointstoint = temppointlist[j].toInt()
                total_bid_amount += castpointstoint
                j += 1
            }
            val resultArrayData = JsonArray()

            while (i < tempopendigitList.size) {

                val digit = tempopendigitList.elementAt(i)
                val closedigit = ""
                val pointes = temppointlist.elementAt(i)
                val sessiontype = "Open"

                val jsondata = JsonObject()
                jsondata.addProperty("digits", digit)
                jsondata.addProperty("closedigits", closedigit)
                jsondata.addProperty("points", pointes)
                jsondata.addProperty("session", sessiontype)
                resultArrayData.add(jsondata)
                i += 1
            }


            val resultjsonobject = JsonObject()
            val newresult = JsonObject()

            val userid = session.getSession_userid()
            val gamename = getgamename
            val totalbid = total_bid_amount.toString()
            val currentdate = currentdateTextView.text.toString()

            newresult.addProperty("unique_token", userid)
            newresult.addProperty("Gamename", gamename)
            newresult.addProperty("totalbit", totalbid)
            newresult.addProperty("gameid", getgameid)
            newresult.addProperty("pana", pana_type)
            newresult.addProperty("bid_date", currentdate)
            newresult.addProperty("session", "Open")
            newresult.add("result", resultArrayData)
            resultjsonobject.addProperty("app_key", AppKeyHolderClass().getAppKey())
            resultjsonobject.addProperty("env_type", env_type)
            resultjsonobject.add("new_result", newresult)

            if (CheckMyGameStatus.equals("1")) {
                ShowMessageDialog(resultjsonobject).show()
            } else {
                val snak = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Sorry Betting Is Closed",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snak.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                view.layoutParams = params
                snak.show()
                vibrate()
            }
        }
    }

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initvalues() {
        session = MySession(applicationContext)
        walletpoint = findViewById(R.id.starlinebid_window_walletpoint)
        currentdateTextView = findViewById(R.id.starlinebid_window_currentDate)
        openDigitEditText = findViewById(R.id.starlinebid_window_opendigit)
        opendigitTextView = findViewById(R.id.bid_window_opendigitTV)
        pointsEditText = findViewById(R.id.starlinebid_window_points)
        proceedbut = findViewById(R.id.starlinebiduser_proceed_button)
        submitButton = findViewById(R.id.starlinebiduser_submit_button)
        windowtitle = findViewById(R.id.starlinebid_window_title)
        recyclerView = findViewById(R.id.starlinebid_window_recycler)
        backBUT = findViewById(R.id.userbackbut)
        progressBar = findViewById(R.id.progressbar2)
        getgameid = intent.getStringExtra("game_id").toString()
        getgamename = intent.getStringExtra("game_name").toString()
        getpana_type = intent.getStringExtra("pana").toString()
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm


        if (getpana_type.equals("single_digit")) {
            val SingleDigitList = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
            pana_type = "Single Digit"
            windowtitle.text = pana_type
            openDigitEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))
            dataStringArray = SingleDigitList.copyOf()
        }
        if (getpana_type.equals("single_pana")) {
            pana_type = "Single Pana"
            opendigitTextView.text = "Open Panna"
            windowtitle.text = "Single Panna"
            openDigitEditText.hint = "Enter Panna"
            openDigitEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))
            val paymentname: ArrayList<String> = ArrayList()
            paymentname.add("120")
            paymentname.add("123")
            paymentname.add("124")
            paymentname.add("125")
            paymentname.add("126")
            paymentname.add("127")
            paymentname.add("128")
            paymentname.add("129")
            paymentname.add("130")
            paymentname.add("134")
            paymentname.add("135")
            paymentname.add("136")
            paymentname.add("137")
            paymentname.add("138")
            paymentname.add("139")
            paymentname.add("140")
            paymentname.add("145")
            paymentname.add("146")
            paymentname.add("147")
            paymentname.add("148")
            paymentname.add("149")
            paymentname.add("150")
            paymentname.add("156")
            paymentname.add("157")
            paymentname.add("158")
            paymentname.add("159")
            paymentname.add("160")
            paymentname.add("167")
            paymentname.add("168")
            paymentname.add("169")
            paymentname.add("170")
            paymentname.add("178")
            paymentname.add("179")
            paymentname.add("180")
            paymentname.add("189")
            paymentname.add("190")
            paymentname.add("230")
            paymentname.add("234")
            paymentname.add("235")

            paymentname.add("236")
            paymentname.add("237")
            paymentname.add("238")
            paymentname.add("239")
            paymentname.add("240")
            paymentname.add("245")
            paymentname.add("246")
            paymentname.add("247")
            paymentname.add("248")
            paymentname.add("249")
            paymentname.add("250")
            paymentname.add("256")
            paymentname.add("257")
            paymentname.add("258")
            paymentname.add("259")
            paymentname.add("260")
            paymentname.add("267")
            paymentname.add("268")
            paymentname.add("269")
            paymentname.add("270")
            paymentname.add("278")
            paymentname.add("279")
            paymentname.add("280")
            paymentname.add("289")
            paymentname.add("290")
            paymentname.add("340")
            paymentname.add("345")
            paymentname.add("346")
            paymentname.add("347")
            paymentname.add("348")
            paymentname.add("349")
            paymentname.add("350")
            paymentname.add("356")
            paymentname.add("357")
            paymentname.add("358")
            paymentname.add("359")
            paymentname.add("360")
            paymentname.add("367")
            paymentname.add("368")
            paymentname.add("369")
            paymentname.add("370")
            paymentname.add("378")
            paymentname.add("379")
            paymentname.add("380")
            paymentname.add("389")
            paymentname.add("390")
            paymentname.add("450")
            paymentname.add("456")
            paymentname.add("457")
            paymentname.add("458")
            paymentname.add("459")
            paymentname.add("460")
            paymentname.add("467")
            paymentname.add("468")
            paymentname.add("469")
            paymentname.add("470")
            paymentname.add("478")
            paymentname.add("479")
            paymentname.add("480")
            paymentname.add("489")
            paymentname.add("490")

            paymentname.add("560")
            paymentname.add("567")
            paymentname.add("568")
            paymentname.add("569")
            paymentname.add("570")
            paymentname.add("578")
            paymentname.add("579")
            paymentname.add("580")
            paymentname.add("589")
            paymentname.add("590")
            paymentname.add("670")
            paymentname.add("678")
            paymentname.add("679")
            paymentname.add("680")
            paymentname.add("689")
            paymentname.add("690")
            paymentname.add("780")
            paymentname.add("789")
            paymentname.add("790")
            paymentname.add("890")
            dataStringArray = paymentname.toTypedArray()

        }
        if (getpana_type.equals("double_pana")) {
            pana_type = "Double Pana"
            opendigitTextView.text = "Open Panna"
            openDigitEditText.hint = "Enter Panna"
            windowtitle.text = "Double Panna"
            openDigitEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))
            val paymentname: ArrayList<String> = ArrayList()
            paymentname.add("100")
            paymentname.add("110")

            paymentname.add("112")
            paymentname.add("113")
            paymentname.add("114")
            paymentname.add("115")
            paymentname.add("116")
            paymentname.add("117")
            paymentname.add("118")
            paymentname.add("119")
            paymentname.add("122")

            paymentname.add("133")
            paymentname.add("144")
            paymentname.add("155")
            paymentname.add("166")
            paymentname.add("177")
            paymentname.add("188")
            paymentname.add("199")
            paymentname.add("200")
            paymentname.add("220")

            paymentname.add("223")
            paymentname.add("224")
            paymentname.add("225")
            paymentname.add("226")
            paymentname.add("227")
            paymentname.add("228")
            paymentname.add("229")
            paymentname.add("233")
            paymentname.add("244")
            paymentname.add("255")

            paymentname.add("266")
            paymentname.add("277")
            paymentname.add("288")
            paymentname.add("299")
            paymentname.add("300")
            paymentname.add("330")
            paymentname.add("334")
            paymentname.add("335")
            paymentname.add("336")
            paymentname.add("337")

            paymentname.add("338")
            paymentname.add("339")
            paymentname.add("344")
            paymentname.add("355")
            paymentname.add("366")
            paymentname.add("377")
            paymentname.add("388")
            paymentname.add("399")
            paymentname.add("400")
            paymentname.add("440")

            paymentname.add("445")
            paymentname.add("446")
            paymentname.add("447")
            paymentname.add("448")
            paymentname.add("449")
            paymentname.add("449")
            paymentname.add("455")
            paymentname.add("466")
            paymentname.add("477")
            paymentname.add("488")

            paymentname.add("499")
            paymentname.add("500")
            paymentname.add("550")
            paymentname.add("556")
            paymentname.add("557")
            paymentname.add("558")
            paymentname.add("559")
            paymentname.add("566")
            paymentname.add("577")
            paymentname.add("588")

            paymentname.add("599")
            paymentname.add("600")
            paymentname.add("660")
            paymentname.add("667")
            paymentname.add("668")
            paymentname.add("669")
            paymentname.add("677")
            paymentname.add("688")
            paymentname.add("699")
            paymentname.add("700")

            paymentname.add("770")
            paymentname.add("778")
            paymentname.add("779")
            paymentname.add("788")
            paymentname.add("799")
            paymentname.add("800")
            paymentname.add("880")
            paymentname.add("889")
            paymentname.add("899")
            paymentname.add("900")
            paymentname.add("990")

            dataStringArray = paymentname.toTypedArray()
        }
        if (getpana_type.equals("triple_pana")) {
            pana_type = "Triple Pana"
            opendigitTextView.text = "Open Panna"
            windowtitle.text = "Triple Panna"
            openDigitEditText.hint = "Enter Panna"
            openDigitEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))
            val paymentname: ArrayList<String> = ArrayList()

            paymentname.add("000")
            paymentname.add("111")
            paymentname.add("222")
            paymentname.add("333")
            paymentname.add("444")
            paymentname.add("555")
            paymentname.add("666")
            paymentname.add("777")
            paymentname.add("888")
            paymentname.add("999")
            dataStringArray = paymentname.toTypedArray()

        }

        retrofitBid_onload_Data()
        retrofitCheckGameStatus()
    }

    private fun retrofitBid_onload_Data() {

        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("game_id", getgameid)

        RetrofitClient.service.Bid_window_currentdate(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val responsevalues = response
                        val status = responsevalues.body()?.get("status").toString().replace(
                            "\"",
                            ""
                        )

                        if (status.equals("true")) {
                            val walletpointget =
                                responsevalues.body()?.get("wallet_amt").toString().replace(
                                    "\"",
                                    ""
                                )
                            val min_bid_amount =
                                responsevalues.body()?.get("min_bid_amount").toString().replace(
                                    "\"",
                                    ""
                                )
                            val max_bid_amount =
                                responsevalues.body()?.get("max_bid_amount").toString().replace(
                                    "\"",
                                    ""
                                )
                            val currentdate =
                                responsevalues.body()?.get("new_date").toString().replace(
                                    "\"",
                                    ""
                                )
                            walletpoint.text = "\u20B9 " + walletpointget
                            currentdateTextView.text = currentdate
                            val castingtemp: Int
                            castingtemp = min_bid_amount.toInt()
                            main_min_bid_amount = castingtemp
                            val castingtempmax: Int
                            castingtempmax = max_bid_amount.toInt()
                            main_max_bid_amount = castingtempmax

                            myHideShowProgress(false)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "NO Data!",
                                Toast.LENGTH_LONG
                            ).show()
                            myHideShowProgress(false)
                        }
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Unable to Connect to Internet",
                        Toast.LENGTH_LONG
                    ).show()
                    myHideShowProgress(false)

                }
            })
    }


    private fun retrofitCheckGameStatus() {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("game_id", getgameid)

        RetrofitClient.service.realstarlinegamestatusApi(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.get("msg").toString().replace("\"", "")
                        val game_status =
                            response.body()?.get("game_status").toString().replace("\"", "")
                        myHideShowProgress(false)
                        CheckMyGameStatus = game_status
                    }
                    myHideShowProgress(false)

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Unable to Connect to Internet",
                        Toast.LENGTH_LONG
                    ).show()
                    myHideShowProgress(false)

                }
            })
    }

    private fun retrofitcalling(sendingdata: JsonObject) {
        myHideShowProgress(true)
        RetrofitClient.service.realstarlinegamebidSubmitApi(sendingdata).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val responsevalues = response
                        val msg = responsevalues.body()?.get("msg").toString().replace("\"", "")
                        val status =
                            responsevalues.body()?.get("status").toString().replace("\"", "")

                        if (status.equals("true")) {
                            ShowSuccessMessageDialog(msg).show()
                        } else {
                            ShowSuccessMessageFailDialog(msg).show()
                        }
                        myHideShowProgress(false)
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Unable to Connect to Internet",
                        Toast.LENGTH_LONG
                    ).show()
                    myHideShowProgress(false)

                }
            })
    }

    private fun myHideShowProgress(check: Boolean) {
        if (check) {
            progressBar.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            progressBar.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }


    fun ShowMessageDialog(sendingdata: JsonObject): AlertDialog {
        val alert = AlertDialog.Builder(this)
        val myview = layoutInflater.inflate(R.layout.custom_dialog_message_bid_window, null)
        alert.setView(myview)
        val cancelbut = myview.findViewById<Button>(R.id.btn_cancel)
        val ConfirmBut = myview.findViewById<Button>(R.id.btn_okay)

        val alertDialog = alert.create()

        alertDialog.setCanceledOnTouchOutside(false)
        cancelbut.setOnClickListener {
            alertDialog.dismiss()
        }
        ConfirmBut.setOnClickListener {
            retrofitcalling(sendingdata)
            total_bid_amount = 0
            tempopendigitList.clear()
            temppointlist.clear()
            adaptercalling.clear()
            submitButton.visibility = View.GONE
            openDigitEditText.text.clear()
            pointsEditText.text.clear()
            alertDialog.dismiss()
        }
        return alertDialog
    }

    fun ShowSuccessMessageDialog(msg: String): AlertDialog {
        val alert = AlertDialog.Builder(this)
        val myview = layoutInflater.inflate(R.layout.custom_dialog_message_bid__sucess, null)
        alert.setView(myview)
        val ConfirmBut = myview.findViewById<Button>(R.id.btn_okay)
        val msgprint = myview.findViewById<TextView>(R.id.sucessmessage)
        msgprint.text = msg
        val alertDialog = alert.create()
        alertDialog.setCanceledOnTouchOutside(false)
        ConfirmBut.setOnClickListener {
            onResume()
            alertDialog.dismiss()
        }
        return alertDialog

    }

    fun ShowSuccessMessageFailDialog(msg: String): AlertDialog {
        val alert = AlertDialog.Builder(this)
        val myview = layoutInflater.inflate(R.layout.custom_dialog_message_bid_fail, null)
        alert.setView(myview)
        val ConfirmBut = myview.findViewById<Button>(R.id.btn_okay)
        val msgprint = myview.findViewById<TextView>(R.id.sucessmessage)
        msgprint.text = msg
        val alertDialog = alert.create()
        alertDialog.setCanceledOnTouchOutside(false)
        ConfirmBut.setOnClickListener {
            onResume()
            alertDialog.dismiss()
        }
        return alertDialog
    }
}