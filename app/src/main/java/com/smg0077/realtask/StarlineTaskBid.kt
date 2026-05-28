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
import com.smg0077.all_adapter.StarlineBidPointsAdapter
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.session.MySession
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.smg0077.R
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient.env_type
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StarlineTaskBid : AppCompatActivity() {
    lateinit var backBUT: ImageView
    lateinit var adaptercalling: StarlineBidPointsAdapter
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
        setContentView(R.layout.activity_starline_game_bid)
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
                val view: View = snak.getView()
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
                        castwallet -= castpoint
                        walletpoint.text = "\u20B9 " + castwallet.toString()
                        if (tempopendigitList.contains(getopendigit)) {
                            val index = tempopendigitList.indexOf(getopendigit)
                            val pointsdum = temppointlist.elementAt(index)
                            val castpoint: Int = pointsdum.toInt()
                            castwallet += castpoint
                            walletpoint.text = "\u20B9 " + castwallet.toString()
                            tempopendigitList.removeAt(index)
                            temppointlist.removeAt(index)
                            tempopendigitList.add(index, getopendigit)
                            temppointlist.add(index, getpoints)

                        } else {
                            tempopendigitList.add(getopendigit)
                            temppointlist.add(getpoints)
                        }
                        adaptercalling = StarlineBidPointsAdapter(
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
                val view: View = snak.getView()
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
        proceedbut = findViewById(R.id.starlinebiduser_proceed_Button)
        submitButton = findViewById(R.id.starlinebiduser_submit_Button)
        windowtitle = findViewById(R.id.starlinebid_window_title)
        recyclerView = findViewById(R.id.starlinebid_window_recycler)
        backBUT = findViewById(R.id.userbackbut)
        progressBar = findViewById(R.id.progressbar2)
        getgameid = intent.getStringExtra("game_id").toString()
        getgamename = intent.getStringExtra("game_name").toString()
        getpana_type = intent.getStringExtra("pana").toString()
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.setLayoutManager(llm)

        if (getpana_type.equals("single_digit")) {
            val SingleDigitList = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
            pana_type = "Left Digit"
            windowtitle.text = pana_type
            openDigitEditText.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(1)))
            dataStringArray = SingleDigitList.copyOf()
        }
        if (getpana_type.equals("single_pana")) {
            pana_type = "Right Digit"
            opendigitTextView.text = "Open Digit"
            windowtitle.text = "Right Digit"
            openDigitEditText.hint = "Enter Digit"
            openDigitEditText.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(1)))
            val SingleDigitList = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
            dataStringArray = SingleDigitList.copyOf()
        }
        if (getpana_type.equals("double_pana")) {
            pana_type = "Jodi Digit"
            opendigitTextView.text = "Open Digit"
            openDigitEditText.hint = "Enter Digit"
            windowtitle.text = "Jodi Digit"
            openDigitEditText.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(2)))
            val paymentname: ArrayList<String> = ArrayList()
            paymentname.add("00")
            paymentname.add("01")
            paymentname.add("02")
            paymentname.add("03")
            paymentname.add("04")
            paymentname.add("05")
            paymentname.add("06")
            paymentname.add("07")
            paymentname.add("08")
            paymentname.add("09")
            for (i in 10..99) {
                paymentname.add(i.toString())
            }
            dataStringArray = paymentname.toTypedArray()
        }
        if (getpana_type.equals("triple_pana")) {
            pana_type = "Triple Pana"
            opendigitTextView.text = "Open Panna"
            windowtitle.text = "Triple Panna"
            openDigitEditText.hint = "Enter Panna"
            openDigitEditText.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(3)))
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
                        val status = response.body()?.get("status").toString().replace(
                            "\"",
                            ""
                        )
                        if (status.equals("true")) {
                            val walletpointget =
                                response.body()?.get("wallet_amt").toString().replace(
                                    "\"",
                                    ""
                                )
                            val min_bid_amount =
                                response.body()?.get("min_bid_amount").toString().replace(
                                    "\"",
                                    ""
                                )
                            val max_bid_amount =
                                response.body()?.get("max_bid_amount").toString().replace(
                                    "\"",
                                    ""
                                )
                            val currentdate =
                                response.body()?.get("new_date").toString().replace(
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
        RetrofitClient.service.starlinegamestatusApi(jsonvalues).enqueue(
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
        RetrofitClient.service.starlinegamebidSubmitApi(sendingdata).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val msg = response.body()?.get("msg").toString().replace("\"", "")
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")
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
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            progressBar.visibility = View.GONE
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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