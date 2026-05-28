package com.smg0077.bating

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smg0077.all_adapter.WinHistoryRecyclerAdapter
import com.smg0077.model.WinHistoryHolder
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.session.MySession
import com.google.gson.JsonObject
import com.smg0077.R
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient.env_type
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ToperHistoryActivity : AppCompatActivity() {
    lateinit var backbut: ImageView
    lateinit var startdate: TextView
    lateinit var enddate: TextView
    lateinit var getcallingactivity: String
    lateinit var submitbut: Button
    lateinit var recordFoundImage: RelativeLayout
    lateinit var calendar: Calendar
    lateinit var progressBar: View
    lateinit var recyclerView: RecyclerView
    lateinit var app_key: AppKeyHolderClass
    lateinit var session: MySession
    var Adapterarrylist: ArrayList<WinHistoryHolder> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win_history)
        initvalues()
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        calendar = Calendar.getInstance()
        startdate.text =
            "" + calendar[Calendar.DAY_OF_MONTH] + "-" + (calendar[Calendar.MONTH] + 1) + "-" + calendar[Calendar.YEAR]
        enddate.text =
            "" + calendar[Calendar.DAY_OF_MONTH] + "-" + (calendar[Calendar.MONTH] + 1) + "-" + calendar[Calendar.YEAR]
        if (getcallingactivity.equals("home")) {
            retrofitWinHistoryData(startdate.text.toString(), enddate.text.toString())
        } else if (getcallingactivity == "realstarline") {
            retrofitrealStarlineWinHistoryData(startdate.text.toString(), enddate.text.toString())
        } else {
            retrofitStarlineWinHistoryData(startdate.text.toString(), enddate.text.toString())
        }
        val StartdatePickerDialog = DatePickerDialog(
            this, R.style.datePicker, { _, year, month, dayOfMonth ->
                val dateis = "" + dayOfMonth + "-" + (month + 1) + "-" + year
                startdate.text = dateis

            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        )

        val EnddatePickerDialog = DatePickerDialog(
            this, R.style.datePicker, { _, year, month, dayOfMonth ->
                val dateis = "" + dayOfMonth + "-" + (month + 1) + "-" + year
                enddate.text = dateis

            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        )


        startdate.setOnClickListener {
            StartdatePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            StartdatePickerDialog.show()
        }

        enddate.setOnClickListener {
            EnddatePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            EnddatePickerDialog.show()
        }

        backbut.setOnClickListener {
            onBackPressed()
        }

        submitbut.setOnClickListener {
            val startdatestr = startdate.text.toString()
            val enddatestr = enddate.text.toString()
            if (startdatestr.isEmpty() && enddatestr.isEmpty()) {
                startdate.error = "select start Date!"
                enddate.error = "Select End Date!"
            } else {
                if (startdatestr.isEmpty()) {
                    startdate.error = "select start Date!"
                } else if (enddatestr.isEmpty()) {
                    enddate.error = "Select End Date!"
                } else {
                    startdate.error = null

                    if (getcallingactivity.equals("home")) {
                        retrofitWinHistoryData(startdatestr, enddatestr)
                    } else if (getcallingactivity == "realstarline") {
                        retrofitrealStarlineWinHistoryData(startdatestr, enddatestr)
                    } else {
                        retrofitStarlineWinHistoryData(startdatestr, enddatestr)
                    }
                }
            }

        }

    }

    private fun initvalues() {
        getcallingactivity = intent.getStringExtra("history_win").toString()
        backbut = findViewById(R.id.userbackbut)
        startdate = findViewById(R.id.startdatetxt)
        enddate = findViewById(R.id.todatetxt)
        session = MySession(applicationContext)
        recordFoundImage = findViewById(R.id.bidhistory_norecordfound)
        progressBar = findViewById(R.id.progressbar2)
        recyclerView = findViewById(R.id.win_history_recyclerview)
        submitbut = findViewById(R.id.user_submit_Button)
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

    private fun retrofitWinHistoryData(bid_from: String, bid_to: String) {
        Adapterarrylist.clear()

        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("date_from", bid_from)
        jsonvalues.addProperty("date_to", bid_to)

        RetrofitClient.service.winhistoryApi(jsonvalues).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>, response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    val status = response.body()?.get("status").toString().replace("\"", "")
                    val msg = response.body()?.get("msg").toString().replace("\"", "")
                    if (status.equals("true")) {
                        var gameObject: JsonObject
                        val gamerateArrayObject =
                            response.body()?.getAsJsonArray("win_data")!!
                        var i = 0
                        while (i < gamerateArrayObject.size()) {
                            gameObject = gamerateArrayObject[i] as JsonObject
                            i++
                            val amountwon = gameObject.get("amount").toString().replace(
                                "\"", ""
                            )
                            val transaction_type =
                                gameObject.get("transaction_type").toString().replace(
                                    "\"", ""
                                )
                            val pana_type = gameObject.get("pana").toString().replace(
                                "\"", ""
                            )
                            val session_type = gameObject.get("session").toString().replace(
                                "\"", ""
                            )
                            val transaction_note =
                                gameObject.get("transaction_note").toString().replace(
                                    "\"", ""
                                )

                            val Game_name = gameObject.get("game_name").toString().replace(
                                "\"", ""
                            )

                            val amount_status = gameObject.get("amount_status").toString().replace(
                                "\"", ""
                            )
                            val tx_request_number =
                                gameObject.get("tx_request_number").toString().replace(
                                    "\"", ""
                                )
                            val wining_date = gameObject.get("wining_date").toString().replace(
                                "\"", ""
                            )
                            Adapterarrylist.add(
                                WinHistoryHolder(
                                    pana_type,
                                    amountwon,
                                    transaction_type,
                                    transaction_note,
                                    amount_status,
                                    wining_date,
                                    tx_request_number,
                                    Game_name,
                                    session_type
                                )
                            )
                        }

                        val adaptercalling = WinHistoryRecyclerAdapter(
                            Adapterarrylist, true
                        )
                        recyclerView.adapter = adaptercalling
                        adaptercalling.notifyDataSetChanged()

                        myHideShowProgress(false)
                    } else {
                        val adaptercalling = WinHistoryRecyclerAdapter(
                            Adapterarrylist, true
                        )
                        recyclerView.adapter = adaptercalling
                        adaptercalling.notifyDataSetChanged()
                        Toast.makeText(
                            applicationContext, msg, Toast.LENGTH_LONG
                        ).show()
                        myHideShowProgress(false)
                    }

                }
                if (Adapterarrylist.isNotEmpty()) {
                    recordFoundImage.visibility = View.GONE
                } else {
                    recordFoundImage.visibility = View.VISIBLE
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

    private fun retrofitrealStarlineWinHistoryData(bid_from: String, bid_to: String) {
        Adapterarrylist.clear()

        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("date_from", bid_from)
        jsonvalues.addProperty("date_to", bid_to)

        RetrofitClient.service.realstarlinegameWiningHistoyApi(jsonvalues)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val status = response.body()?.get("status").toString().replace("\"", "")
                        val msg = response.body()?.get("msg").toString().replace("\"", "")
                        if (status.equals("true")) {
                            var gameObject: JsonObject
                            val gamerateArrayObject = response.body()?.getAsJsonArray("win_data")!!
                            var i = 0
                            while (i < gamerateArrayObject.size()) {
                                gameObject = gamerateArrayObject[i] as JsonObject
                                i++
                                val amountwon = gameObject.get("amount").toString().replace(
                                    "\"", ""
                                )
                                val transaction_type =
                                    gameObject.get("transaction_type").toString().replace(
                                        "\"", ""
                                    )
                                val pana_type = gameObject.get("pana").toString().replace(
                                    "\"", ""
                                )
                                val session_type = gameObject.get("session").toString().replace(
                                    "\"", ""
                                )
                                val transaction_note =
                                    gameObject.get("transaction_note").toString().replace(
                                        "\"", ""
                                    )

                                val Game_name = gameObject.get("game_name").toString().replace(
                                    "\"", ""
                                )
                                val amount_status =
                                    gameObject.get("amount_status").toString().replace(
                                        "\"", ""
                                    )
                                val tx_request_number =
                                    gameObject.get("tx_request_number").toString().replace(
                                        "\"", ""
                                    )
                                val wining_date = gameObject.get("wining_date").toString().replace(
                                    "\"", ""
                                )
                                Adapterarrylist.add(
                                    WinHistoryHolder(
                                        pana_type,
                                        amountwon,
                                        transaction_type,
                                        transaction_note,
                                        amount_status,
                                        wining_date,
                                        tx_request_number,
                                        Game_name,
                                        session_type
                                    )
                                )

                            }


                            val adaptercalling = WinHistoryRecyclerAdapter(
                                Adapterarrylist, false
                            )
                            recyclerView.adapter = adaptercalling
                            adaptercalling.notifyDataSetChanged()

                            myHideShowProgress(false)
                        } else {

                            val adaptercalling = WinHistoryRecyclerAdapter(
                                Adapterarrylist, false
                            )
                            recyclerView.adapter = adaptercalling
                            adaptercalling.notifyDataSetChanged()
                            Toast.makeText(
                                applicationContext, msg, Toast.LENGTH_LONG
                            ).show()
                            myHideShowProgress(false)
                        }


                    }
                    if (Adapterarrylist.isNotEmpty()) {
                        recordFoundImage.visibility = View.GONE
                    } else {
                        recordFoundImage.visibility = View.VISIBLE
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

    private fun retrofitStarlineWinHistoryData(bid_from: String, bid_to: String) {

        Adapterarrylist.clear()
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("date_from", bid_from)
        jsonvalues.addProperty("date_to", bid_to)

        RetrofitClient.service.starlinegameWiningHistoyApi(jsonvalues)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")
                        val msg = response.body()?.get("msg").toString().replace("\"", "")
                        if (status.equals("true")) {
                            var gameObject: JsonObject
                            val gamerateArrayObject =
                                response.body()?.getAsJsonArray("win_data")!!
                            var i = 0
                            while (i < gamerateArrayObject.size()) {
                                gameObject = gamerateArrayObject[i] as JsonObject
                                i++
                                val amountwon = gameObject.get("amount").toString().replace(
                                    "\"", ""
                                )
                                val transaction_type =
                                    gameObject.get("transaction_type").toString().replace(
                                        "\"", ""
                                    )
                                val pana_type = gameObject.get("pana").toString().replace(
                                    "\"", ""
                                )
                                val session_type = gameObject.get("session").toString().replace(
                                    "\"", ""
                                )
                                val transaction_note =
                                    gameObject.get("transaction_note").toString().replace(
                                        "\"", ""
                                    )
                                val Game_name = gameObject.get("game_name").toString().replace(
                                    "\"", ""
                                )
                                val amount_status =
                                    gameObject.get("amount_status").toString().replace(
                                        "\"", ""
                                    )
                                val tx_request_number =
                                    gameObject.get("tx_request_number").toString().replace(
                                        "\"", ""
                                    )
                                val wining_date = gameObject.get("wining_date").toString().replace(
                                    "\"", ""
                                )

                                Adapterarrylist.add(
                                    WinHistoryHolder(
                                        pana_type,
                                        amountwon,
                                        transaction_type,
                                        transaction_note,
                                        amount_status,
                                        wining_date,
                                        tx_request_number,
                                        Game_name,
                                        session_type
                                    )
                                )

                            }

                            val adaptercalling = WinHistoryRecyclerAdapter(
                                Adapterarrylist, false
                            )
                            recyclerView.adapter = adaptercalling
                            adaptercalling.notifyDataSetChanged()

                            myHideShowProgress(false)
                        } else {
                            val adaptercalling = WinHistoryRecyclerAdapter(
                                Adapterarrylist, false
                            )
                            recyclerView.adapter = adaptercalling
                            adaptercalling.notifyDataSetChanged()
                            Toast.makeText(
                                applicationContext, msg, Toast.LENGTH_LONG
                            ).show()
                            myHideShowProgress(false)
                        }
                    }
                    if (Adapterarrylist.isNotEmpty()) {
                        recordFoundImage.visibility = View.GONE
                    } else {
                        recordFoundImage.visibility = View.VISIBLE
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

}