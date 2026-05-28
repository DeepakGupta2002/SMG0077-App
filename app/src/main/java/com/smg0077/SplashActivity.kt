package com.smg0077

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.smg0077.login.SignUpActivity
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.session.MySession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.primarydark)
        setContentView(R.layout.activity_splash)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val setKey = AppKeyHolderClass()
            setKey.setAppKey("SgRTXsywVmWteEBLlYWecwgbDiHwlh")
            if (MySession(applicationContext).getLogin()) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("calling_activity", "HomePage")
                this.startActivityForResult(intent, 120)
                finish()
            } else {
                val i = Intent(applicationContext, SignUpActivity::class.java)
                startActivity(i)
                finish()
            }

        }, 2500)
        // retrofitGetAppKey()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 120) {
            if (resultCode == RESULT_OK) {
                val response_status = data?.getStringExtra("Result").toString()
                if (response_status.equals("Success")) {

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

    private fun retrofitGetAppKey() {
        val jsonValues = JsonObject()
        jsonValues.addProperty("env_type", env_type)
        RetrofitClient.service.AppKeyget(jsonValues).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val status = response.body()?.get("status")?.asBoolean
                    val msg = response.body()?.get("msg")?.asString
                    if (status!!) {
                        val appKey = response.body()?.get("app_key")!!.asString
                        System.out.println(appKey)

                        Encrypt().cryptop(appKey)

                    } else {
                        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
                    }
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "Unable to Connect to Internet", Toast.LENGTH_LONG).show()
            }
        })
    }

    private inner class Encrypt {
        fun cryptop(args: String) {
            val k = -3
            encode(args, k)
        }

        fun encode(str: String, k: Int): String {
            var encodedString = ""
            var nothingencoded = ""

            for (i in 0 until 15) {
                val char = str[i]
                val charCode = char.toInt()
                val newChar2 = charCode.toChar()
                nothingencoded += newChar2
            }

            for (i in 15 until str.length) {
                val char = str[i]
                val charCode = char.toInt()
                val newCharCode = charCode + k
                val newChar = newCharCode.toChar()

                if (newCharCode > 122) {
                    val newCharCode2 = newCharCode + 26
                    val newChar2 = newCharCode2.toChar()
                    encodedString += newChar2
                }
                else if (newCharCode in 91..96) {
                    val newCharCode2 = newCharCode + 26
                    val newChar2 = newCharCode2.toChar()
                    encodedString += newChar2
                } else if (newCharCode < 65) {
                    val newCharCode2 = newCharCode + 26
                    val newChar2 = newCharCode2.toChar()
                    encodedString += newChar2
                } else {
                    encodedString += newChar
                }
            }
            val setKey = AppKeyHolderClass()
            setKey.setAppKey(nothingencoded + encodedString)
            return nothingencoded + encodedString
        }
    }
}