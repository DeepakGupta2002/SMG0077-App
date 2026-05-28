package com.smg0077.common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.session.MySession
import com.smg0077.all_adapter.ListAdapter
import com.google.gson.JsonObject
import com.smg0077.HomeActivity
import com.smg0077.R
import com.smg0077.bating.BatTimeActivity
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.transfer.PointAddViaQRActivity
import com.smg0077.transfer.PointWithdrawActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameRates : AppCompatActivity() {
    lateinit var session: MySession
    lateinit var backbut: ImageView
    lateinit var listView: ListView
    lateinit var progressBar: RelativeLayout
    lateinit var appKey: AppKeyHolderClass
    private lateinit var bidHistory: LinearLayout
    private lateinit var withdraw_point: LinearLayout
    private lateinit var menu_home: RelativeLayout
    lateinit var wanumberButton: LinearLayout
    lateinit var addpointButton: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_rate)
        session = MySession(applicationContext)
        initvalues()
        retrofitcalling()
        backbut.setOnClickListener {
            onBackPressed()
        }
        bidHistory.setOnClickListener {
            val intent = Intent(this, BatTimeActivity::class.java)
            intent.putExtra("history_bid", "home")
            startActivity(intent)
        }

        menu_home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        addpointButton.setOnClickListener {
            startActivity(Intent(this, PointAddViaQRActivity::class.java))
        }

        wanumberButton.setOnClickListener {
            startActivity(Intent(this, GameRates::class.java))
        }
        withdraw_point.setOnClickListener {
            startActivity(Intent(this, PointWithdrawActivity::class.java))
        }
    }

    private fun initvalues() {
        backbut = findViewById(R.id.userbackbut)
        listView = findViewById(R.id.my_game_listview)
        progressBar = findViewById(R.id.progressbar2)
        bidHistory = findViewById(R.id.bidHistory)
        menu_home = findViewById(R.id.menu_home)
        addpointButton = findViewById(R.id.home_add_point)
        wanumberButton = findViewById(R.id.home_whatappbut)
        withdraw_point = findViewById(R.id.withdraw_point)


    }

    private fun retrofitcalling() {
        myHideShowProgress(true)
        val jsonValues = JsonObject()
        jsonValues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonValues.addProperty("env_type", env_type)
        RetrofitClient.service.Maingamerates(jsonValues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                     if (response.isSuccessful) {
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")

                        if (status.equals("true")) {
                            val gameObject: JsonObject
                            val gamerateObject =
                                response.body()?.getAsJsonArray("game_rates")!!
                            gameObject = gamerateObject[0] as JsonObject
                            val singleVal = gameObject.get("single_digit_val_1").toString()
                                .replace("\"", "") + "-" + gameObject.get("single_digit_val_2")
                                .toString().replace("\"", "")
                            val jodiVal = gameObject.get("jodi_digit_val_1").toString()
                                .replace("\"", "") + "-" + gameObject.get("jodi_digit_val_2")
                                .toString().replace("\"", "")
                            val singlePanaVal = gameObject.get("single_pana_val_1").toString()
                                .replace("\"", "") + "-" + gameObject.get("single_pana_val_2")
                                .toString().replace("\"", "")
                            val doubleVal = gameObject.get("double_pana_val_1").toString()
                                .replace("\"", "") + "-" + gameObject.get("double_pana_val_2")
                                .toString().replace("\"", "")
                            val trippleVal = gameObject.get("tripple_pana_val_1").toString()
                                .replace("\"", "") + "-" + gameObject.get("tripple_pana_val_2")
                                .toString().replace("\"", "")
                            val halfVal = gameObject.get("half_sangam_val_1").toString()
                                .replace("\"", "") + "-" + gameObject.get("half_sangam_val_2")
                                .toString().replace("\"", "")
                            val fullVal = gameObject.get("full_sangam_val_1").toString()
                                .replace("\"", "") + "-" + gameObject.get("full_sangam_val_2")
                                .toString().replace("\"", "")

                            val gameNamelist = arrayOf(
                                "Single Digit",
                                "Jodi Digit",
                                "Single Panna",
                                "Double Panna",
                                "Tripple Panna",
                                "Half Sangam",
                                "Full Sangam"
                            )
                            val gamevallist = arrayOf(
                                singleVal,
                                jodiVal,
                                singlePanaVal,
                                doubleVal,
                                trippleVal,
                                halfVal,
                                fullVal
                            )
                            listView.adapter =
                                ListAdapter(applicationContext, gameNamelist, gamevallist)
                            myHideShowProgress(false)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Invalid User!",
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
}