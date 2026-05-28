package com.smg0077.common

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.*
import com.google.gson.JsonObject
import com.smg0077.R
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import retrofit2.*

class HowtoplayActivity : AppCompatActivity() {
    private lateinit var youtubeBut: Button
    private lateinit var backBut: ImageView
    private lateinit var serverText: TextView
    private var youtubeLink: String = "empty"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_how_to_play)
        youtubeBut = findViewById(R.id.youtubeBut)
        serverText = findViewById(R.id.serverText)
        backBut = findViewById(R.id.backBut)

        retrofitGetHowToPlayDetails()
        youtubeBut.setOnClickListener {
            watchYoutubeVideo()
        }

        backBut.setOnClickListener {
            onBackPressed()
        }
    }

    private fun retrofitGetHowToPlayDetails() {
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        RetrofitClient.service.apiHowToPlay(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")
                        val msg = response.body()?.get("msg").toString().replace("\"", "")
                        val contentArray = response.body()?.getAsJsonArray("content")

                        if (status == "true") {
                            val jsonObject: JsonObject
                            if (contentArray != null) {
                                jsonObject = contentArray.elementAt(0).asJsonObject
                                val contentText = jsonObject.get("how_to_play_content").asString
                                val youtubeLinkServer = jsonObject.get("video_link").asString
                                serverText.text = Html.fromHtml(contentText)
                                youtubeLink = youtubeLinkServer
                            }
                        } else {
                            Toast.makeText(this@HowtoplayActivity, msg, Toast.LENGTH_SHORT).show()
                        }

                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Unable to Connect to Internet",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

    }

    private fun watchYoutubeVideo() {
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(youtubeLink)
        )
        try {
            startActivity(webIntent)
        } catch (ex: ActivityNotFoundException) {
           //  println(ex.printStackTrace())
        }
    }
}