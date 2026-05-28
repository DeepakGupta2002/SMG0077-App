package com.smg0077

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.smg0077.bating.BatScreenActivity
import com.smg0077.bating.BatTimeActivity
import com.smg0077.common.GameRates
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.session.MySession
import com.smg0077.transfer.PointAddViaQRActivity
import com.smg0077.transfer.PointWithdrawActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskListWindow : AppCompatActivity() {
    lateinit var userback: ImageView
    lateinit var appKey: AppKeyHolderClass
    lateinit var session: MySession
    lateinit var singledigitBut: RelativeLayout
    lateinit var progressBar: View
    lateinit var jodiDigitBut: RelativeLayout
    lateinit var singlepanatBut: RelativeLayout
    lateinit var doublepanaBut: RelativeLayout
    lateinit var tripplepanaBut: RelativeLayout
    lateinit var halfsangamBut: RelativeLayout
    lateinit var fullsangamBut: RelativeLayout
    lateinit var windowtitle: TextView
    private lateinit var bidHistory: LinearLayout
    private lateinit var withdraw_point: LinearLayout
    private lateinit var menu_home: RelativeLayout
    lateinit var wanumberButton: LinearLayout
    lateinit var addpointButton: LinearLayout
    var game_idget: String = "none"
    var game_nameget: String = "none"
    var get_Status: String = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_view_window)
        singledigitBut = findViewById(R.id.singledigitgame)
        jodiDigitBut = findViewById(R.id.jodidigitgame)
        session = MySession(applicationContext)
        singlepanatBut = findViewById(R.id.singlepanagame)
        doublepanaBut = findViewById(R.id.doublepanagame)
        tripplepanaBut = findViewById(R.id.triplepanagame)
        halfsangamBut = findViewById(R.id.halfsangamgame)
        fullsangamBut = findViewById(R.id.fullsangamgame)
        userback = findViewById(R.id.userbackbut)
        progressBar = findViewById(R.id.progressbar2)
        windowtitle = findViewById(R.id.welcometxt)
        bidHistory = findViewById(R.id.bidHistory)
        menu_home = findViewById(R.id.menu_home)
        addpointButton = findViewById(R.id.home_add_point)
        wanumberButton = findViewById(R.id.home_whatappbut)
        withdraw_point = findViewById(R.id.withdraw_point)
        game_idget = intent.getStringExtra("game_id").toString()
        game_nameget = intent.getStringExtra("game_name").toString()
        windowtitle.text = game_nameget
        retrofitCheckGameStatus()


        userback.setOnClickListener {
            onBackPressed()
        }

        singledigitBut.setOnClickListener {
            openBidWindow("single_digit")
        }
        jodiDigitBut.setOnClickListener(View.OnClickListener {
            if (get_Status.equals("2")) {
                val snak = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Sorry session close for now",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snak.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                view.layoutParams = params
                snak.show()
                return@OnClickListener

            } else {
                openBidWindow("jodi_digit")
            }

        })
        singlepanatBut.setOnClickListener {
            openBidWindow("single_pana")
        }
        doublepanaBut.setOnClickListener {
            openBidWindow("double_pana")
        }
        tripplepanaBut.setOnClickListener {
            openBidWindow("triple_pana")
        }
        halfsangamBut.setOnClickListener(View.OnClickListener {
            if (get_Status.equals("2")) {
                val snak = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Sorry session close for now",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snak.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                view.layoutParams = params
                snak.show()
                return@OnClickListener

            } else {
                openBidWindow("half_sangam")
            }
        })
        fullsangamBut.setOnClickListener(View.OnClickListener {
            if (get_Status.equals("2")) {
                val snak = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Sorry session close for now",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snak.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                view.layoutParams = params
                snak.show()
                return@OnClickListener

            } else {
                openBidWindow("full_sangam")
            }

        })

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

    private fun openBidWindow(gametype: String) {
        val Intent = Intent(applicationContext, BatScreenActivity::class.java)
        Intent.putExtra("game_type", gametype)
        Intent.putExtra("game_id", game_idget)
        Intent.putExtra("game_name", game_nameget)
        Intent.putExtra("game_status", get_Status)
        startActivity(Intent)
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

    private fun retrofitCheckGameStatus() {
        val uniqueToken = session.getSession_userid()
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("game_id", game_idget)
        jsonvalues.addProperty("unique_token", uniqueToken)

        RetrofitClient.service.Checkgamestatus(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.get("msg").toString().replace("\"", "")
                        val game_status =
                            response.body()?.get("game_status").toString().replace("\"", "")
                        get_Status = game_status
                        myHideShowProgress(false)
                    }
                    myHideShowProgress(false)
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