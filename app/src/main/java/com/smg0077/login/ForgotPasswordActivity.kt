package com.smg0077.login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.goodiebag.pinview.Pinview
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.smg0077.R
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var passwordEd: EditText
    private lateinit var backButton: ImageView
    private lateinit var phoneNumberEd: EditText
    private lateinit var progressBar: View
    private lateinit var conPasswordEd: EditText
    private var checkCondition = 0
    private var otpMain: String = "none"

    private lateinit var forgotPasswordBT: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        initvalues()

        backButton.setOnClickListener {
            onBackPressed()
        }

        forgotPasswordBT.setOnClickListener {
            if (checkCondition == 0) {

                if (validatePhoneNumber()) {
                    retrofitresendotp("2")
                } else {
                    phoneNumberEd.error = "Invalid Number"
                }

            } else if (checkCondition == 1) {
                if (validatepass()) {
                    RetrofitChangepassword()
                }
            }
        }

    }

    private fun validatepass(): Boolean {
        var valid = false
        val pass = passwordEd.text.toString().trim()
        val conpass = conPasswordEd.text.toString().trim()
        if (pass.isEmpty()) {
            passwordEd.error = "empty!"
        }
        if (conpass.isEmpty()) {
            conPasswordEd.error = "empty!"
        }

        if (!pass.isEmpty() && !conpass.isEmpty()) {
            if (pass.length > 5) {
                if (pass.equals(conpass)) {
                    valid = true
                } else {
                    conPasswordEd.error = "Password does not match!"
                }

            } else {
                passwordEd.error = "should be more than 5 character!"
            }
        }
        return valid
    }

    private fun initvalues() {
        forgotPasswordBT = findViewById(R.id.forgotPasswordBT)
        backButton = findViewById(R.id.backButton)
        phoneNumberEd = findViewById(R.id.ForgotPhone)
        passwordEd = findViewById(R.id.ForgotNewPassword)
        conPasswordEd = findViewById(R.id.ForgotConfirmPassword)
        progressBar = findViewById(R.id.progressbar2)
        passwordEd.visibility = View.GONE
        conPasswordEd.visibility = View.GONE
    }

    private fun validatePhoneNumber(): Boolean {
        val phoneStr = phoneNumberEd.text.toString()
        if (phoneStr.length <= 9) {
            return false
        }
        return true
    }

    private fun RetrofitChangepassword() {
        myHideShowProgress(true)
        val passwordis = passwordEd.text.toString()
        val phoneNumber = phoneNumberEd.text.toString()
        val jsonValues = JsonObject()
        jsonValues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonValues.addProperty("env_type", env_type)
        jsonValues.addProperty("mobile", phoneNumber)
        jsonValues.addProperty("new_pass", passwordis)
        RetrofitClient.service.changeForgotPassword(jsonValues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val responsevalues = response
                        val status =
                            responsevalues.body()?.get("status").toString().replace("\"", "")
                        val msg = responsevalues.body()?.get("msg").toString().replace("\"", "")
                        if (status.equals("true")) {
                            startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
                            finishAffinity()
                            onBackPressed()
                            Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
                            checkCondition = 0
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

    private fun retrofitresendotp(s: String) {
        myHideShowProgress(true)
        val userphone = phoneNumberEd.text.toString()
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("mobile", userphone)
        RetrofitClient.service.ValideNumberForgotPassword(jsonvalues).enqueue(
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
                            if (s == "1") {

                            } else {
                                showBottomSheet()
                            }
                            myHideShowProgress(false)
                        } else {
                            Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
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

    fun showBottomSheet() {
        val bottomSheet = BottomSheetDialog(this)
        bottomSheet.setContentView(R.layout.bottom_sheet_layout)
        bottomSheet.dismissWithAnimation = true
        bottomSheet.setCanceledOnTouchOutside(false)
        val goBT: Button? = bottomSheet.findViewById(R.id.Otp_Go)
        val timerTV: TextView? = bottomSheet.findViewById(R.id.OtpTimer)
        val phoneNumber: TextView? = bottomSheet.findViewById(R.id.OtpNumber)
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

        val phoneStr = phoneNumberEd.text.toString()
        var Invalid_Pin_Counter: Int = 5
        val subNumber = phoneStr!!.substring(6, phoneStr!!.length)
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

            retrofitresendotp("1")
        }

        pinview?.setPinViewEventListener { pinview, _ ->
            val enteredOtp = pinview.value.toString()
            if (enteredOtp == otpMain) {
                passwordEd.visibility = View.VISIBLE
                conPasswordEd.visibility = View.VISIBLE
                checkCondition = 1
                phoneNumberEd.isEnabled = false
                bottomSheet.dismiss()

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
                passwordEd.visibility = View.VISIBLE
                conPasswordEd.visibility = View.VISIBLE
                checkCondition = 1
                phoneNumberEd.isEnabled = false
                bottomSheet.dismiss()

            } else {
                val snack2 = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Invalid OTP!",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snack2.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER
                view.layoutParams = params
                snack2.setBackgroundTint(Color.RED)
                snack2.show()
            }

        }

        if (bottomSheet.isShowing) {
        } else {
            bottomSheet.show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                val response_status = data?.getStringExtra("Result").toString()
                if (response_status == "Success") {

                    passwordEd.visibility = View.VISIBLE
                    conPasswordEd.visibility = View.VISIBLE
                    checkCondition = 1
                    phoneNumberEd.isEnabled = false
                    phoneNumberEd.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_check_24,
                        0
                    )


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
                snak.setBackgroundTint(Color.RED)
                snak.show()
                Handler().postDelayed({}, 200)
            }

        }
    }
}