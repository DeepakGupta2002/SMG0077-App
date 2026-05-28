package com.smg0077.transfer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.goodiebag.pinview.Pinview
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.smg0077.all_adapter.WalletHistoryRecyclerAdapter
import com.smg0077.model.Wallethistorydataholder
import com.smg0077.R
import com.smg0077.login.LoginActivity
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.session.MySession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserWalletActivity : AppCompatActivity() {
    lateinit var session: MySession
    lateinit var swipetorefresh: SwipeRefreshLayout
    lateinit var userback: ImageView
    lateinit var progressBar: View
    var userphone: String = "none"
    var otpMain: String = "none"

    lateinit var useraccountpoint: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var Transfer_Status: String
    lateinit var Withdraw_Status: String
    lateinit var Min_Withdraw: String
    var registernumberget_Paytm: String = ""
    var registernumberget_Googlepay: String = ""
    var registernumberget_phonepe: String = ""
    lateinit var Max_Withdraw: String
    lateinit var Min_Transfer: String
    lateinit var phonenumerget: String
    lateinit var Max_Transfer: String
    lateinit var transferpoint_BUT: CardView
    lateinit var paytm: RelativeLayout
    var Adapterarrylist: ArrayList<Wallethistorydataholder> = ArrayList()
    lateinit var app_key: AppKeyHolderClass
    private var upiCode: Int = 0
    lateinit var checkDeviceId: String
    lateinit var checkSecurityPin: String
    lateinit var checkLogoutStatus: String
    var deviceArrayList: MutableList<String> = ArrayList()

    lateinit var walletOtp: TextView
    lateinit var pinview: Pinview


    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_wallet)
        session = MySession(applicationContext)
        Transfer_Status = intent.getStringExtra("transfer_status").toString()
        Withdraw_Status = intent.getStringExtra("withdraw_status").toString()
        checkDeviceId = Settings.Secure.getString(
            applicationContext.contentResolver, Settings.Secure.ANDROID_ID
        )

        initvalues()
        userphone = session.getSession_usermobile()
        if (Transfer_Status == "0") {
            transferpoint_BUT.visibility = View.INVISIBLE


        }
        phonenumerget = "non"

        retrofitpaymentHistorydata()


        swipetorefresh.setOnRefreshListener {
            retrofitpaymentHistorydata()
            swipetorefresh.isRefreshing = false
        }

        userback.setOnClickListener {
            onBackPressed()
        }

    }

    private fun initvalues() {
        userback = findViewById(R.id.userbackbut)
        progressBar = findViewById(R.id.progressbar2)
        transferpoint_BUT = findViewById(R.id.tranferrelative)
        useraccountpoint = findViewById(R.id.updatedpointtxt)
        recyclerView = findViewById(R.id.user_account_myrecyclerview)
        val llm = LinearLayoutManager(this)
        swipetorefresh = findViewById(R.id.account_swipetorefresh)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        RetrofitCheckNumber()
        retrofitRefreshAmt()
    }

    fun showBottomSheet(calling: Int) {
        retrofitOtpSend()
        val bottomSheet = BottomSheetDialog(this)
        bottomSheet.setContentView(R.layout.bottom_sheet_layout)
        bottomSheet.dismissWithAnimation = true
        bottomSheet.setCanceledOnTouchOutside(false)
        val goBT: Button? = bottomSheet.findViewById(R.id.Otp_Go)
        val timerTV: TextView? = bottomSheet.findViewById(R.id.OtpTimer)
        walletOtp= bottomSheet.findViewById(R.id.otpText)!!
        val phoneNumber: TextView? = bottomSheet.findViewById(R.id.OtpNumber)
        val resendOtpTV: TextView? = bottomSheet.findViewById(R.id.resendOTP)
          pinview = bottomSheet.findViewById(R.id.pinview)!!
//        walletOtp.text =otpMain
//        pinview.value= otpMain

        object : CountDownTimer(31000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTV?.text = (millisUntilFinished / 1000).toString() + " Second"
            }

            override fun onFinish() {
                timerTV?.setTextColor(Color.BLACK)
                timerTV?.text = "Time Out"
                resendOtpTV?.visibility = View.VISIBLE
            }

        }.start()
        var Invalid_Pin_Counter: Int = 5
        val userphone = session.getSession_usermobile()
        val subNumber = userphone.substring(6, userphone.length)
        phoneNumber?.text = "XXXXXX" + subNumber
        resendOtpTV?.setOnClickListener {
            resendOtpTV.visibility = View.INVISIBLE
            object : CountDownTimer(31000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timerTV?.text = (millisUntilFinished / 1000).toString() + " Second"
                }

                override fun onFinish() {
                    timerTV?.setTextColor(Color.RED)
                    timerTV?.text = "Time Out"
                    resendOtpTV.visibility = View.VISIBLE
                }

            }.start()

            retrofitresendotp()
        }

        pinview?.setPinViewEventListener { pinview, _ ->
            val enteredOtp = pinview.value.toString()
            if (enteredOtp == otpMain) {
                when (calling) {
                    1 -> {
                        RetrofitAddnumber(phonenumerget, "1")
                        bottomSheet.dismiss()
                    }
                    2 -> {
                        RetrofitAddnumber(phonenumerget, "2")
                        bottomSheet.dismiss()
                    }
                    3 -> {
                        RetrofitAddnumber(phonenumerget, "3")
                        bottomSheet.dismiss()
                    }
                }

            } else {
                Invalid_Pin_Counter -= 1
                if (Invalid_Pin_Counter != 0) {
                    if (Invalid_Pin_Counter >= 0) {
                        val snack2 = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Invalid OTP \n You have $Invalid_Pin_Counter attempt left",
                            Snackbar.LENGTH_LONG
                        )
                        val view: View = snack2.view
                        val params = view.layoutParams as FrameLayout.LayoutParams
                        params.gravity = Gravity.CENTER
                        view.layoutParams = params
                        snack2.setBackgroundTint(Color.RED)
                        snack2.show()
                        vibrate()
                    }

                }
            }
            if (Invalid_Pin_Counter == 0) {
                val snack2 = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Sorry, you have exceeded the maximum number of attempts to verify your identity",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snack2.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER
                view.layoutParams = params
                snack2.setBackgroundTint(Color.RED)
                snack2.show()
                vibrate()
                Handler().postDelayed({
                    bottomSheet.dismiss()
                }, 2000)
            }
            pinview.clearValue()
        }

        goBT?.setOnClickListener {
            val codeentered = pinview?.value
            if (codeentered.equals(otpMain)) {
                when (calling) {
                    1 -> {
                        RetrofitAddnumber(phonenumerget, "1")
                        bottomSheet.dismiss()
                    }
                    2 -> {
                        RetrofitAddnumber(phonenumerget, "2")
                        bottomSheet.dismiss()
                    }
                    3 -> {
                        RetrofitAddnumber(phonenumerget, "3")
                        bottomSheet.dismiss()
                    }
                }
            } else {
                val snack2 = Snackbar.make(
                    findViewById(android.R.id.content), "Invalid OTP!", Snackbar.LENGTH_LONG
                )
                val view: View = snack2.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER
                view.layoutParams = params
                snack2.setBackgroundTint(Color.RED)
                snack2.show()
            }

        }
        bottomSheet.show()
    }

    private fun retrofitresendotp() {
        myHideShowProgress(true)

        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("mobile", userphone)

        RetrofitClient.service.otpresent(jsonvalues).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>, response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    val status = response.body()?.get("status").toString().replace("\"", "")
                    val newotp = response.body()?.get("otp").toString().replace("\"", "")
                    val msg = response.body()?.get("msg").toString().replace("\"", "")
                    if (status.equals("true")) {
                        otpMain = newotp
                        pinview.value= newotp
                        walletOtp.text=newotp
                        Toast.makeText(applicationContext, "" + msg, Toast.LENGTH_LONG).show()
                        myHideShowProgress(false)
                    } else {
                        Toast.makeText(
                            applicationContext, "Something Went Wrong!", Toast.LENGTH_LONG
                        ).show()
                        myHideShowProgress(false)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(
                    applicationContext, "Unable to Connect to Internet", Toast.LENGTH_LONG
                ).show()
                myHideShowProgress(false)
            }
        })

    }

    private fun retrofitOtpSend() {
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("mobile", userphone)

        RetrofitClient.service.otpresent(jsonvalues).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>, response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    val status = response.body()?.get("status").toString().replace("\"", "")
                    val newotp = response.body()?.get("otp").toString().replace("\"", "")
                    response.body()?.get("msg").toString().replace("\"", "")

                    if (status.equals("true")) {
                        otpMain = newotp
                        pinview.value= newotp
                        walletOtp.text=newotp
                    } else {
                        Toast.makeText(
                            applicationContext, "Something Went Wrong!", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(
                    applicationContext, "Unable to Connect to Internet", Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }


    fun withdrowpoint() {
        if (Withdraw_Status.equals("0")) {
            val snak = Snackbar.make(
                findViewById(android.R.id.content),
                "Withdraw Not allowed contact admin!",
                Snackbar.LENGTH_LONG
            )
            val view: View = snak.view
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
            view.layoutParams = params
            snak.show()

        } else {
            val intent = Intent(this, PointWithdrawActivity::class.java)
            intent.putExtra("min_withdraw", Min_Withdraw)
            intent.putExtra("max_withdraw", Max_Withdraw)
            intent.putExtra("withdraw_status", Withdraw_Status)
            startActivity(intent)
        }
    }

    fun transferpoint() {
        if (Transfer_Status.equals("0")) {
            val snak = Snackbar.make(
                findViewById(android.R.id.content),
                "Transfer Not allowed contact admin!",
                Snackbar.LENGTH_LONG
            )
            val view: View = snak.view
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
            view.layoutParams = params
            snak.show()
        } else {
            val intent = Intent(this, PointTransferActivity::class.java)
            intent.putExtra("min_transfer", Min_Transfer)
            intent.putExtra("max_transfer", Max_Transfer)
            intent.putExtra("transfer_status", Transfer_Status)
            startActivity(intent)
        }
    }

    fun addpoints() {
        startActivity(Intent(this, PointAddViaQRActivity::class.java))
    }

    override fun onResume() {
        retrofitpaymentHistorydata()
        super.onResume()
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

    private fun retrofitRefreshAmt() {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())

        RetrofitClient.service.refreshAmtApi(jsonvalues).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>, response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    val status = response.body()?.get("status").toString().replace("\"", "")
                    val msg = response.body()?.get("msg").toString().replace("\"", "")

                    if (status.equals("true")) {
                        val walletamt =
                            response.body()?.get("wallet_amt").toString().replace("\"", "")
                        useraccountpoint.text = "\u20B9 " + walletamt

                        val min_transfer =
                            response.body()?.get("min_transfer").toString().replace("\"", "")
                        val max_transfer =
                            response.body()?.get("max_transfer").toString().replace("\"", "")
                        val min_Withdraw = response.body()?.get("min_withdrawal").toString()
                            .replace("\"", "")
                        val max_Withdraw = response.body()?.get("max_withdrawal").toString()
                            .replace("\"", "")
                        Min_Transfer = min_transfer
                        Max_Transfer = max_transfer
                        Min_Withdraw = min_Withdraw
                        Max_Withdraw = max_Withdraw

                        myHideShowProgress(false)
                    } else {
                        Toast.makeText(
                            applicationContext, msg, Toast.LENGTH_LONG
                        ).show()
                        myHideShowProgress(false)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(
                    applicationContext, "Unable to Connect to Internet", Toast.LENGTH_LONG
                ).show()
                myHideShowProgress(false)
            }
        })
    }

    private fun retrofitpaymentHistorydata() {

        Adapterarrylist.clear()
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        RetrofitClient.service.walletTranHistoryApi(jsonvalues)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: Response<JsonObject>
                ) {
                    Adapterarrylist.clear()
                    if (response.isSuccessful) {
                        val status = response.body()?.get("status").toString().replace(
                            "\"", ""
                        )

                        if (status.equals("true")) {

                            var Paymentobject: JsonObject
                            val walletamt = response.body()?.get("wallet_amt").toString()
                                .replace("\"", "")
                            useraccountpoint.text = "\u20B9 " + walletamt
                            val gamerateArrayObject =
                                response.body()?.getAsJsonArray("transaction_history")!!
                            var i = 0
                            while (i < gamerateArrayObject.size()) {
                                Paymentobject = gamerateArrayObject[i] as JsonObject
                                i++
                                val amountstr = Paymentobject.get("amount").toString().replace(
                                    "\"", ""
                                )
                                val trasnote =
                                    Paymentobject.get("transaction_note").toString().replace(
                                        "\"", ""
                                    )
                                val datestr = Paymentobject.get("insert_date").toString().replace(
                                    "\"", ""
                                )
                                val amountstatus =
                                    Paymentobject.get("amount_status").toString().replace(
                                        "\"", ""
                                    )
                                val transtype =
                                    Paymentobject.get("transaction_type").toString().replace(
                                        "\"", ""
                                    )
                                Adapterarrylist.add(
                                    Wallethistorydataholder(
                                        amountstr, trasnote, amountstatus, transtype, datestr
                                    )
                                )
                            }
                            val deviceArray = response.body()?.getAsJsonArray("device_result")!!
                            deviceArrayList.clear()
                            checkSecurityPin = "1"
                            checkLogoutStatus = "1"
                            var j = 0
                            while (j < deviceArray.size()) {
                                val jsonObject = deviceArray[j] as JsonObject
                                j++
                                val device =
                                    jsonObject.get("device_id").toString().replace("\"", "")
                                val securityPin = jsonObject.get("security_pin_status").toString()
                                    .replace("\"", "")
                                val logoutStatus =
                                    jsonObject.get("logout_status").toString().replace("\"", "")
                                Log.e("check", "$securityPin and $logoutStatus")
                                if (device == checkDeviceId) {
                                    checkSecurityPin = securityPin
                                    checkLogoutStatus = logoutStatus
                                }

                                deviceArrayList.add(device)

                            }

                            val adaptercalling = WalletHistoryRecyclerAdapter(
                                applicationContext, Adapterarrylist
                            )
                            recyclerView.adapter = adaptercalling
                            checkCondition()
                            myHideShowProgress(false)
                        } else {
                            Toast.makeText(
                                applicationContext, "No Record Found!", Toast.LENGTH_LONG
                            ).show()
                            myHideShowProgress(false)
                        }

                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(
                        applicationContext, "Unable to Connect to Internet", Toast.LENGTH_LONG
                    ).show()
                    myHideShowProgress(false)
                }
            })
    }

    private fun RetrofitCheckNumber() {

        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        RetrofitClient.service.CheckuserUpinumberAPI(jsonvalues)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")
                        response.body()?.get("msg").toString().replace("\"", "")

                        if (status.equals("true")) {
                            val paymentdetailarray =
                                response.body()?.getAsJsonArray("payment_details")
                            var itemsjsonobject = JsonObject()
                            itemsjsonobject = paymentdetailarray?.get(0) as JsonObject
                            val paytmno =
                                itemsjsonobject.get("paytm_number").toString().replace("\"", "")
                            val googleno = itemsjsonobject.get("google_pay_number").toString()
                                .replace("\"", "")
                            val phonepeno =
                                itemsjsonobject.get("phone_pay_number").toString().replace("\"", "")

                            registernumberget_Paytm = paytmno
                            registernumberget_Googlepay = googleno
                            registernumberget_phonepe = phonepeno

                            myHideShowProgress(false)
                        } else {
                            myHideShowProgress(false)
                        }
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(
                        applicationContext, "Unable to Connect to Internet", Toast.LENGTH_LONG
                    ).show()
                    myHideShowProgress(false)
                }
            })

    }

    fun showdialog1(upi_type: String): AlertDialog {
        val alert = AlertDialog.Builder(this)
        val myview = layoutInflater.inflate(R.layout.custom_dialog_box, null)
        alert.setView(myview)
        val phoneEd = myview.findViewById<EditText>(R.id.txt_input)
        val cancelbut = myview.findViewById<Button>(R.id.btn_cancel)
        val addphoneBut = myview.findViewById<Button>(R.id.btn_okay)
        var calling = 0

        val alertDialog = alert.create()
        if (upi_type == "1") {
            phoneEd.setText(registernumberget_Paytm)
            upiCode = 63254
            calling = 1
        }
        if (upi_type == "2") {
            phoneEd.setText(registernumberget_Googlepay)
            upiCode = 25468
            calling = 2
        }
        if (upi_type == "3") {
            phoneEd.setText(registernumberget_phonepe)
            upiCode = 10235
            calling = 3
        }

        alertDialog.setCanceledOnTouchOutside(false)
        cancelbut.setOnClickListener {
            alertDialog.dismiss()
        }
        addphoneBut.setOnClickListener {
            phonenumerget = phoneEd.text.toString()
            if (phonenumerget.isEmpty()) {
                phoneEd.error = "Can't be empty!"
            } else {
                if (phonenumerget.length < 10) {
                    phoneEd.error = "Invalid number!"
                } else {
                    showBottomSheet(calling)
                    alertDialog.dismiss()
                }
            }
        }
        return alertDialog
    }

    private fun logoutSession() {
        session.setLogin(false)
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
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

    private fun RetrofitAddnumber(phonenumerget: String, upi_type: String) {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("upi_type", upi_type)
        if (upi_type.equals("1")) {
            jsonvalues.addProperty("paytm_no", phonenumerget)
            jsonvalues.addProperty("google_pay_no", "")
            jsonvalues.addProperty("phon_pay_no", "")
        } else if (upi_type.equals("2")) {
            jsonvalues.addProperty("paytm_no", "")
            jsonvalues.addProperty("google_pay_no", phonenumerget)
            jsonvalues.addProperty("phon_pay_no", "")
        } else {
            jsonvalues.addProperty("paytm_no", "")
            jsonvalues.addProperty("google_pay_no", "")
            jsonvalues.addProperty("phon_pay_no", phonenumerget)
        }
        RetrofitClient.service.AddusernumberAPI(jsonvalues).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>, response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    val status = response.body()?.get("status").toString().replace("\"", "")
                    val msg = response.body()?.get("msg").toString().replace("\"", "")
                    if (status.equals("true")) {
                        if (upi_type.equals("1")) {
                            registernumberget_Paytm = phonenumerget
                        } else if (upi_type.equals("2")) {
                            registernumberget_Googlepay = phonenumerget
                        } else {
                            registernumberget_phonepe = phonenumerget
                        }

                        Toast.makeText(
                            applicationContext, msg, Toast.LENGTH_LONG
                        ).show()

                        myHideShowProgress(false)
                    } else {
                        Toast.makeText(
                            applicationContext, "Invalid User!", Toast.LENGTH_LONG
                        ).show()
                        myHideShowProgress(false)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(
                    applicationContext, "Unable to Connect to Internet", Toast.LENGTH_LONG
                ).show()
                myHideShowProgress(false)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 63254) {
            if (resultCode == RESULT_OK) {
                val responseStatus = data?.getStringExtra("Result").toString()
                if (responseStatus == "Success") {
                    RetrofitAddnumber(phonenumerget, "1") //for Paytm
                } else {
                    val snak = Snackbar.make(
                        findViewById(android.R.id.content),
                        "Something went wrong! \n Request is not processed",
                        Snackbar.LENGTH_LONG
                    )
                    val view: View = snak.view
                    val params = view.layoutParams as FrameLayout.LayoutParams
                    params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                    view.layoutParams = params
                    snak.setTextColor(Color.WHITE)
                    snak.setBackgroundTint(Color.RED)
                    snak.show()
                    Handler().postDelayed({}, 200)
                }
            }
            if (resultCode == RESULT_CANCELED) {

                val snak = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Something went wrong! \n Request is not processed",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snak.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                view.layoutParams = params
                snak.setTextColor(Color.WHITE)
                snak.setBackgroundTint(Color.RED)
                snak.show()
                Handler().postDelayed({}, 200)
            }
        }

        if (requestCode == 10235) {
            if (resultCode == RESULT_OK) {
                val responseStatus = data?.getStringExtra("Result").toString()
                if (responseStatus == "Success") {
                    RetrofitAddnumber(phonenumerget, "3")
                } else {
                    val snak = Snackbar.make(
                        findViewById(android.R.id.content),
                        "Something went wrong! \n Request is not processed",
                        Snackbar.LENGTH_LONG
                    )
                    val view: View = snak.view
                    val params = view.layoutParams as FrameLayout.LayoutParams
                    params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                    view.layoutParams = params
                    snak.setTextColor(Color.WHITE)
                    snak.setBackgroundTint(Color.RED)
                    snak.show()
                    Handler().postDelayed({}, 200)
                }
            }
            if (resultCode == RESULT_CANCELED) {

                val snak = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Something went wrong! \n Request is not processed",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snak.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                view.layoutParams = params
                snak.setTextColor(Color.WHITE)
                snak.setBackgroundTint(Color.RED)
                snak.show()
                Handler().postDelayed({}, 200)
            }
        }

        if (requestCode == 25468) {
            if (resultCode == RESULT_OK) {
                val responseStatus = data?.getStringExtra("Result").toString()
                if (responseStatus == "Success") {
                    RetrofitAddnumber(phonenumerget, "2")
                } else {
                    val snak = Snackbar.make(
                        findViewById(android.R.id.content),
                        "Something went wrong! \n Request is not processed",
                        Snackbar.LENGTH_LONG
                    )
                    val view: View = snak.view
                    val params = view.layoutParams as FrameLayout.LayoutParams
                    params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                    view.layoutParams = params
                    snak.setTextColor(Color.WHITE)
                    snak.setBackgroundTint(Color.RED)
                    snak.show()
                    Handler().postDelayed({}, 200)
                }
            }
            if (resultCode == RESULT_CANCELED) {
                val snak = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Something went wrong! \n Request is not processed",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snak.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                view.layoutParams = params
                snak.setTextColor(Color.WHITE)
                snak.setBackgroundTint(Color.RED)
                snak.show()
                Handler().postDelayed({}, 200)
            }

        }
    }

}