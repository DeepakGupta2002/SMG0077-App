package com.smg0077.bating

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.text.InputFilter
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.smg0077.all_adapter.BidPointsRecycleViewAdapter
import com.smg0077.all_adapter.BidPointsSpecialRecycleViewAdapter
import com.smg0077.R
import com.smg0077.login.LoginActivity
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.session.MySession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BatScreenActivity : AppCompatActivity() {
    lateinit var radioGroup: RadioGroup
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: View
    lateinit var app_key: AppKeyHolderClass
    lateinit var session: MySession
    lateinit var walletpoint: TextView
    lateinit var adaptercallingbid: BidPointsRecycleViewAdapter
    lateinit var adaptercalling: BidPointsSpecialRecycleViewAdapter
    lateinit var windoetitle: TextView
    lateinit var backbut: ImageView
    lateinit var proceedbut: Button
    lateinit var submitButton: Button
    lateinit var RadioButton: RadioButton
    lateinit var openRadioButton: RadioButton
    lateinit var closeRadioButton: RadioButton
    lateinit var currentdateTextView: TextView
    lateinit var openDigitEditText: AutoCompleteTextView
    lateinit var closedigittext: TextView
    lateinit var choosesessionTV: TextView
    lateinit var opendigitTV: TextView
    lateinit var closeDigitEditText: AutoCompleteTextView
    lateinit var pointsEditText: EditText
    var temppointlist: MutableList<String> = ArrayList()
    var tempopendigitList: MutableList<String> = ArrayList()
    var tempclosedigitList: MutableList<String> = ArrayList()
    var game_type: String = "none"
    var game_idget: String = "none"
    var game_nameget: String = "none"
    var main_min_bid_amount = 0
    var main_max_bid_amount = 0
    var total_bid_amount = 0
    private var GameStatus: String = "0"
    var pana_type: String = "none"
    lateinit var CheckMyGameStatus: String
    lateinit var dataStringArray: Array<String>
    lateinit var dataStringArray_second: Array<String>

    lateinit var checkDeviceId: String
    lateinit var checkSecurityPin: String
    lateinit var checkLogoutStatus: String
    var deviceArrayList: MutableList<String> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bid_window)
        checkDeviceId =
            Settings.Secure.getString(
                applicationContext.contentResolver,
                Settings.Secure.ANDROID_ID
            )

        initvalues()

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataStringArray)
        openDigitEditText.setAdapter(adapter)
        if (game_type.equals("half_sangam")) {

            val closeadapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1, dataStringArray_second
            )
            closeDigitEditText.setAdapter(closeadapter)
        }
        if (game_type.equals("full_sangam")) {

            val closeadapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1, dataStringArray_second
            )
            closeDigitEditText.setAdapter(closeadapter)
        }

        retrofitBid_onload_Data()
        backbut.setOnClickListener {
            onBackPressed()
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            RadioButton = findViewById(checkedId)
            if (game_type.equals("half_sangam")) {
                val valueis = RadioButton.text.toString()
                if (valueis.equals("Open")) {
                    opendigitTV.text = "Open Digit"
                    openDigitEditText.hint = "Enter Digit"
                    closeDigitEditText.hint = "Enter Pana"
                    closedigittext.text = "Close Pana"
                    openDigitEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))
                    closeDigitEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))
                    val closeadapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1, dataStringArray_second
                    )
                    closeDigitEditText.setAdapter(closeadapter)
                    val openadapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1, dataStringArray
                    )
                    openDigitEditText.setAdapter(openadapter)


                } else {
                    opendigitTV.text = "Open Pana"
                    openDigitEditText.hint = "Enter Pana"
                    closeDigitEditText.hint = "Enter Digit"
                    closedigittext.text = "Close Digit"
                    openDigitEditText.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(3)))
                    closeDigitEditText.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(1)))
                    val closeadapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1, dataStringArray_second
                    )
                    openDigitEditText.setAdapter(closeadapter)
                    val openadapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1, dataStringArray
                    )
                    closeDigitEditText.setAdapter(openadapter)

                }

            }

        }


        proceedbut.setOnClickListener(View.OnClickListener {
            val selectedRadioButtonId: Int = radioGroup.checkedRadioButtonId
            RadioButton = findViewById(selectedRadioButtonId)

            val checkid = RadioButton.text.toString()
            if (checkid.equals("Open")) {
                (radioGroup.getChildAt(1) as RadioButton).isEnabled = false
                RadioButton.text = "Open"
            } else if (checkid.equals("Close")) {
                (radioGroup.getChildAt(0) as RadioButton).isEnabled = false
                RadioButton.text = "Close"
            }

            val getopendigit = openDigitEditText.text.toString().trim()
            val getpoints = pointsEditText.text.toString().trim()
            var getclosegidit = ""
            if (game_type.equals("half_sangam") || game_type.equals("full_sangam")) {
                getclosegidit = closeDigitEditText.text.toString().trim()
                if (getclosegidit.equals("")) {
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
                    closeDigitEditText.error = "!Input Invalid"
                    return@OnClickListener
                }
                if (getclosegidit.isEmpty()) {
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
                    closeDigitEditText.error = "!Input Invalid"
                    return@OnClickListener
                }
                if (RadioButton.text.equals("Close")) {
                    if (!dataStringArray_second.contains(getopendigit)) {
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


                } else {
                    if (!dataStringArray_second.contains(getclosegidit)) {
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
                }


            }

            if (game_type.equals("half_sangam")) {
                if (RadioButton.text.equals("Close")) {


                    if (!dataStringArray.contains(getclosegidit)) {
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
                }

            } else {
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
                vibrate()
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
                    vibrate()
                    return@OnClickListener
                } else {
                    if (castpoint > main_max_bid_amount) {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Should be less than max bid amount $main_max_bid_amount",
                            Snackbar.LENGTH_LONG
                        ).show()
                        vibrate()
                        return@OnClickListener

                    } else {

                        castwallet = castwallet - castpoint
                        walletpoint.text = "\u20B9 " + castwallet.toString()
                        if (tempopendigitList.contains(getopendigit)) {

                            val index = tempopendigitList.indexOf(getopendigit)
                            val pointsdum = temppointlist.elementAt(index)
                            val castpoint2: Int = pointsdum.toInt()
                            castwallet += castpoint2
                            walletpoint.text = "\u20B9 " + castwallet.toString()
                            tempopendigitList.removeAt(index)
                            temppointlist.removeAt(index)
                            tempopendigitList.add(index, getopendigit)
                            temppointlist.add(index, getpoints)
                            if (game_type.equals("half_sangam") || game_type.equals("full_sangam")) {
                                tempclosedigitList.removeAt(index)
                                tempclosedigitList.add(index, getclosegidit)
                            }

                        } else {
                            tempopendigitList.add(getopendigit)
                            temppointlist.add(getpoints)
                            tempclosedigitList.add(getclosegidit)
                        }
                        if (game_type.equals("half_sangam") || game_type.equals("full_sangam")) {
                            if (RadioButton.text.equals("Close")) {
                                adaptercalling = BidPointsSpecialRecycleViewAdapter(
                                    applicationContext,
                                    tempclosedigitList,
                                    tempopendigitList,
                                    temppointlist,
                                    submitButton,
                                    walletpoint,
                                    radioGroup
                                )
                                recyclerView.adapter = adaptercalling
                            } else {
                                adaptercalling = BidPointsSpecialRecycleViewAdapter(
                                    applicationContext,
                                    tempopendigitList,
                                    tempclosedigitList,
                                    temppointlist,
                                    submitButton,
                                    walletpoint,
                                    radioGroup
                                )
                                recyclerView.adapter = adaptercalling
                            }
                            closeDigitEditText.text.clear()
                            openDigitEditText.text.clear()
                            pointsEditText.text.clear()
                            openDigitEditText.requestFocus()
                        } else {
                            if (CheckMyGameStatus.equals("2")) {
                                adaptercallingbid = BidPointsRecycleViewAdapter(
                                    applicationContext,
                                    tempopendigitList,
                                    temppointlist,
                                    submitButton,
                                    walletpoint,
                                    radioGroup,
                                    false
                                )
                                recyclerView.adapter = adaptercallingbid

                            } else {
                                adaptercallingbid = BidPointsRecycleViewAdapter(
                                    applicationContext,
                                    tempopendigitList,
                                    temppointlist,
                                    submitButton,
                                    walletpoint,
                                    radioGroup,
                                    true
                                )
                                recyclerView.adapter = adaptercallingbid

                            }
                            openDigitEditText.text.clear()
                            pointsEditText.text.clear()
                            openDigitEditText.requestFocus()
                        }
                    }
                }

            }
        })

        submitButton.setOnClickListener {
            total_bid_amount = 0
            retrofitCheckGameStatusinside()
            var i = 0
            var j = 0
            while (j < tempopendigitList.size) {
                var castpointstoint: Int
                castpointstoint = temppointlist[j].toInt()

                total_bid_amount += castpointstoint
                j += 1
            }

            val resultArrayData = JsonArray()

            while (i < tempopendigitList.size) {
                var digit: String
                var closedigit: String
                var sessiontype: String
                closedigit = if (game_type == "half_sangam" || game_type == "full_sangam") {
                    tempclosedigitList.elementAt(i)

                } else {
                    ""
                }
                if (game_type.equals("half_sangam")) {
                    if (RadioButton.text.equals("Close")) {
                        digit = tempclosedigitList.elementAt(i)
                        closedigit = tempopendigitList.elementAt(i)

                    } else {
                        digit = tempopendigitList.elementAt(i)
                        closedigit = tempclosedigitList.elementAt(i)
                    }
                } else {
                    digit = tempopendigitList.elementAt(i)
                }
                val pointes: String = temppointlist.elementAt(i)
                if (game_type.equals("full_sangam") || game_type.equals("jodi_digit")) {
                    sessiontype = ""
                } else {
                    sessiontype = RadioButton.text.toString()
                }

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
            val gamename = game_nameget
            val totalbid = total_bid_amount.toString()
            val currentdate = currentdateTextView.text.toString()

            newresult.addProperty("unique_token", userid)
            newresult.addProperty("Gamename", gamename)
            newresult.addProperty("totalbit", totalbid)
            newresult.addProperty("gameid", game_idget)
            newresult.addProperty("pana", pana_type)
            newresult.addProperty("bid_date", currentdate)
            if (game_type.equals("full_sangam") || game_type.equals("jodi_digit")) {
                newresult.addProperty("session", "")
            } else {
                newresult.addProperty("session", RadioButton.text.toString())
            }
            newresult.addProperty("bid_date", currentdate)
            newresult.add("result", resultArrayData)
            resultjsonobject.addProperty("app_key", AppKeyHolderClass().getAppKey())
            resultjsonobject.addProperty("env_type", env_type)
            resultjsonobject.add("new_result", newresult)  // final data to be sent

            if (CheckMyGameStatus.equals("1")) {
                ShowMessageDialog(resultjsonobject).show()
            } else {
                if (game_type.equals("jodi_digit") || game_type.equals("half_sangam") || game_type.equals(
                        "full_sangam"
                    )
                ) {
                    val snak = Snackbar.make(
                        findViewById(android.R.id.content),
                        "Sorry Betting Session Is Closed",
                        Snackbar.LENGTH_LONG
                    )
                    val view: View = snak.view
                    val params = view.layoutParams as FrameLayout.LayoutParams
                    params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                    view.layoutParams = params
                    snak.show()
                } else {
                    ShowMessageDialog(resultjsonobject).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
        game_idget = intent.getStringExtra("game_id").toString()
        game_nameget = intent.getStringExtra("game_name").toString()
        game_type = intent.getStringExtra("game_type").toString()
        GameStatus = intent.getStringExtra("game_status").toString()
        session = MySession(applicationContext)
        recyclerView = findViewById(R.id.bid_window_recycler)
        radioGroup = findViewById<RadioGroup>(R.id.session_group)
        openRadioButton = findViewById(R.id.bid_window_opensession)
        closeRadioButton = findViewById(R.id.bid_window_closesession)
        proceedbut = findViewById(R.id.user_proceed_Button)
        choosesessionTV = findViewById(R.id.bid_window_choosesessionTV)
        opendigitTV = findViewById(R.id.bid_window_opendigitTV)
        progressBar = findViewById(R.id.progressbar2)
        submitButton = findViewById(R.id.user_submit_Button)
        openDigitEditText = findViewById(R.id.bid_window_opendigit)
        closeDigitEditText = findViewById(R.id.bid_window_closedigit)
        pointsEditText = findViewById(R.id.bid_window_points)
        closedigittext = findViewById(R.id.closepanatextview)

        backbut = findViewById(R.id.userbackbut)
        currentdateTextView = findViewById(R.id.bid_window_currentDate)
        walletpoint = findViewById(R.id.bid_window_walletpoint)
        windoetitle = findViewById(R.id.bid_window_title)
        submitButton.visibility = View.GONE

        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm

        if (GameStatus.equals("2")) {
            (radioGroup.getChildAt(0) as RadioButton).isEnabled = false
            (radioGroup.getChildAt(1) as RadioButton).isChecked = true
        } else if (GameStatus.equals("1")) {
            (radioGroup.getChildAt(0) as RadioButton).isChecked = true
        }

        val checkButton = radioGroup.checkedRadioButtonId
        RadioButton = findViewById(checkButton)

        if (game_type.equals("single_digit")) {
            val SingleDigitList = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
            pana_type = "Single Digit"
            windoetitle.text = "Single Digit"
            openDigitEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))
            dataStringArray = SingleDigitList.copyOf()
        }

        if (game_type.equals("jodi_digit")) {
            radioGroup.visibility = View.GONE
            choosesessionTV.visibility = View.GONE
            pana_type = "Jodi Digit"
            windoetitle.text = "Jodi Digit"
            openDigitEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
            val numbers = ArrayList<String>(100)
            numbers.add("00")
            numbers.add("01")
            numbers.add("02")
            numbers.add("03")
            numbers.add("04")
            numbers.add("05")
            numbers.add("06")
            numbers.add("07")
            numbers.add("08")
            numbers.add("09")
            for (i in 10..99) {
                numbers.add(i.toString())
            }
            dataStringArray = numbers.toTypedArray()
        }
        if (game_type.equals("single_pana")) {
            pana_type = "Single Pana"
            windoetitle.text = "Single Panna"
            openDigitEditText.hint = "Enter Panna"
            opendigitTV.text = "Open Panna"
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
        if (game_type.equals("double_pana")) {
            pana_type = "Double Pana"
            windoetitle.text = "Double Panna"
            openDigitEditText.hint = "Enter Panna"
            opendigitTV.text = "Open Panna"
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
        if (game_type.equals("triple_pana")) {
            pana_type = "Triple Pana"
            windoetitle.text = "Triple Panna"
            openDigitEditText.hint = "Enter Panna"
            opendigitTV.text = "Open Panna"
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
        if (game_type.equals("half_sangam")) {

            closeDigitEditText.visibility = View.VISIBLE
            closedigittext.visibility = View.VISIBLE
            closedigittext.text = "Close Panna"
            closeDigitEditText.hint = "Enter Panna"
            pana_type = "Half Sangam"
            windoetitle.text = "Half Sangam"
            openDigitEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))
            closeDigitEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))
            val SingleDigitList = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
            dataStringArray = SingleDigitList.copyOf()
            val paymentnameclose: ArrayList<String> = ArrayList()

            paymentnameclose.add("000")
            paymentnameclose.add("100")
            paymentnameclose.add("110")
            paymentnameclose.add("111")
            paymentnameclose.add("112")
            paymentnameclose.add("113")
            paymentnameclose.add("114")
            paymentnameclose.add("115")
            paymentnameclose.add("116")
            paymentnameclose.add("117")
            paymentnameclose.add("118")
            paymentnameclose.add("119")
            paymentnameclose.add("120")
            paymentnameclose.add("122")
            paymentnameclose.add("123")
            paymentnameclose.add("124")
            paymentnameclose.add("125")
            paymentnameclose.add("126")
            paymentnameclose.add("127")
            paymentnameclose.add("128")
            paymentnameclose.add("129")
            paymentnameclose.add("130")
            paymentnameclose.add("133")
            paymentnameclose.add("134")
            paymentnameclose.add("135")
            paymentnameclose.add("136")
            paymentnameclose.add("137")
            paymentnameclose.add("138")
            paymentnameclose.add("139")
            paymentnameclose.add("140")
            paymentnameclose.add("144")
            paymentnameclose.add("145")
            paymentnameclose.add("146")
            paymentnameclose.add("147")
            paymentnameclose.add("148")
            paymentnameclose.add("149")
            paymentnameclose.add("150")
            paymentnameclose.add("155")
            paymentnameclose.add("156")
            paymentnameclose.add("157")
            paymentnameclose.add("158")
            paymentnameclose.add("159")
            paymentnameclose.add("160")
            paymentnameclose.add("166")
            paymentnameclose.add("167")
            paymentnameclose.add("168")
            paymentnameclose.add("169")
            paymentnameclose.add("170")

            paymentnameclose.add("177")
            paymentnameclose.add("178")
            paymentnameclose.add("179")
            paymentnameclose.add("180")
            paymentnameclose.add("188")
            paymentnameclose.add("189")
            paymentnameclose.add("190")
            paymentnameclose.add("199")

            paymentnameclose.add("200")
            paymentnameclose.add("220")
            paymentnameclose.add("222")
            paymentnameclose.add("223")
            paymentnameclose.add("224")
            paymentnameclose.add("225")
            paymentnameclose.add("226")
            paymentnameclose.add("227")
            paymentnameclose.add("228")
            paymentnameclose.add("229")
            paymentnameclose.add("230")
            paymentnameclose.add("233")
            paymentnameclose.add("234")
            paymentnameclose.add("235")
            paymentnameclose.add("236")
            paymentnameclose.add("237")
            paymentnameclose.add("238")
            paymentnameclose.add("239")
            paymentnameclose.add("240")
            paymentnameclose.add("244")
            paymentnameclose.add("245")
            paymentnameclose.add("246")
            paymentnameclose.add("247")
            paymentnameclose.add("248")
            paymentnameclose.add("249")
            paymentnameclose.add("250")
            paymentnameclose.add("255")
            paymentnameclose.add("256")
            paymentnameclose.add("257")
            paymentnameclose.add("258")
            paymentnameclose.add("259")
            paymentnameclose.add("260")
            paymentnameclose.add("266")
            paymentnameclose.add("267")
            paymentnameclose.add("268")
            paymentnameclose.add("269")
            paymentnameclose.add("270")
            paymentnameclose.add("277")
            paymentnameclose.add("278")
            paymentnameclose.add("279")
            paymentnameclose.add("280")
            paymentnameclose.add("288")
            paymentnameclose.add("289")
            paymentnameclose.add("290")
            paymentnameclose.add("299")
            paymentnameclose.add("300")
            paymentnameclose.add("330")
            paymentnameclose.add("333")
            paymentnameclose.add("334")
            paymentnameclose.add("335")
            paymentnameclose.add("336")
            paymentnameclose.add("337")
            paymentnameclose.add("338")
            paymentnameclose.add("339")
            paymentnameclose.add("340")
            paymentnameclose.add("344")
            paymentnameclose.add("345")
            paymentnameclose.add("346")
            paymentnameclose.add("347")
            paymentnameclose.add("348")
            paymentnameclose.add("349")
            paymentnameclose.add("350")
            paymentnameclose.add("355")
            paymentnameclose.add("356")
            paymentnameclose.add("357")
            paymentnameclose.add("358")
            paymentnameclose.add("359")
            paymentnameclose.add("360")
            paymentnameclose.add("366")
            paymentnameclose.add("367")
            paymentnameclose.add("368")
            paymentnameclose.add("369")
            paymentnameclose.add("370")
            paymentnameclose.add("377")
            paymentnameclose.add("378")
            paymentnameclose.add("379")
            paymentnameclose.add("380")
            paymentnameclose.add("388")
            paymentnameclose.add("389")
            paymentnameclose.add("390")
            paymentnameclose.add("399")
            paymentnameclose.add("400")
            paymentnameclose.add("440")
            paymentnameclose.add("444")
            paymentnameclose.add("445")
            paymentnameclose.add("446")
            paymentnameclose.add("447")
            paymentnameclose.add("448")
            paymentnameclose.add("449")
            paymentnameclose.add("450")
            paymentnameclose.add("455")
            paymentnameclose.add("456")
            paymentnameclose.add("457")
            paymentnameclose.add("458")
            paymentnameclose.add("459")
            paymentnameclose.add("460")
            paymentnameclose.add("466")
            paymentnameclose.add("467")
            paymentnameclose.add("468")
            paymentnameclose.add("469")
            paymentnameclose.add("470")
            paymentnameclose.add("477")
            paymentnameclose.add("478")
            paymentnameclose.add("479")
            paymentnameclose.add("480")
            paymentnameclose.add("488")
            paymentnameclose.add("489")
            paymentnameclose.add("490")
            paymentnameclose.add("499")
            paymentnameclose.add("500")
            paymentnameclose.add("550")
            paymentnameclose.add("555")
            paymentnameclose.add("556")
            paymentnameclose.add("557")
            paymentnameclose.add("558")
            paymentnameclose.add("559")
            paymentnameclose.add("560")
            paymentnameclose.add("566")
            paymentnameclose.add("567")
            paymentnameclose.add("568")
            paymentnameclose.add("569")
            paymentnameclose.add("570")
            paymentnameclose.add("577")
            paymentnameclose.add("578")
            paymentnameclose.add("579")
            paymentnameclose.add("580")
            paymentnameclose.add("588")
            paymentnameclose.add("589")
            paymentnameclose.add("590")
            paymentnameclose.add("599")
            paymentnameclose.add("600")
            paymentnameclose.add("660")
            paymentnameclose.add("666")
            paymentnameclose.add("667")
            paymentnameclose.add("668")
            paymentnameclose.add("669")
            paymentnameclose.add("670")
            paymentnameclose.add("677")
            paymentnameclose.add("678")
            paymentnameclose.add("679")
            paymentnameclose.add("680")
            paymentnameclose.add("688")
            paymentnameclose.add("689")
            paymentnameclose.add("690")
            paymentnameclose.add("699")
            paymentnameclose.add("700")
            paymentnameclose.add("770")
            paymentnameclose.add("777")
            paymentnameclose.add("778")
            paymentnameclose.add("779")
            paymentnameclose.add("780")
            paymentnameclose.add("788")
            paymentnameclose.add("789")
            paymentnameclose.add("790")
            paymentnameclose.add("799")
            paymentnameclose.add("800")
            paymentnameclose.add("880")
            paymentnameclose.add("888")
            paymentnameclose.add("889")
            paymentnameclose.add("890")
            paymentnameclose.add("899")
            paymentnameclose.add("900")
            paymentnameclose.add("990")
            paymentnameclose.add("999")

            dataStringArray_second = paymentnameclose.toTypedArray()

        }
        if (game_type.equals("full_sangam")) {
            closeDigitEditText.visibility = View.VISIBLE
            closedigittext.visibility = View.VISIBLE
            radioGroup.visibility = View.GONE
            choosesessionTV.visibility = View.GONE
            pana_type = "Full Sangam"
            windoetitle.text = "Full Sangam"
            openDigitEditText.hint = "Enter Pana"
            opendigitTV.text = "Open Pana"
            closedigittext.text = "Close Pana"
            closeDigitEditText.hint = "Enter Pana"
            openDigitEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))
            closeDigitEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))
            val paymentnameclose: ArrayList<String> = ArrayList()

            paymentnameclose.add("000")
            paymentnameclose.add("100")
            paymentnameclose.add("110")
            paymentnameclose.add("111")
            paymentnameclose.add("112")
            paymentnameclose.add("113")
            paymentnameclose.add("114")
            paymentnameclose.add("115")
            paymentnameclose.add("116")
            paymentnameclose.add("117")
            paymentnameclose.add("118")
            paymentnameclose.add("119")
            paymentnameclose.add("120")
            paymentnameclose.add("122")
            paymentnameclose.add("123")
            paymentnameclose.add("124")
            paymentnameclose.add("125")
            paymentnameclose.add("126")
            paymentnameclose.add("127")
            paymentnameclose.add("128")
            paymentnameclose.add("129")
            paymentnameclose.add("130")
            paymentnameclose.add("133")
            paymentnameclose.add("134")
            paymentnameclose.add("135")
            paymentnameclose.add("136")
            paymentnameclose.add("137")
            paymentnameclose.add("138")
            paymentnameclose.add("139")
            paymentnameclose.add("140")
            paymentnameclose.add("144")
            paymentnameclose.add("145")
            paymentnameclose.add("146")
            paymentnameclose.add("147")
            paymentnameclose.add("148")
            paymentnameclose.add("149")
            paymentnameclose.add("150")
            paymentnameclose.add("155")
            paymentnameclose.add("156")
            paymentnameclose.add("157")
            paymentnameclose.add("158")
            paymentnameclose.add("159")
            paymentnameclose.add("160")
            paymentnameclose.add("166")
            paymentnameclose.add("167")
            paymentnameclose.add("168")
            paymentnameclose.add("169")
            paymentnameclose.add("170")

            paymentnameclose.add("177")
            paymentnameclose.add("178")
            paymentnameclose.add("179")
            paymentnameclose.add("180")
            paymentnameclose.add("188")
            paymentnameclose.add("189")
            paymentnameclose.add("190")
            paymentnameclose.add("199")

            paymentnameclose.add("200")
            paymentnameclose.add("220")
            paymentnameclose.add("222")
            paymentnameclose.add("223")
            paymentnameclose.add("224")
            paymentnameclose.add("225")
            paymentnameclose.add("226")
            paymentnameclose.add("227")
            paymentnameclose.add("228")
            paymentnameclose.add("229")
            paymentnameclose.add("230")
            paymentnameclose.add("233")
            paymentnameclose.add("234")
            paymentnameclose.add("235")
            paymentnameclose.add("236")
            paymentnameclose.add("237")
            paymentnameclose.add("238")
            paymentnameclose.add("239")
            paymentnameclose.add("240")
            paymentnameclose.add("244")
            paymentnameclose.add("245")
            paymentnameclose.add("246")
            paymentnameclose.add("247")
            paymentnameclose.add("248")
            paymentnameclose.add("249")
            paymentnameclose.add("250")
            paymentnameclose.add("255")
            paymentnameclose.add("256")
            paymentnameclose.add("257")
            paymentnameclose.add("258")
            paymentnameclose.add("259")
            paymentnameclose.add("260")
            paymentnameclose.add("266")
            paymentnameclose.add("267")
            paymentnameclose.add("268")
            paymentnameclose.add("269")
            paymentnameclose.add("270")
            paymentnameclose.add("277")
            paymentnameclose.add("278")
            paymentnameclose.add("279")
            paymentnameclose.add("280")
            paymentnameclose.add("288")
            paymentnameclose.add("289")
            paymentnameclose.add("290")
            paymentnameclose.add("299")
            paymentnameclose.add("300")
            paymentnameclose.add("330")
            paymentnameclose.add("333")
            paymentnameclose.add("334")
            paymentnameclose.add("335")
            paymentnameclose.add("336")
            paymentnameclose.add("337")
            paymentnameclose.add("338")
            paymentnameclose.add("339")
            paymentnameclose.add("340")
            paymentnameclose.add("344")
            paymentnameclose.add("345")
            paymentnameclose.add("346")
            paymentnameclose.add("347")
            paymentnameclose.add("348")
            paymentnameclose.add("349")
            paymentnameclose.add("350")
            paymentnameclose.add("355")
            paymentnameclose.add("356")
            paymentnameclose.add("357")
            paymentnameclose.add("358")
            paymentnameclose.add("359")
            paymentnameclose.add("360")
            paymentnameclose.add("366")
            paymentnameclose.add("367")
            paymentnameclose.add("368")
            paymentnameclose.add("369")
            paymentnameclose.add("370")
            paymentnameclose.add("377")
            paymentnameclose.add("378")
            paymentnameclose.add("379")
            paymentnameclose.add("380")
            paymentnameclose.add("388")
            paymentnameclose.add("389")
            paymentnameclose.add("390")
            paymentnameclose.add("399")
            paymentnameclose.add("400")
            paymentnameclose.add("440")
            paymentnameclose.add("444")
            paymentnameclose.add("445")
            paymentnameclose.add("446")
            paymentnameclose.add("447")
            paymentnameclose.add("448")
            paymentnameclose.add("449")
            paymentnameclose.add("450")
            paymentnameclose.add("455")
            paymentnameclose.add("456")
            paymentnameclose.add("457")
            paymentnameclose.add("458")
            paymentnameclose.add("459")
            paymentnameclose.add("460")
            paymentnameclose.add("466")
            paymentnameclose.add("467")
            paymentnameclose.add("468")
            paymentnameclose.add("469")
            paymentnameclose.add("470")
            paymentnameclose.add("477")
            paymentnameclose.add("478")
            paymentnameclose.add("479")
            paymentnameclose.add("480")
            paymentnameclose.add("488")
            paymentnameclose.add("489")
            paymentnameclose.add("490")
            paymentnameclose.add("499")
            paymentnameclose.add("500")
            paymentnameclose.add("550")
            paymentnameclose.add("555")
            paymentnameclose.add("556")
            paymentnameclose.add("557")
            paymentnameclose.add("558")
            paymentnameclose.add("559")
            paymentnameclose.add("560")
            paymentnameclose.add("566")
            paymentnameclose.add("567")
            paymentnameclose.add("568")
            paymentnameclose.add("569")
            paymentnameclose.add("570")
            paymentnameclose.add("577")
            paymentnameclose.add("578")
            paymentnameclose.add("579")
            paymentnameclose.add("580")
            paymentnameclose.add("588")
            paymentnameclose.add("589")
            paymentnameclose.add("590")
            paymentnameclose.add("599")
            paymentnameclose.add("600")
            paymentnameclose.add("660")
            paymentnameclose.add("666")
            paymentnameclose.add("667")
            paymentnameclose.add("668")
            paymentnameclose.add("669")
            paymentnameclose.add("670")
            paymentnameclose.add("677")
            paymentnameclose.add("678")
            paymentnameclose.add("679")
            paymentnameclose.add("680")
            paymentnameclose.add("688")
            paymentnameclose.add("689")
            paymentnameclose.add("690")
            paymentnameclose.add("699")
            paymentnameclose.add("700")
            paymentnameclose.add("770")
            paymentnameclose.add("777")
            paymentnameclose.add("778")
            paymentnameclose.add("779")
            paymentnameclose.add("780")
            paymentnameclose.add("788")
            paymentnameclose.add("789")
            paymentnameclose.add("790")
            paymentnameclose.add("799")
            paymentnameclose.add("800")
            paymentnameclose.add("880")
            paymentnameclose.add("888")
            paymentnameclose.add("889")
            paymentnameclose.add("890")
            paymentnameclose.add("899")
            paymentnameclose.add("900")
            paymentnameclose.add("990")
            paymentnameclose.add("999")

            dataStringArray = paymentnameclose.toTypedArray()
            dataStringArray_second = paymentnameclose.toTypedArray()

        }
        retrofitCheckGameStatus()
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

    private fun retrofitBid_onload_Data() {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("game_id", game_idget)

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
        val uniqueToken = session.getSession_userid()
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("game_id", game_idget)
        jsonvalues.addProperty("unique_token", uniqueToken)

        RetrofitClient.service.Checkgamestatus(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val game_status =
                            response.body()?.get("game_status").toString().replace("\"", "")
                        myHideShowProgress(false)
                        CheckMyGameStatus = game_status
                        val deviceArray = response.body()?.getAsJsonArray("device_result")!!
                        deviceArrayList.clear()
                        checkSecurityPin = "1"
                        checkLogoutStatus = "1"
                        var j = 0
                        while (j < deviceArray.size()) {
                            val jsonObject = deviceArray[j] as JsonObject
                            j++
                            val device = jsonObject.get("device_id").toString().replace("\"", "")
                            val securityPin =
                                jsonObject.get("security_pin_status").toString().replace("\"", "")
                            val logoutStatus =
                                jsonObject.get("logout_status").toString().replace("\"", "")

                            if (device == checkDeviceId) {
                                checkSecurityPin = securityPin
                                checkLogoutStatus = logoutStatus
                            }
                            deviceArrayList.add(device)
                        }
                    }
                    checkCondition()
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

    private fun retrofitCheckGameStatusinside() {
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("game_id", game_idget)

        RetrofitClient.service.Checkgamestatus(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val game_status =
                            response.body()?.get("game_status").toString().replace("\"", "")
                        CheckMyGameStatus = game_status
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }
            })
    }

    private fun retrofitcalling(sendingdata: JsonObject) {
        myHideShowProgress(true)
        RetrofitClient.service.SubmitBidApi(sendingdata).enqueue(
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
            tempclosedigitList.clear()
            submitButton.visibility = View.GONE

            if (game_type.equals("half_sangam") || game_type.equals("full_sangam")) {
                closeDigitEditText.text.clear()
                adaptercalling.clear()
            } else {
                adaptercallingbid.clear()
            }
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
            if (GameStatus.equals("2")) {
                (radioGroup.getChildAt(0) as RadioButton).isEnabled = false
                (radioGroup.getChildAt(1) as RadioButton).isChecked = true
            } else if (GameStatus.equals("1")) {
                (radioGroup.getChildAt(0) as RadioButton).isChecked = true
                (radioGroup.getChildAt(0) as RadioButton).isEnabled = true
                (radioGroup.getChildAt(1) as RadioButton).isEnabled = true
            }
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
            initvalues()
            alertDialog.dismiss()
        }
        return alertDialog
    }

    private fun checkCondition() {
        if (checkLogoutStatus == "1") {
            logoutSession()
        }
        if (checkSecurityPin == "1") {
            logoutSession()
        }

        if (!deviceArrayList.contains(checkDeviceId)) {
            logoutSession()
        }
    }

    private fun logoutSession() {
        session.setLogin(false)
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}