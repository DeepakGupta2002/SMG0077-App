package com.smg0077

import `in`.arjsna.passcodeview.PassCodeView
import android.annotation.SuppressLint
import android.graphics.Color
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.smg0077.session.MySession
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.smg0077.login.LoginActivity
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("DEPRECATION")
class PinVerificationActivity : AppCompatActivity() {
    private lateinit var passCodeView: PassCodeView
    private lateinit var username: String
    private lateinit var session: MySession
    private lateinit var mobile: String
    private var settingCheck = true
    private lateinit var message: String
    private lateinit var Calling_Activity_Name: String
    private lateinit var whatsapp_mobilenumber: String
    private lateinit var userid: String
    private var invalidPinCounter: Int = 5
    private lateinit var wanumberTV: TextView
    private lateinit var userenterbox: TextView
    private lateinit var forgotpinlayout: LinearLayout
    private lateinit var progressBar: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_pin)
        initValues()
        retrofitSecurityPinCheck("1234")

        passCodeView.setOnTextChangeListener { text ->
            if (text?.length == 4) {
                invalidPinCounter -= 1
                if (invalidPinCounter != 0) {
                    retrofitSecurityPinCheck("1234")
                } else {
                    displayMessageActivity()
                }
            }
        }

        forgotpinlayout.setOnClickListener {
            whatsAppFunction()
        }

    }

    private fun initValues() {
        progressBar = findViewById(R.id.progressbar2)
        passCodeView = findViewById(R.id.pass_code_view)
        passCodeView.setKeyTextColor(resources.getColor(R.color.black))
        session = MySession(this)
        userenterbox = findViewById(R.id.security_enterpintext)
        forgotpinlayout = findViewById(R.id.Security_forgetpin)
        wanumberTV = findViewById(R.id.security_WAnumber)
        Calling_Activity_Name = intent.getStringExtra("calling_activity").toString()
        if (Calling_Activity_Name == "HomePage" || Calling_Activity_Name == "pointWithdrawMoney" || Calling_Activity_Name == "UserWalletPaisa") {
            whatsapp_mobilenumber = session.getSession_Whatappnumber()
            wanumberTV.text = whatsapp_mobilenumber
            userid = session.getSession_userid()
            username=session.getSession_username();
            mobile = session.getSession_usermobile()
            message = "success"

        } else {
            username = intent.getStringExtra("user_name").toString()
            userid = intent.getStringExtra("unique_token").toString()
            mobile = intent.getStringExtra("mobile").toString()
            message = intent.getStringExtra("msg").toString()
            whatsapp_mobilenumber = intent.getStringExtra("mobile_contact").toString()
            wanumberTV.text = whatsapp_mobilenumber
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

    @SuppressLint("QueryPermissionsNeeded")
    private fun whatsAppFunction() {
        val uri = Uri.parse("smsto:$whatsapp_mobilenumber")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.setPackage("com.whatsapp")
        if (intent.resolveActivity(packageManager) == null) {
            val uri1 = Uri.parse("smsto:$whatsapp_mobilenumber")
            val intent1 = Intent(Intent.ACTION_SENDTO, uri1)
            intent1.setPackage("com.whatsapp.w4b")
            if (intent.resolveActivity(packageManager) == null) {
                val url = "https://api.whatsapp.com/send?phone=$whatsapp_mobilenumber"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            } else {
                startActivity(intent1)
            }
        } else {
            startActivity(Intent.createChooser(intent, ""))
        }

    }


    private fun finalCall() {
        if (settingCheck) {
            settingCheck = false
            when (Calling_Activity_Name) {
                "UserWalletPaisa" -> {
                    val intent = Intent()
                    intent.putExtra("Result", "Success")
                    setResult(RESULT_OK, intent)
                    finish()
                }
                "pointWithdrawMoney" -> {
                    val intent = Intent()
                    intent.putExtra("Result", "Success")
                    setResult(RESULT_OK, intent)
                    finish()
                }
                else -> {
                    session.setLogin(true)
                    session.setSession_userid(userid)
                    session.setSession_username(username)
                    session.setSession_usermobile(mobile)
                    session.setSession_Whatappnumber(whatsapp_mobilenumber)
                    val intent = Intent(this@PinVerificationActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }

    }


    fun displayMessageActivity() {
        val snack2 = Snackbar.make(
            findViewById(android.R.id.content),
            "Sorry, you have exceeded the maximum number of attempts",
            Snackbar.LENGTH_LONG
        )
        val view: View = snack2.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
        view.layoutParams = params
        snack2.setBackgroundTint(Color.RED)
        snack2.show()
        Handler().postDelayed({
            if (Calling_Activity_Name == "pointWithdrawMoney") {
                onBackPressed()
            } else {
                val intent = Intent(this@PinVerificationActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                onBackPressed()
            }
        }, 1000)

    }

    private fun retrofitSecurityPinCheck(pinEntered: String) {
        myHideShowProgress(true)
        val jsonValues = JsonObject()
        jsonValues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonValues.addProperty("env_type", RetrofitClient.env_type)
        jsonValues.addProperty("unique_token", userid)
        jsonValues.addProperty("security_pin", pinEntered)

       //  println("+++++Status "+ pinEntered)
       //  println("+++++AppKey "+ AppKeyHolderClass().getAppKey())
       //  println("+++++userid "+ userid)
       //  println("+++++env_type "+ RetrofitClient.env_type)

println("++++"+ AppKeyHolderClass().getAppKey()+ "    "+ RetrofitClient.env_type+ "   "+ userid)

        RetrofitClient.service.securityPinCheck(jsonValues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    Log.d("response", response.message())
                    if (response.isSuccessful) {

                        val url = call.request().url
                        Log.d("TAG", "URL: $url")

                        val msg = response.body()?.get("msg").toString().replace("\"", "")
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")

                       //  println("+++++Status "+ status)
                        if (status == "true") {
                           // Log.d("pin match", "true")
                            finalCall()
                            myHideShowProgress(false)
                        } else {
                            if (invalidPinCounter > 0) {
                                val snack2 = Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "$msg ! \nYou have $invalidPinCounter attempt left",
                                    Snackbar.LENGTH_LONG
                                )
                                val view: View = snack2.view
                                val params = view.layoutParams as FrameLayout.LayoutParams
                                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                view.layoutParams = params
                                snack2.setBackgroundTint(Color.RED)
                                snack2.show()
                                vibrate()
                                passCodeView.setPassCode("")
                            }
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