package com.smg0077.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.smg0077.session.MySession
import com.smg0077.retrofit.RetrofitClient
import com.goodiebag.pinview.Pinview
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.smg0077.HomeActivity
import com.smg0077.R
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient.env_type
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpVerifyActivity : AppCompatActivity() {
    private lateinit var goBT: Button
    private lateinit var timerTV: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var resendOtpTV: TextView

    private lateinit var withdrawText: TextView
    private lateinit var appLogoImageView: ImageView
    private var result: String? = null
    private var username: String? = null
    private var useremail: String? = null
    private var userpassword: String? = null
    private var userphone: String? = null
    private var usermpin: String? = null
    private var otp: String? = null
    lateinit var session: MySession
    lateinit var pinview: Pinview
    lateinit var callingActivity: String
    private lateinit var otpNo: TextView

    var Invalid_Pin_Counter: Int = 5
    lateinit var progressBar: View
    lateinit var app_key: AppKeyHolderClass
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_window)
        session = MySession(this)
        initvalues()
        pinview.setTextColor(resources.getColor(R.color.black))
        resendOtpTV.setOnClickListener {
            resendOtpTV.visibility = View.INVISIBLE
            countDownFun()
            retrofitresendotp()
        }

        pinview.setPinViewEventListener { pinview, _ ->
            val enteredOtp = pinview.value.toString()
            if (enteredOtp == otp) {
                if (callingActivity == "UserWalletPaisa") {
                    val intent = Intent()
                    intent.putExtra("Result", "Success")
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    if (result.equals("forgotPassAct")) {
                        gotoactivity()
                    } else {
                        retrofitcalling_Signup()
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
                        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
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
                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                view.layoutParams = params
                snack2.setBackgroundTint(Color.RED)
                snack2.show()
                vibrate()
                Handler().postDelayed({
                    onBackPressed()
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
                }, 2000)

            }
            pinview.clearValue()

        }

        goBT.setOnClickListener {
            val codeentered = pinview.value
            if (codeentered.equals(otp)) {
                if (result.equals("forgotPassAct")) {
                    gotoactivity()
                } else {
                    retrofitcalling_Signup()
                }
            } else {
                val snack2 = Snackbar.make(
                    findViewById(android.R.id.content), "Invalid OTP!", Snackbar.LENGTH_LONG
                )
                val view: View = snack2.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                view.layoutParams = params
                snack2.setBackgroundTint(Color.RED)
                snack2.show()
            }

        }

    }


    private fun initvalues() {
        withdrawText = findViewById(R.id.withdrawMethodText)
        appLogoImageView = findViewById(R.id.imageLogo)
        goBT = findViewById(R.id.Otp_Go)
        otpNo = findViewById(R.id.otpNo)
        timerTV = findViewById(R.id.OtpTimer)
        phoneNumber = findViewById(R.id.OtpNumber)
        resendOtpTV = findViewById(R.id.resendOTP)
        pinview = findViewById(R.id.pinview)
        progressBar = findViewById(R.id.progressbar2)
        countDownFun()
        callingActivity = intent.getStringExtra("calling_activity").toString()
        if (callingActivity == "UserWalletPaisa") {
            appLogoImageView.visibility = View.GONE
            withdrawText.visibility = View.VISIBLE
            retrofitSendOtp()
            userphone = session.getSession_usermobile()
            val subNumber = userphone!!.substring(6, userphone!!.length)
            phoneNumber.text = "XXXXXX" + subNumber
        } else {
            intentvalueget()
        }

    }

    private fun countDownFun() {
        object : CountDownTimer(31000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTV.text = (millisUntilFinished / 1000).toString() + " Second"
            }

            override fun onFinish() {
                timerTV.setTextColor(Color.RED)
                timerTV.text = "Time Out"
                resendOtpTV.visibility = View.VISIBLE
            }

        }.start()

    }

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }

    private fun retrofitresendotp() {
        myHideShowProgress(true)
        if (callingActivity == "UserWalletPaisa") {
            userphone = session.getSession_usermobile()
        }
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
                        otp = newotp
                        pinview.value = newotp

//                        otpNo.text=newotp
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

    private fun intentvalueget() {
        result = intent.getStringExtra("Login_Tag").toString()
        userphone = intent.getStringExtra("userphone").toString()
        otp = intent.getStringExtra("otp").toString()

        if (result.equals("SignupAct")) {
            username = intent.getStringExtra("username").toString()
            useremail = intent.getStringExtra("useremail").toString()
            userpassword = intent.getStringExtra("userpassword").toString()
//            usermpin = intent.getStringExtra("usermpin").toString()
            usermpin = "1234"
        }
        val subnumber = userphone!!.substring(6, userphone!!.length)
        phoneNumber.text = "XXXXXX" + subnumber
    }

    private fun retrofitSendOtp() {
        myHideShowProgress(true)
        if (callingActivity == "UserWalletPaisa") {
            userphone = session.getSession_usermobile()
        }

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
                        otp = newotp
//                        otpNo.text=newotp
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

    private fun gotoactivity() {
        if (result.equals("forgotPassAct")) {
            intent.putExtra("Result", "Success")
            setResult(RESULT_OK, intent)
            finish()
        } else if (result.equals("SignupAct")) {
            retrofitcallingLogin()
        } else {

        }
    }

    @SuppressLint("HardwareIds")
    private fun retrofitcallingLogin() {
        myHideShowProgress(true)
        val checkDeviceId = Settings.Secure.getString(
            applicationContext.contentResolver, Settings.Secure.ANDROID_ID
        )
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("mobile", userphone)
        jsonvalues.addProperty("password", userpassword)
        jsonvalues.addProperty("device_id", checkDeviceId)

        RetrofitClient.service.userlogin(jsonvalues).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>, response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    val msg = response.body()?.get("msg").toString().replace("\"", "")
                    val status = response.body()?.get("status").toString().replace("\"", "")
                    if (status.equals("true")) {
                        val username =
                            response.body()?.get("user_name").toString().replace("\"", "")
                        val userid =
                            response.body()?.get("unique_token").toString().replace("\"", "")
                        val mobile = response.body()?.get("mobile").toString().replace("\"", "")
                        val mobile_contact =
                            response.body()?.get("mobile_no").toString().replace("\"", "")

                        val notificationstatus =
                            response.body()?.get("notification_status").toString().replace("\"", "")

                        session.setNotification_Status(notificationstatus=="1")
                        session.setLogin(true)
                        session.setSession_userid(userid)
                        session.setSession_username(username)
                        session.setSession_usermobile(mobile)
                        session.setSession_Whatappnumber(mobile_contact)
                        val intent = Intent(this@OtpVerifyActivity, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
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

    private fun retrofitcalling_Signup() {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("name", username)
        jsonvalues.addProperty("email", useremail)
        jsonvalues.addProperty("mobile", userphone)
        jsonvalues.addProperty("password", userpassword)
        jsonvalues.addProperty("security_pin", usermpin)
        RetrofitClient.service.registeruser(jsonvalues).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>, response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    val msg = response.body()?.get("msg").toString().replace("\"", "")
                    val status = response.body()?.get("status").toString().replace("\"", "")
                    if (status.equals("true")) {
                        val snack2 = Snackbar.make(
                            findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG
                        )
                        val view: View = snack2.view
                        val params = view.layoutParams as FrameLayout.LayoutParams
                        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                        view.layoutParams = params
                        snack2.setBackgroundTint(Color.GREEN)
                        snack2.show()
                        gotoactivity()
                        myHideShowProgress(false)
                    } else {
                        val snack2 = Snackbar.make(
                            findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG
                        )
                        val view: View = snack2.view
                        val params = view.layoutParams as FrameLayout.LayoutParams
                        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                        view.layoutParams = params
                        snack2.setBackgroundTint(Color.RED)
                        snack2.show()
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

}