package com.smg0077.bating

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smg0077.all_adapter.BidHistoryRecyclerAdapter
import com.smg0077.model.bidHistoryHolder
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.session.MySession
import com.google.gson.JsonObject
import com.smg0077.HomeActivity
import com.smg0077.R
import com.smg0077.common.GameRates
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.transfer.PointAddViaQRActivity
import com.smg0077.transfer.PointWithdrawActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class BatTimeActivity : AppCompatActivity() {

    lateinit var backbut: ImageView
    lateinit var startdate: TextView
    lateinit var enddate: TextView
    lateinit var submitbut: Button
    lateinit var calendar: Calendar
    lateinit var progressBar: View
    lateinit var recordFoundImage: RelativeLayout
    lateinit var getcallingactivity: String
    lateinit var recyclerView: RecyclerView
    lateinit var session: MySession
    private lateinit var bidHistory: LinearLayout
    private lateinit var withdraw_point: LinearLayout
    private lateinit var menu_home: RelativeLayout
    lateinit var wanumberButton: LinearLayout
    lateinit var addpointButton: LinearLayout
    var Adapterarrylist: ArrayList<bidHistoryHolder> = ArrayList()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bid_history)

        initvalues()
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        calendar = Calendar.getInstance()
        startdate.text =
            "" + calendar[Calendar.DAY_OF_MONTH] + "-" + (calendar[Calendar.MONTH] + 1) + "-" + calendar[Calendar.YEAR]
        enddate.text =
            "" + calendar[Calendar.DAY_OF_MONTH] + "-" + (calendar[Calendar.MONTH] + 1) + "-" + calendar[Calendar.YEAR]
        if (getcallingactivity == "home") {
            retrofitBidHistoryData(startdate.text.toString(), enddate.text.toString())
        } else {
            if (getcallingactivity == "realstarline") {
                retrofitrealStarlineBidHistoryData(
                    startdate.text.toString(),
                    enddate.text.toString()
                )
            } else {
                retrofitStarlineBidHistoryData(startdate.text.toString(), enddate.text.toString())
            }
        }

        val startdatePickerDialog = DatePickerDialog(
            this,
            R.style.datePicker,
            { _, year, month, dayOfMonth ->
                val dateis = "" + dayOfMonth + "-" + (month + 1) + "-" + year
                startdate.text = dateis

            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )

        val enddatePickerDialog = DatePickerDialog(
            this,
            R.style.datePicker,
            { _, year, month, dayOfMonth ->
                val dateis = "" + dayOfMonth + "-" + (month + 1) + "-" + year
                enddate.text = dateis

            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )

        startdate.setOnClickListener {
            startdatePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            startdatePickerDialog.show()
        }

        enddate.setOnClickListener {
            enddatePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            enddatePickerDialog.show()
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
                        retrofitBidHistoryData(startdatestr, enddatestr)
                    } else {
                        if (getcallingactivity == "realstarline") {
                            retrofitrealStarlineBidHistoryData(startdatestr, enddatestr)
                        } else {
                            retrofitStarlineBidHistoryData(startdatestr, enddatestr)
                        }

                    }

                }
            }
        }

        backbut.setOnClickListener {
            onBackPressed()
        }

    }

    private fun initvalues() {
        getcallingactivity = intent.getStringExtra("history_bid").toString()
        session = MySession(applicationContext)
        recordFoundImage = findViewById(R.id.bidhistory_norecordfound)
        progressBar = findViewById(R.id.progressbar2)
        backbut = findViewById(R.id.userbackbut)
        startdate = findViewById(R.id.startdatetxt)
        enddate = findViewById(R.id.todatetxt)
        recyclerView = findViewById(R.id.bid_history_recyclerview)
        submitbut = findViewById(R.id.user_submit_Button)
        bidHistory = findViewById(R.id.bidHistory)
        menu_home = findViewById(R.id.menu_home)
        addpointButton = findViewById(R.id.home_add_point)
        wanumberButton = findViewById(R.id.home_whatappbut)
        withdraw_point = findViewById(R.id.withdraw_point)
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

    private fun retrofitBidHistoryData(bid_from: String, bid_to: String) {
        Adapterarrylist.clear()

        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("bid_from", bid_from)
        jsonvalues.addProperty("bid_to", bid_to)

        RetrofitClient.service.bidhistoryApi(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")
                        val msg = response.body()?.get("msg").toString().replace("\"", "")
                        if (status.equals("true")) {
                            var gameObject: JsonObject
                            val gamerateArrayObject =
                                response.body()?.getAsJsonArray("bid_data")!!
                            var i = 0
                            Adapterarrylist.clear()
                            while (i < gamerateArrayObject.size()) {
                                gameObject = gamerateArrayObject[i] as JsonObject
                                i++

                                val gamename = gameObject.get("game_name").toString().replace(
                                    "\"",
                                    ""
                                )
                                val pana = gameObject.get("pana").toString().replace(
                                    "\"",
                                    ""
                                )
                                val bid_date = gameObject.get("bid_date").toString().replace(
                                    "\"",
                                    ""
                                )

                                val points = gameObject.get("points").toString().replace(
                                    "\"",
                                    ""
                                )
                                val sessiontype = gameObject.get("session").toString().replace(
                                    "\"",
                                    ""
                                )
                                val ondigit = gameObject.get("digits").toString().replace(
                                    "\"",
                                    ""
                                )
                                val closedigit = gameObject.get("closedigits").toString().replace(
                                    "\"",
                                    ""
                                )
                                Adapterarrylist.add(
                                    bidHistoryHolder(
                                        gamename,
                                        pana,
                                        bid_date,
                                        points,
                                        sessiontype,
                                        ondigit,
                                        closedigit
                                    )
                                )

                            }

                            val adaptercalling = BidHistoryRecyclerAdapter(
                                Adapterarrylist,
                                true
                            )
                            recyclerView.adapter = adaptercalling
                            adaptercalling.notifyDataSetChanged()

                            myHideShowProgress(false)
                        } else {
                            val adaptercalling = BidHistoryRecyclerAdapter(
                                Adapterarrylist,
                                true
                            )
                            recyclerView.adapter = adaptercalling
                            adaptercalling.notifyDataSetChanged()

                            Toast.makeText(
                                applicationContext,
                                msg,
                                Toast.LENGTH_LONG
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
                        applicationContext,
                        "Unable to Connect to Internet",
                        Toast.LENGTH_LONG
                    ).show()
                    myHideShowProgress(false)
                }
            })
    }

    private fun retrofitStarlineBidHistoryData(bid_from: String, bid_to: String) {
        Adapterarrylist.clear()
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("bid_from", bid_from)
        jsonvalues.addProperty("bid_to", bid_to)

        RetrofitClient.service.starlinegamebidHistoyApi(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")
                        val msg = response.body()?.get("msg").toString().replace("\"", "")

                        if (status.equals("true")) {
                            var gameObject: JsonObject
                            val gamerateArrayObject =
                                response.body()?.getAsJsonArray("bid_data")!!
                            var i = 0
                            Adapterarrylist.clear()
                            while (i < gamerateArrayObject.size()) {
                                gameObject = gamerateArrayObject[i] as JsonObject
                                i++

                                val gamename = gameObject.get("game_name").toString().replace(
                                    "\"",
                                    ""
                                )
                                val pana = gameObject.get("pana").toString().replace(
                                    "\"",
                                    ""
                                )
                                val bid_date = gameObject.get("bid_date").toString().replace(
                                    "\"",
                                    ""
                                )

                                val points = gameObject.get("points").toString().replace(
                                    "\"",
                                    ""
                                )
                                val sessiontype = gameObject.get("session").toString().replace(
                                    "\"",
                                    ""
                                )
                                val opendigit = gameObject.get("digits").toString().replace(
                                    "\"",
                                    ""
                                )
                                val closedigit = gameObject.get("closedigits").toString().replace(
                                    "\"",
                                    ""
                                )
                                Adapterarrylist.add(
                                    bidHistoryHolder(
                                        gamename,
                                        pana,
                                        bid_date,
                                        points,
                                        sessiontype,
                                        opendigit,
                                        closedigit
                                    )
                                )
                            }

                            val adaptercalling = BidHistoryRecyclerAdapter(
                                Adapterarrylist,
                                false
                            )
                            recyclerView.adapter = adaptercalling
                            adaptercalling.notifyDataSetChanged()
                            myHideShowProgress(false)
                        } else {
                            val adaptercalling = BidHistoryRecyclerAdapter(
                                Adapterarrylist,
                                false
                            )
                            recyclerView.adapter = adaptercalling
                            adaptercalling.notifyDataSetChanged()

                            Toast.makeText(
                                applicationContext,
                                msg,
                                Toast.LENGTH_LONG
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
                        applicationContext,
                        "Unable to Connect to Internet",
                        Toast.LENGTH_LONG
                    ).show()
                    myHideShowProgress(false)
                }
            })
    }

    private fun retrofitrealStarlineBidHistoryData(bid_from: String, bid_to: String) {
        Adapterarrylist.clear()
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("bid_from", bid_from)
        jsonvalues.addProperty("bid_to", bid_to)

        RetrofitClient.service.realstarlinegamebidHistoyApi(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")
                        val msg = response.body()?.get("msg").toString().replace("\"", "")
                        if (status.equals("true")) {
                            var gameObject: JsonObject
                            val gamerateArrayObject =
                                response.body()?.getAsJsonArray("bid_data")!!
                            var i = 0
                            Adapterarrylist.clear()
                            while (i < gamerateArrayObject.size()) {
                                gameObject = gamerateArrayObject[i] as JsonObject
                                i++
                                val gamename = gameObject.get("game_name").toString().replace(
                                    "\"",
                                    ""
                                )
                                val pana = gameObject.get("pana").toString().replace(
                                    "\"",
                                    ""
                                )
                                val bid_date = gameObject.get("bid_date").toString().replace(
                                    "\"",
                                    ""
                                )
                                val points = gameObject.get("points").toString().replace(
                                    "\"",
                                    ""
                                )
                                val sessiontype = gameObject.get("session").toString().replace(
                                    "\"",
                                    ""
                                )
                                val opendigit = gameObject.get("digits").toString().replace(
                                    "\"",
                                    ""
                                )
                                val closedigit = gameObject.get("closedigits").toString().replace(
                                    "\"",
                                    ""
                                )
                                Adapterarrylist.add(
                                    bidHistoryHolder(
                                        gamename,
                                        pana,
                                        bid_date,
                                        points,
                                        sessiontype,
                                        opendigit,
                                        closedigit
                                    )
                                )
                            }
                            val adaptercalling = BidHistoryRecyclerAdapter(
                                Adapterarrylist,
                                false
                            )
                            recyclerView.adapter = adaptercalling
                            adaptercalling.notifyDataSetChanged()
                            myHideShowProgress(false)
                        } else {
                            val adaptercalling = BidHistoryRecyclerAdapter(
                                Adapterarrylist,
                                false
                            )
                            recyclerView.adapter = adaptercalling
                            adaptercalling.notifyDataSetChanged()

                            Toast.makeText(
                                applicationContext,
                                msg,
                                Toast.LENGTH_LONG
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
                        applicationContext,
                        "Unable to Connect to Internet",
                        Toast.LENGTH_LONG
                    ).show()
                    myHideShowProgress(false)
                }
            })
    }

}