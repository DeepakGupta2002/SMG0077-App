package com.smg0077.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.smg0077.HomeActivity
import com.smg0077.R
import com.smg0077.retrofit.AppKeyHolderClass
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText
import com.smg0077.AppDelegate
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.session.MySession
import kotlinx.android.synthetic.main.activity_login.backbutton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    var intentWhatsNumber: String = "null"
    var intentTelegramNumber: String = "null"
    private lateinit var loginBT: Button
    private lateinit var forgotPasswordTV: TextView
    private lateinit var whatsAppNumber: TextView
    private lateinit var callNumber: TextView
    private lateinit var telegramNumber: TextView
    private lateinit var whatsappLinear: LinearLayout
    private lateinit var whatsappnew: ImageView
    private lateinit var callLinear: LinearLayout
    private lateinit var telegramLinear: LinearLayout
    private lateinit var session: MySession

    private lateinit var signUpTV: TextView
    lateinit var appkey: AppKeyHolderClass
    lateinit var progressBar: View
    private lateinit var phoneET: EditText
    lateinit var passwordET: ShowHidePasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initValues()
        passwordET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {  }
        })
        signUpTV.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        forgotPasswordTV.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
        loginBT.setOnClickListener {
            AppDelegate.hideSoftKeyboard(this)
            if (validate()) {
                retrofitCalling()
            }
        }

        backbutton.setOnClickListener {
            finishAffinity()
        }
    }

    private fun initValues() {
        whatsAppNumber = findViewById(R.id.whatsAppNumber)
        callNumber = findViewById(R.id.callNumber)
        telegramNumber = findViewById(R.id.telegramNumber)
        whatsappLinear = findViewById(R.id.whatsLinear)
        whatsappnew = findViewById(R.id.whatsappnew)
        callLinear = findViewById(R.id.callLinear)
        telegramLinear = findViewById(R.id.telegramLinear)
        signUpTV = findViewById(R.id.loginSignUpTV)
        loginBT = findViewById(R.id.loginLoginBT)
        forgotPasswordTV = findViewById(R.id.loginForgotPassword)
        progressBar = findViewById(R.id.progressbar2)
        phoneET = findViewById(R.id.login_user_phone)
        passwordET = findViewById(R.id.loginUserPassword)

        whatsAppNumber.text = intentWhatsNumber
        callNumber.text = intentWhatsNumber
        telegramNumber.text = intentTelegramNumber
        retrofitGetContactPhoneNumbers()

        whatsappLinear.setOnClickListener {
            AppDelegate.openWhatsapp(intentWhatsNumber, this)
        }

        whatsappnew.setOnClickListener {
            AppDelegate.openWhatsapp(intentWhatsNumber, this@LoginActivity)
        }


        callLinear.setOnClickListener {
            val whatAppNo = callNumber.text.toString()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$whatAppNo")
            startActivity(intent)
        }
        telegramLinear.setOnClickListener {
            AppDelegate.openTelegramLink(this, telegramNumber.text.toString())
        }

    }

    private fun telegramIntent(context: Context): Intent {
        val intent: Intent?
        val telephoneNo = telegramNumber.text.toString()
        intent = try {
            try {
                context.packageManager.getPackageInfo("org.telegram.messenger", 0)
            } catch (e: Exception) {
                context.packageManager.getPackageInfo("org.thunderdog.challegram", 0)
            }
            Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=${telephoneNo}"))
        } catch (e: Exception) {
            Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/$telephoneNo"))
        }
        return intent!!
    }

    private fun myHideShowProgress(check: Boolean) {
        if (check) {
            progressBar.visibility = View.VISIBLE
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            progressBar.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

    }

    private fun mobileCheck(): Boolean {
        if (phoneET.text.isEmpty()) {
            phoneET.error = "Enter Number"
            return false
        } else if (phoneET.text.length <= 9) {
            phoneET.error = "Enter Valid Phone Number"
            return false
        }
        return true
    }

    private fun validate(): Boolean {
        var chck = true
        if (!mobileCheck())
            chck = false
        if (passwordET.text.isEmpty()) {
            passwordET.error = "Enter Password"
            chck = false
        } else if (passwordET.text.length <= 5) {
            passwordET.error = "Should be more then 6 character"
            chck = false
        }
        return chck
    }

    @SuppressLint("HardwareIds")
    private fun retrofitCalling() {
        val deviceId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        val appkey = AppKeyHolderClass()
        appkey.getAppKey()
        myHideShowProgress(true)
        val mobile = phoneET.text.toString().trim()
        val pass = passwordET.text.toString().trim()
        val jsonValues = JsonObject()
        jsonValues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonValues.addProperty("env_type", env_type)
        jsonValues.addProperty("mobile", mobile)
        jsonValues.addProperty("password", pass)
        jsonValues.addProperty("device_id", deviceId)

        println("+++++"+ AppKeyHolderClass().getAppKey())
        println("+++++1"+ env_type)
        println("+++++2"+ mobile)
        println("+++++3"+ pass)
        println("+++++4"+ deviceId)

        RetrofitClient.service.userlogin(jsonValues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val msg = response.body()?.get("msg").toString().replace("\"", "")

                        println("+++++++l "+ Gson().toJson(response.body()))
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")
                        if (status.equals("true")) {
                            val username =
                                response.body()?.get("user_name").toString().replace("\"", "")
                            val userid = response.body()?.get("unique_token").toString()
                                .replace("\"", "")
                            val mobileNum =
                                response.body()?.get("mobile").toString().replace("\"", "")
                            val mobileContact =
                                response.body()?.get("mobile_no").toString().replace("\"", "")
                            response.body()?.get("security_pin").toString().replace("\"", "")

                            val notificationstatus =
                                response.body()?.get("notification_status").toString().replace("\"", "")

                            println("+++++ "+ notificationstatus)

                            session = MySession(this@LoginActivity)
                            session.setLogin(true)
                            session.setSession_userid(userid)
                            session.setNotification_Status(notificationstatus=="1")
                            session.setSession_username(username)
                            session.setSession_usermobile(mobile)
                            session.setSession_Whatappnumber(mobileContact)
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)


//                            val intent = Intent(this@LoginActivity, PinVerificationActivity::class.java)
//
//                            intent.putExtra("user_name", username)
//                            intent.putExtra("unique_token", userid)
//                            intent.putExtra("mobile", mobileNum)
//                            intent.putExtra("msg", msg)
//                            intent.putExtra("mobile_contact", mobileContact)
//                            intent.putExtra("calling_activity", "login")
//                            startActivity(intent)
//                            onBackPressed()

                            myHideShowProgress(false)

                        } else {
                            val vibrateservice = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            vibrateservice.vibrate(200)
                            Toast.makeText(this@LoginActivity,msg,Toast.LENGTH_LONG).show()
                            myHideShowProgress(false)
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Unable to Connect to Internet", Toast.LENGTH_LONG)
                        .show()
                    myHideShowProgress(false)
                }
            })
    }

    private fun retrofitGetContactPhoneNumbers() {
        val jsonValues = JsonObject()
        jsonValues.addProperty("env_type", env_type)
        jsonValues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        RetrofitClient.service.socialDataGet(jsonValues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val status = response.body()?.get("status")?.asBoolean
                        if (status!!) {

                            val whatsResponse = response.body()?.get("mobile_no")!!.asString
                            val telegramResponse = response.body()?.get("telegram_no")!!.asString
                            intentWhatsNumber = whatsResponse
                            intentTelegramNumber = telegramResponse

                            whatsAppNumber.text = intentWhatsNumber
                            callNumber.text = intentWhatsNumber
                            telegramNumber.text = intentTelegramNumber


                        } else {
                            Toast.makeText(applicationContext,
                                "No contact detail found",
                                Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(applicationContext,
                        "Unable to Connect to Internet",
                        Toast.LENGTH_LONG).show()
                }
            })
    }

}
