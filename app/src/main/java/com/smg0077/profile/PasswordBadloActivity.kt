package com.smg0077.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.session.MySession
import com.google.gson.JsonObject
import com.smg0077.R
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient.env_type
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordBadloActivity : AppCompatActivity() {
    lateinit var session: MySession
    lateinit var backbut: ImageView
    lateinit var progressBar: View
    lateinit var oldpass: ShowHidePasswordEditText
    lateinit var newpass: ShowHidePasswordEditText
    lateinit var confnewpass: ShowHidePasswordEditText
    lateinit var submitButton: Button
    lateinit var app_key: AppKeyHolderClass
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        initvalues()
        backbut.setOnClickListener {
            onBackPressed()
        }

        submitButton.setOnClickListener {
            if (validate()) {
                UpdatedataRetrofitcalling()
            }
        }
    }

    private fun initvalues() {
        session = MySession(applicationContext)
        backbut = findViewById(R.id.userbackbut)
        progressBar = findViewById(R.id.progressbar2)
        oldpass = findViewById(R.id.oldpassword)
        newpass = findViewById(R.id.newpassword)
        confnewpass = findViewById(R.id.confirmnewpassword)
        submitButton = findViewById(R.id.user_submit_Button)
    }

    private fun UpdatedataRetrofitcalling() {
        myHideShowProgress(true)
        val updateoldpass = oldpass.text.toString()
        val updatenewpass = newpass.text.toString()

        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("old_pass", updateoldpass)
        jsonvalues.addProperty("new_pass", updatenewpass)
        jsonvalues.addProperty("unique_token", session.getSession_userid())

        RetrofitClient.service.updatepasswordApi(jsonvalues).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>, response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    val msg = response.body()?.get("msg").toString().replace("\"", "")
                    val status = response.body()?.get("status").toString().replace("\"", "")

                    if (status.equals("true")) {
                        Toast.makeText(applicationContext, "" + msg, Toast.LENGTH_LONG).show()
                        oldpass.text.clear()
                        newpass.text.clear()
                        confnewpass.text.clear()
                        oldpass.requestFocus()
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

    private fun validate(): Boolean {
        val oldpassstr = oldpass.text.toString()
        val newpassstr = newpass.text.toString()
        val confpassstr = confnewpass.text.toString()
        var checkstatus = true

        if (oldpassstr.isEmpty() && newpassstr.isEmpty() && confpassstr.isEmpty()) {
            oldpass.error = "Can't be Empty!"
            newpass.error = "Can't be Empty!"
            confnewpass.error = "Can't be Empty!"
            checkstatus = false
        } else {
            if (oldpassstr.isEmpty()) {
                oldpass.error = "Can't be Empty!"
                checkstatus = false
            }
            if (newpassstr.isEmpty()) {
                newpass.error = "Can't be Empty!"
                checkstatus = false

            }
            if (confpassstr.isEmpty()) {
                confnewpass.error = "Can't be Empty!"
                checkstatus = false
            }

            if (!newpassstr.equals(confpassstr)) {
                confnewpass.error = "Password does not match!"
                checkstatus = false
            } else {
                if (newpassstr.length < 6) {
                    newpass.error = "should be more the 6 character"
                    checkstatus = false

                }

            }
            if (oldpassstr.equals(newpassstr)) {
                newpass.error = "Can't be same as old password"
                checkstatus = false
            }

        }

        return checkstatus
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