package com.smg0077.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.session.MySession
import com.google.gson.JsonObject
import com.smg0077.R
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient.env_type
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileDetails : AppCompatActivity() {
    lateinit var userFirstLetter: TextView
    lateinit var backbut: ImageView
    lateinit var progressBar: View
    lateinit var nameEd: EditText
    lateinit var emailEd: EditText
    lateinit var mobileEd: EditText
    lateinit var submitbut: Button
    lateinit var editBut: ImageView
    lateinit var session: MySession
    lateinit var app_key: AppKeyHolderClass
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        session = MySession(this)
        initvalues()
        retrofitcalling()

        editBut.setOnClickListener {
            nameEd.isEnabled = true
            emailEd.isEnabled = true
            submitbut.visibility = View.VISIBLE
        }
        submitbut.setOnClickListener(View.OnClickListener {
            val username = nameEd.text.toString()
            if (username.isEmpty()) {
                nameEd.setError("cannot be empty!", getDrawable(R.drawable.error_images))
                return@OnClickListener
            }

            val emailcheck =
                android.util.Patterns.EMAIL_ADDRESS.matcher(emailEd.text.toString()).matches()
            if (emailEd.text.isEmpty()) {
                nameEd.isEnabled = false
                emailEd.isEnabled = false
                submitbut.visibility = View.INVISIBLE
                UpdatedataRetrofitcalling()

            } else {
                if (emailcheck) {
                    nameEd.isEnabled = false
                    emailEd.isEnabled = false
                    submitbut.visibility = View.INVISIBLE
                    UpdatedataRetrofitcalling()
                } else
                    emailEd.error = "Invalid email format!"
            }

        })


        backbut.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initvalues() {
        userFirstLetter = findViewById(R.id.userFirstLetter)
        backbut = findViewById(R.id.userbackbut)
        progressBar = findViewById(R.id.progressbar2)
        nameEd = findViewById(R.id.userp_username)
        emailEd = findViewById(R.id.userp_emial)
        mobileEd = findViewById(R.id.userp_mobile)
        editBut = findViewById(R.id.userdetail_edit)
        submitbut = findViewById(R.id.user_submit_Button)
        val firstLetter = session.getSession_username().substring(0, 1)
        userFirstLetter.text = firstLetter

        nameEd.isEnabled = false
        emailEd.isEnabled = false
        mobileEd.isEnabled = false
        submitbut.visibility = View.INVISIBLE

    }

    private fun retrofitcalling() {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())

        RetrofitClient.service.getprofile(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val msg = response.body()?.get("msg").toString().replace("\"", "")
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")
                        response.body()?.get("profile").toString().replace("\"", "")
                        if (status.equals("true")) {
                            val jsonObject: JsonObject
                            val jsonArray = response.body()?.getAsJsonArray("profile")!!
                            jsonObject = jsonArray[0] as JsonObject

                            val username = jsonObject.get("user_name").toString().replace("\"", "")
                            val useremail = jsonObject.get("email").toString().replace("\"", "")
                            val mobile = jsonObject.get("mobile").toString().replace("\"", "")
                            nameEd.setText(username)
                            emailEd.setText(useremail)
                            mobileEd.setText(mobile)

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
                    Toast.makeText(
                        applicationContext,
                        "Unable to Connect to Internet",
                        Toast.LENGTH_LONG
                    ).show()
                    myHideShowProgress(false)

                }
            })
    }


    private fun UpdatedataRetrofitcalling() {
        myHideShowProgress(true)
        val updatename = nameEd.text
        val updateemail = emailEd.text
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("user_name", updatename.toString())
        jsonvalues.addProperty("email", updateemail.toString())

        RetrofitClient.service.updateprofiledata(jsonvalues).enqueue(
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
                            session.setSession_username(updatename.toString())
                            val firstLetter = session.getSession_username().substring(0, 1)
                            userFirstLetter.text = firstLetter

                            Toast.makeText(applicationContext, "" + msg, Toast.LENGTH_LONG).show()
                            retrofitcalling()
                            myHideShowProgress(false)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                msg,
                                Toast.LENGTH_LONG
                            ).show()
                            nameEd.isEnabled = true
                            emailEd.isEnabled = true
                            submitbut.visibility = View.VISIBLE
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