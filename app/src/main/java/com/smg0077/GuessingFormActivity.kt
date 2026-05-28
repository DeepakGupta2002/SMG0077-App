package com.smg0077

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.JsonObject
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuessingFormActivity : AppCompatActivity() {

    private lateinit var bannerSlider: ImageSlider
    private lateinit var progress: RelativeLayout
    private lateinit var emptyText: TextView
    private val banners = ArrayList<SlideModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guessing_form)

        bannerSlider = findViewById(R.id.guessingBannerSlider)
        progress = findViewById(R.id.guessingProgress)
        emptyText = findViewById(R.id.guessingEmptyText)

        findViewById<ImageView>(R.id.guessingBack).setOnClickListener {
            finish()
        }

        loadBanners()
    }

    private fun loadBanners() {
        banners.clear()
        progress.visibility = View.VISIBLE
        emptyText.visibility = View.GONE

        val jsonValues = JsonObject()
        jsonValues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonValues.addProperty("env_type", env_type)

        RetrofitClient.service.BannerApi(jsonValues).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                progress.visibility = View.GONE
                if (response.isSuccessful) {
                    val status = response.body()?.get("status").toString().replace("\"", "")
                    if (status == "true") {
                        val bannerArray = response.body()?.getAsJsonArray("banner")
                        if (bannerArray != null) {
                            for (i in 0 until bannerArray.size()) {
                                val bannerObject = bannerArray[i] as JsonObject
                                val bannerUrl = bannerObject.get("banner").toString().replace("\"", "")
                                banners.add(SlideModel(bannerUrl, ScaleTypes.FIT))
                            }
                        }
                        if (banners.isNotEmpty()) {
                            bannerSlider.setImageList(banners)
                        } else {
                            emptyText.visibility = View.VISIBLE
                        }
                    } else {
                        emptyText.visibility = View.VISIBLE
                    }
                } else {
                    emptyText.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                progress.visibility = View.GONE
                emptyText.visibility = View.VISIBLE
                Toast.makeText(applicationContext, "Unable to Connect to Internet", Toast.LENGTH_LONG).show()
            }
        })
    }
}