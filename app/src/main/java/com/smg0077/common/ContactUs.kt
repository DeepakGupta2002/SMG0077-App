package com.smg0077.common

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.session.MySession
import com.google.gson.JsonObject
import com.smg0077.AppDelegate
import com.smg0077.R
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient.env_type
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUs : AppCompatActivity() {
    private lateinit var whatsAppNumberMain: CardView
    private lateinit var emailIdMain: CardView
    private lateinit var callNo1Main: CardView
    private lateinit var callNo2Main: CardView
    private lateinit var callNo2Layer: RelativeLayout
    lateinit var backBtn: ImageView
    lateinit var whatsAppNo: TextView
    lateinit var emailid: TextView
    lateinit var callnumber1: TextView
    lateinit var callnumber2Label: TextView
    lateinit var progressBar: RelativeLayout
    lateinit var callnumber2: TextView
    lateinit var session: MySession
    lateinit var appKey: AppKeyHolderClass
    var whatsAppMobileNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        session = MySession(applicationContext)
        initvalues()
        retrofitCheckNumber()
        backBtn.setOnClickListener {
            onBackPressed()
        }

        whatsAppNumberMain.setOnClickListener {
            whataappfunction()
        }
        emailIdMain.setOnClickListener {
            sendEmail(
                emailid.text.toString(),
                "Testing Subject",
                "Download my application from play store "
            )
        }
        callNo2Main.setOnClickListener {
            val supportValue = callnumber2.text.toString()
            if (supportValue.startsWith("http://", true) || supportValue.startsWith("https://", true)) {
                AppDelegate.openWebPage(this, supportValue)
            } else {
                callFuntion(supportValue)
            }

        }
        callNo1Main.setOnClickListener {
            callFuntion(callnumber1.text.toString())
        }
    }

    private fun initvalues() {
        backBtn = findViewById(R.id.userbackbut)
        whatsAppNo = findViewById(R.id.contact_whatappno)
        emailid = findViewById(R.id.contact_emailid)
        callnumber1 = findViewById(R.id.contact_callno1)
        callnumber2 = findViewById(R.id.contact_callno2)
        callnumber2Label = findViewById(R.id.contact_callno2label)
        progressBar = findViewById(R.id.progressbar2)
        whatsAppNumberMain = findViewById(R.id.contact_whatsappmain)
        emailIdMain = findViewById(R.id.contact_emailidmain)
        callNo1Main = findViewById(R.id.contact_callno1main)
        callNo2Main = findViewById(R.id.contact_callno2main)
        callNo2Layer = findViewById(R.id.contact_callno2layer)

    }

    private fun retrofitCheckNumber() {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)

        RetrofitClient.service.ContactusAPI(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")
                        response.body()?.get("msg").toString().replace("\"", "")
                        if (status.equals("true")) {

                            val responseBody = response.body()
                            val mobile1 = getContactValue(responseBody, "mobile_1")
                            val mobile2 = getContactValue(responseBody, "mobile_2")
                            val email = getContactValue(responseBody, "email_1")
                            val whatappno = getContactValue(responseBody, "whatsapp_no")

                            callnumber1.text = mobile1
                            callnumber2.text = mobile2
                            callnumber2Label.text = if (mobile2.startsWith("http", true)) "Support:" else "Call Us:"
                            callNo2Layer.visibility = if (mobile2.isEmpty()) View.GONE else View.VISIBLE
                            emailid.text = email
                            whatsAppNo.text = whatappno
                            whatsAppMobileNumber = whatappno
                            session.setSession_Whatappnumber(whatappno)

                            myHideShowProgress(false)
                        } else {
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

    private fun getContactValue(responseBody: JsonObject?, key: String): String {
        val value = responseBody?.get(key)
        if (value == null || value.isJsonNull) {
            return ""
        }
        return value.asString.trim()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun whataappfunction() {
        AppDelegate.openWhatsapp(whatsAppMobileNumber, this)
    }


    private fun callFuntion(Phonenumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$Phonenumber")
        startActivity(intent)
    }

    private fun sendEmail(recipient: String, subject: String, message: String) {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }
}
