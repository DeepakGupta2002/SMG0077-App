package com.smg0077.realtask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.JsonObject
import com.smg0077.all_adapter.StarlineGameAdapter
import com.smg0077.bating.BatTimeActivity
import com.smg0077.model.StarlineGamedataholder
import com.smg0077.web.MyTaskViewWebView
import com.smg0077.R
import com.smg0077.bating.ToperHistoryActivity
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.session.MySession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GalidesawarGameActivity : AppCompatActivity() {
    lateinit var session: MySession
    lateinit var swipetorefresh: SwipeRefreshLayout
    lateinit var progressBar: View
    lateinit var app_key: AppKeyHolderClass
    lateinit var recyclerView: RecyclerView
    lateinit var getsingeldigitvalue: TextView
    lateinit var getsingelpanavalue: TextView
    lateinit var getdoublepanavalue: TextView
    lateinit var gettripplepanavalue: TextView
    lateinit var startline_chartIV: ImageView
    lateinit var userbackBUT: ImageView
    lateinit var bidhistoryBUT: Button
    lateinit var winhistoryBUT: Button
    var my_char_url: String = ""
    var Adapterarrylist: ArrayList<StarlineGamedataholder> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starline_game)
        initvalues()
        swipetorefresh.setOnRefreshListener {
            retrofitgamedata()
            retrofitgameratesdata()
            swipetorefresh.isRefreshing = false
        }
        userbackBUT.setOnClickListener {
            onBackPressed()
        }
        bidhistoryBUT.setOnClickListener {
            val intent = Intent(this, BatTimeActivity::class.java)
            intent.putExtra("history_bid", "starline")
            startActivity(intent)
        }

        winhistoryBUT.setOnClickListener {
            val intent = Intent(this, ToperHistoryActivity::class.java)
            intent.putExtra("history_win", "starline")
            startActivity(intent)
        }

        startline_chartIV.setOnClickListener {
            val intent = Intent(this, MyTaskViewWebView::class.java)
            intent.putExtra("web_url", my_char_url)
            intent.putExtra("status", true)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


    private fun initvalues() {
        Adapterarrylist.clear()
        swipetorefresh = findViewById(R.id.starline_swipetorefresh)
        session = MySession(applicationContext)
        progressBar = findViewById(R.id.progressbar2)
        startline_chartIV = findViewById(R.id.desawar_chart)
        getsingeldigitvalue = findViewById(R.id.starlinegamelist_singledigit)
        getsingelpanavalue = findViewById(R.id.starlinegamelist_singlepana)
        getdoublepanavalue = findViewById(R.id.starlinegamelist_doublepana)
        gettripplepanavalue = findViewById(R.id.starlinegamelist_triplepana)
        recyclerView = findViewById(R.id.starlinegame_recyclerview)
        userbackBUT = findViewById(R.id.userbackbut)
        bidhistoryBUT = findViewById(R.id.starlinegame_bidhistorybut)
        winhistoryBUT = findViewById(R.id.starlinegame_winhistory)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.setLayoutManager(llm)
        retrofitgamedata()
        retrofitgameratesdata()
    }

    private fun retrofitgamedata() {
        Adapterarrylist.clear()
//        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)

        RetrofitClient.service.starlinegamedata(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val responsevalues = response
                        Log.d("Desawar", "onResponse: ${response.body()}")
                        var gameObject: JsonObject
                        val web_starline_chart_url =
                            responsevalues.body()?.get("web_galidessar_chart_url").toString()
                                .replace("\"", "")
                        my_char_url = web_starline_chart_url
                        val gamerateArrayObject =
                            responsevalues.body()?.getAsJsonArray("result")!!
                        var i = 0
                        while (i < gamerateArrayObject.size()) {
                            gameObject = gamerateArrayObject[i] as JsonObject
                            i++
                            val game_id = gameObject.get("game_id").toString().replace(
                                "\"",
                                ""
                            )
                            val game_name = gameObject.get("game_name").toString().replace(
                                "\"",
                                ""
                            )
                            val msg = gameObject.get("msg").toString().replace(
                                "\"",
                                ""
                            )
                            val msg_status = gameObject.get("msg_status").toString().replace(
                                "\"",
                                ""
                            )
                            val open_result = gameObject.get("open_result").toString().replace(
                                "\"",
                                ""
                            )
                            val close_result = gameObject.get("close_result").toString().replace(
                                "\"",
                                ""
                            )
                            val open_time = gameObject.get("open_time").toString().replace(
                                "\"",
                                ""
                            )
                            Adapterarrylist.add(
                                StarlineGamedataholder(
                                    game_id,
                                    game_name,
                                    msg,
                                    msg_status,
                                    open_result,
                                    close_result,
                                    open_time
                                )
                            )
                        }

                        val adaptercalling = StarlineGameAdapter(
                            applicationContext,
                            Adapterarrylist
                        )
                        recyclerView.setHasFixedSize(true)
                        adaptercalling.notifyDataSetChanged()
                        recyclerView.adapter = adaptercalling
                        myHideShowProgress(false)
                    }else {
                        val errorMessage = response.errorBody()?.string()
                        val httpStatusCode = response.code()

                        Log.e("Response", "Response not successful. HTTP Status Code: $httpStatusCode")
                        if (errorMessage != null) {
                            Log.e("Response", "Error message: $errorMessage")
                        } else {
                            Log.e("Response", "Error message is empty")
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Unable to Connect to Server",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("Response", "Request failed", t)

                    myHideShowProgress(false)
                }
            })
    }

    private fun retrofitgameratesdata() {
        Adapterarrylist.clear()
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)

        RetrofitClient.service.starlinegamerates(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val gameObject: JsonObject
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")

                        if (status.equals("true")) {
                            val gamerateArrayObject =
                                response.body()?.getAsJsonArray("game_rates")!!
                            gameObject = gamerateArrayObject.get(0) as JsonObject

                            val single_digit_val_1 =
                                gameObject.get("single_digit_val_1").toString().replace(
                                    "\"",
                                    ""
                                )
                            val single_digit_val_2 =
                                gameObject.get("single_digit_val_2").toString().replace(
                                    "\"",
                                    ""
                                )
                            val single_pana_val_1 =
                                gameObject.get("single_pana_val_1").toString().replace(
                                    "\"",
                                    ""
                                )
                            val single_pana_val_2 =
                                gameObject.get("single_pana_val_2").toString().replace(
                                    "\"",
                                    ""
                                )
                            val double_pana_val_1 =
                                gameObject.get("double_pana_val_1").toString().replace(
                                    "\"",
                                    ""
                                )
                            val double_pana_val_2 =
                                gameObject.get("double_pana_val_2").toString().replace(
                                    "\"",
                                    ""
                                )
                            getsingeldigitvalue.text = single_digit_val_1 + "-" + single_digit_val_2
                            getsingelpanavalue.text = single_pana_val_1 + "-" + single_pana_val_2
                            getdoublepanavalue.text = double_pana_val_1 + "-" + double_pana_val_2
                        }
                        myHideShowProgress(false)
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