package com.smg0077.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.smg0077.all_adapter.GameChartRecyclerAdapter
import com.smg0077.model.GraphUrl
import com.smg0077.R
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.session.MySession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChartDraw : AppCompatActivity() {
    lateinit var session: MySession
    lateinit var backbut: ImageView
    lateinit var progressBar: RelativeLayout
    lateinit var app_key: AppKeyHolderClass
    lateinit var recyclerView: RecyclerView
    var gameDataList: ArrayList<GraphUrl> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart_draw)
        session = MySession(applicationContext)
        initValues()

        backbut.setOnClickListener {
            onBackPressed()
        }

    }

    private fun initValues() {
        backbut = findViewById(R.id.userbackbut)
        recyclerView = findViewById(R.id.chartRecyclerView)
        progressBar = findViewById(R.id.progressbar2)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        retrofitGameData()

    }

    private fun retrofitGameData() {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())

        RetrofitClient.service.getdashboardData(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val status = response.body()?.get("status").toString().replace(
                            "\"",
                            ""
                        )
                        gameDataList.clear()
                        if (status.equals("true")) {

                            var gameObject: JsonObject
                            val gamerateArrayObject =
                                response.body()?.getAsJsonArray("result")!!
                            var i = 0
                            while (i < gamerateArrayObject.size()) {
                                gameObject = gamerateArrayObject[i] as JsonObject
                                i++
                                val gamename = gameObject.get("game_name").asString
                                val weburl = gameObject.get("web_chart_url").asString
                                gameDataList.add(
                                    GraphUrl(
                                        gamename,
                                        weburl
                                    )
                                )
                            }
                            val adaptercalling = GameChartRecyclerAdapter(
                                this@ChartDraw,
                                gameDataList
                            )
                            recyclerView.adapter = adaptercalling
                            myHideShowProgress(false)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "No Record Found!",
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