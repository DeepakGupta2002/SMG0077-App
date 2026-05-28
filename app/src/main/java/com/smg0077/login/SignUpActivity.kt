package com.smg0077.login

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.goodiebag.pinview.Pinview
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.smg0077.AppDelegate
import com.smg0077.HomeActivity
import com.smg0077.R
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.session.MySession
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText
import kotlinx.android.synthetic.main.activity_sign_up.backbutton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpActivity : AppCompatActivity() {
    var intentWhatsNumber:String="null"
    var intentTelegramNumber:String="null"
    private lateinit var whatsappLinear:ImageView
    private lateinit var callLinear:ImageView
//    private lateinit var telegramLinear:LinearLayout
    private lateinit var loginSignUpTV:TextView
    lateinit var username: EditText
    lateinit var appkey : AppKeyHolderClass
    lateinit var progressBar: View
    lateinit var phonenumber: EditText
    lateinit var password: ShowHidePasswordEditText
    lateinit var signupBut: Button
    private var otpMain: String? = null
    private lateinit var userphone: String
    lateinit var session: MySession
    private lateinit var otpText:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        session = MySession(this)
        initValues()

        password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


        backbutton.setOnClickListener {
            finishAffinity()
        }

        loginSignUpTV.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        signupBut.setOnClickListener {
            AppDelegate.hideSoftKeyboard(this)
            userphone = phonenumber.text.trim().toString()
            retrofitcalling_Signup()

            if (userphone.length < 10) {
                Toast.makeText(this, "Enter 10 Digit Mobile Number", Toast.LENGTH_LONG).show()
            } else {
                val check = validate()
                if (check) {

                    myHideShowProgress(true)
                    val jsonValues = JsonObject()

                    username.text.trim().toString()
                    password.text.trim().toString()

                    jsonValues.addProperty("env_type", env_type)
                    jsonValues.addProperty("app_key", AppKeyHolderClass().getAppKey())
                    jsonValues.addProperty("mobile", userphone)

                    RetrofitClient.service.checkmobilevalid(jsonValues).enqueue(
                        object : Callback<JsonObject> {
                            override fun onResponse(
                                call: Call<JsonObject>,
                                response: Response<JsonObject>
                            ) {
                                if (response.isSuccessful) {
                                    response.body()?.get("msg").toString().replace("\"", "")
                                    Toast.makeText(
                                        this@SignUpActivity,
                                        "You Are Registered Successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    myHideShowProgress(false)

//                                    println("+++++"+ Gson().toJson(response))



                                }

                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Unable to Connect to Internet",
                                    Toast.LENGTH_LONG
                                ).show()
                                myHideShowProgress(false)

                            }
                        })


                }

            }
        }
    }

    private fun initValues() {
        whatsappLinear=findViewById(R.id.signup_whatappbut)
        callLinear=findViewById(R.id.signup_callbut)
//        telegramLinear=findViewById(R.id.telegramLinear)

        loginSignUpTV=findViewById(R.id.loginSignUpTV)
        progressBar=findViewById(R.id.progressbar2)
        username=findViewById(R.id.SignUpUserName)
        phonenumber=findViewById(R.id.SignUpPhone)
        password=findViewById(R.id.SignUpPassword)
        signupBut=findViewById(R.id.SignUpBT)
//        whatsAppNumber.text=intentWhatsNumber
//        callNumber.text=intentWhatsNumber
//        telegramNumber.text=intentTelegramNumber

        whatsappLinear.setOnClickListener {
            AppDelegate.openWhatsapp(intentWhatsNumber, this)
        }
        callLinear.setOnClickListener {
            val whatsappNumber=intentWhatsNumber
            val intent= Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + whatsappNumber)
            startActivity(intent)
        }
        retrofitGetContactPhoneNumbers()
//        telegramLinear.setOnClickListener {
//            val intent=telegramIntent(this)
//            startActivity(intent)
//        }
    }
//    private fun telegramIntent(context: Context): Intent {
//        val intent: Intent?
//        val telegramNumber=telegramNumber.text.toString()
//        intent = try {
//            try {
//                context.packageManager.getPackageInfo("org.telegram.messenger", 0)//Check for Telegram Messenger App
//            } catch (e : Exception){
//                context.packageManager.getPackageInfo("org.thunderdog.challegram", 0)//Check for Telegram X App
//            }
//            Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=${telegramNumber}"))
//        }catch (e : Exception){
//            Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/$telegramNumber"))
//        }
//        return intent!!
//    }

    private fun validate():Boolean {
        val unstr = username.text.trim().toString()
        val passstr = password.text.trim().toString()
        val phonenum = phonenumber.text.trim().toString()
        var checkstatus = true

        if (unstr.isEmpty() && passstr.isEmpty() && phonenum.isEmpty()) {
            username.error = "Can't be Empty!"
//            useremail.error="Can't be Empty!"
            phonenumber.error = "Can't be Empty!"
            password.error = "Can't be Empty!"
            checkstatus = false
        } else {
            if (unstr.isEmpty()) {
                username.error = "Can't be Empty!"
                checkstatus = false
            }

            if (phonenum.isEmpty()) {
                phonenumber.error = "Can't be Empty!"
                checkstatus = false
            } else if (phonenum.length <= 9) {
                phonenumber.error = "invalid number"
                checkstatus = false
            }
            if (passstr.isEmpty()) {
                password.error="Can't be Empty!"
                checkstatus=false
            }else if(passstr.length<6){
                password.error="less than 6 Character!"
                checkstatus=false
            }
//            if(mpin.isEmpty()){
//                mPinED.error="Can't be Empty!"
//                checkstatus=false
//            }else if(mpin.length<4){
//                mPinED.error="should be 4 Character"
//                checkstatus=false
//            }

        }

        return checkstatus
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun myHideShowProgress(check:Boolean) {
        if(check) {
            progressBar.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
        else {
            progressBar.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    fun showBottomSheet() {
        var bottomSheet = Dialog(this)
        bottomSheet.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        bottomSheet!!.window!!.attributes.gravity = Gravity.CENTER
        bottomSheet.setContentView(R.layout.bottom_sheet_layout)
        bottomSheet.setCancelable(false)
        bottomSheet.window?.setLayout(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        bottomSheet.getWindow()?.attributes!!.windowAnimations = R.style.DialogZoomAnimation

        val GoButton: Button? = bottomSheet.findViewById(R.id.Otp_Go)
        val timerTV: TextView? = bottomSheet.findViewById(R.id.OtpTimer)
        val phoneNumber: TextView? = bottomSheet.findViewById(R.id.OtpNumber)
        otpText = bottomSheet.findViewById(R.id.otpText)!!
        val resendOtpTV: TextView? = bottomSheet.findViewById(R.id.resendOTP)
        val pinview: Pinview? = bottomSheet.findViewById(R.id.pinview)



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
        var InvalidPinCounter: Int = 5
        val subNumber = userphone!!.substring(6, userphone!!.length)
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
                    resendOtpTV?.visibility = View.VISIBLE
                }

            }.start()

            retrofitResendOtp()
        }

        pinview?.setPinViewEventListener { pinview, fromUser ->
            val enteredOtp = pinview.value.toString()

            if (enteredOtp == otpMain) {
                AppDelegate.hideSoftKeyboard(this)
                retrofitcalling_Signup()

            } else {
                InvalidPinCounter -= 1
                if (InvalidPinCounter != 0) {
                    if (InvalidPinCounter >= 0) {
                        val snack2 = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Invalid OTP \n You have $InvalidPinCounter attempt left",
                            Snackbar.LENGTH_LONG
                        )
                        val view: View = snack2.view
                        val params = view.layoutParams as FrameLayout.LayoutParams
                        params.gravity = Gravity.TOP
                        view.layoutParams = params
                        snack2.setBackgroundTint(Color.RED)
                        snack2.show()
                        vibrate()
                    }

                }
            }
            if (InvalidPinCounter == 0) {
                val snack2 = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Sorry, you have exceeded the maximum number of attempts to verify your identity",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snack2.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.TOP
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


        GoButton?.setOnClickListener {
            val codeentered = pinview?.value
            if (codeentered.equals(otpMain)) {
                AppDelegate.hideSoftKeyboard(this)
                retrofitcalling_Signup()
            } else {
                val snack2 = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Invalid OTP!",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snack2.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.TOP
                view.layoutParams = params
                snack2.setBackgroundTint(Color.RED)
                snack2.show()
            }
        }
        bottomSheet.show()
    }

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }

    private fun retrofitResendOtp() {
        myHideShowProgress(true)

        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("mobile", userphone)
        RetrofitClient.service.otpresent(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")
                        val newotp = response.body()?.get("otp").toString().replace("\"", "")
                        val msg = response.body()?.get("msg").toString().replace("\"", "")

                        if (status.equals("true")) {
                            otpMain = newotp
//                            otpText.text=newotp
                            Toast.makeText(applicationContext, "" + msg, Toast.LENGTH_LONG).show()
                            myHideShowProgress(false)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Something Went Wrong!",
                                Toast.LENGTH_LONG
                            ).show()
                            myHideShowProgress(false)
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(applicationContext, "Unable to Connect to Internet", Toast.LENGTH_LONG).show()
                    myHideShowProgress(false)
                }
            })

    }

    private fun retrofitcalling_Signup() {
        myHideShowProgress(true)
        val usernamestr = username.text.trim().toString()
        val useremailstr = "temp@gmail.com"
        val phonenumberstr = phonenumber.text.trim().toString()
        val passwordstr = password.text.trim().toString()
//        val mpinstr=mPinED.text.trim().toString()
        val mpinstr = "1234"

        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("name", usernamestr)
        jsonvalues.addProperty("email", useremailstr)
        jsonvalues.addProperty("mobile", phonenumberstr)
        jsonvalues.addProperty("password", passwordstr)
        jsonvalues.addProperty("security_pin", mpinstr)

        RetrofitClient.service.registeruser(jsonvalues).enqueue(
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
                            val snack2 = Snackbar.make(
                                findViewById(android.R.id.content),
                                msg,
                                Snackbar.LENGTH_LONG
                            )
                            val view: View = snack2.view
                            val params = view.layoutParams as FrameLayout.LayoutParams
                            params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                            view.layoutParams = params
                            snack2.setBackgroundTint(Color.GREEN)
                            snack2.setTextColor(Color.BLACK)
                            snack2.show()
                            gotoactivity()
                            myHideShowProgress(false)
                        } else {
                            val snack2 = Snackbar.make(
                                findViewById(android.R.id.content),
                                msg,
                                Snackbar.LENGTH_LONG
                            )
                            val view: View = snack2.view
                            val params = view.layoutParams as FrameLayout.LayoutParams
                            params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                            view.layoutParams = params
                            snack2.setBackgroundTint(Color.RED)
                            snack2.setTextColor(Color.WHITE)
                            snack2.show()
                            myHideShowProgress(false)
                        }
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(applicationContext, "Unable to Connect to Internet", Toast.LENGTH_LONG).show()
                    myHideShowProgress(false)
                }
            })
    }

    private fun gotoactivity() {
        retrofitcallingLogin()
    }

    @SuppressLint("HardwareIds")
    private fun retrofitcallingLogin() {
        myHideShowProgress(true)
        val checkDeviceId =
            Settings.Secure.getString(
                applicationContext.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        val phonenumberstr=phonenumber.text.trim().toString()
        val passwordstr=password.text.trim().toString()

        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("mobile", phonenumberstr)
        jsonvalues.addProperty("password", passwordstr)
        jsonvalues.addProperty("device_id", checkDeviceId)
        RetrofitClient.service.userlogin(jsonvalues).enqueue(
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

                            val username =
                                response.body()?.get("user_name").toString().replace("\"", "")
                            val userid = response.body()?.get("unique_token").toString()
                                .replace("\"", "")
                            val mobile =
                                response.body()?.get("mobile").toString().replace("\"", "")
                            val mobileContact =
                                response.body()?.get("mobile_no").toString().replace("\"", "")

                            session.setLogin(true)
                            session.setSession_userid(userid)
                            session.setSession_username(username)
                            session.setSession_usermobile(mobile)
                            session.setSession_Whatappnumber(mobileContact)
                            val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)

                            myHideShowProgress(false)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                msg,
                                Toast.LENGTH_LONG
                            ).show()
                            myHideShowProgress(false)
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(applicationContext, "Unable to Connect to Internet", Toast.LENGTH_LONG).show()
                    myHideShowProgress(false)

                }
            })
    }
    private fun retrofitGetContactPhoneNumbers() {
        myHideShowProgress(true)
        val jsonValues= JsonObject()
        jsonValues.addProperty("env_type", env_type)
        jsonValues.addProperty("app_key", AppKeyHolderClass().getAppKey())

        RetrofitClient.service.socialDataGet(jsonValues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val status= response.body()?.get("status")?.asString
                        if (status == "true") {
                            val whatsNumberk= response.body()?.get("mobile_no")!!.asString
                            val telegramNumberk= response.body()?.get("telegram_no")!!.asString
                            Log.e("whatsApp",whatsNumberk)
                            Log.e("telegram",whatsNumberk)
                            intentWhatsNumber=whatsNumberk
                            intentTelegramNumber=telegramNumberk

                            println("+++++"+ intentWhatsNumber)
//                            whatsAppNumber.text=intentWhatsNumber
//                            callNumber.text=intentWhatsNumber
//                            telegramNumber.text=intentTelegramNumber
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "No contact detail found",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    myHideShowProgress(false)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(applicationContext, "Unable to Connect to Internet", Toast.LENGTH_LONG).show()
                    myHideShowProgress(false)
                }

            })
    }

}