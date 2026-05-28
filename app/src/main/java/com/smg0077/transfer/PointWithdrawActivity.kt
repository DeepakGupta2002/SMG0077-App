package com.smg0077.transfer

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.smg0077.AppDelegate
import com.smg0077.HomeActivity
import com.smg0077.all_adapter.WithdrawTransHistoryRecyclerAdapter
import com.smg0077.model.WithdrawTransHistoryDataHolder
import com.smg0077.R
import com.smg0077.bating.BatTimeActivity
import com.smg0077.common.GameRates
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.session.MySession
import kotlinx.android.synthetic.main.activity_point_withdraw.RadioWithdrawType
import kotlinx.android.synthetic.main.activity_point_withdraw.notewithdrawal
import kotlinx.android.synthetic.main.activity_point_withdraw.withdrawal_number
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PointWithdrawActivity : AppCompatActivity() {
    lateinit var userback: ImageView
    lateinit var walletpoint: TextView
    lateinit var timingstv: TextView
    lateinit var with_Point_value: EditText
    lateinit var submitButton: Button
    lateinit var recyclerView: RecyclerView
    lateinit var last_request_status: String
    lateinit var Min_Withdraw: String
    lateinit var Max_Withdraw: String
    lateinit var Withdraw_status: String
    var minwithdrawamt: String = "0"
    var maxwithdrawamt: String = "0"
    lateinit var progressBar: RelativeLayout
    lateinit var app_key: AppKeyHolderClass
    lateinit var session: MySession
    var withdrawtype= ""
    var adapterarrylist: ArrayList<WithdrawTransHistoryDataHolder> = ArrayList()
    val categories: MutableList<String> = ArrayList()

    var withdraw_open_time = ""
    var withdraw_close_time = ""
    private lateinit var bidHistory: LinearLayout
    private lateinit var withdraw_point: LinearLayout
    private lateinit var menu_home: RelativeLayout
    lateinit var wanumberButton: LinearLayout
    lateinit var addpointButton: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_withdraw)

        Min_Withdraw = intent.getStringExtra("min_withdraw").toString()
        Max_Withdraw = intent.getStringExtra("max_withdraw").toString()
        Withdraw_status = intent.getStringExtra("withdraw_status").toString()

        initValues()

        userback.setOnClickListener {
            onBackPressed()
        }

        RadioWithdrawType.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            withdrawtype = radioButton.text.toString()
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

        submitButton.setOnClickListener(View.OnClickListener {
            Retrofitwithdrawhistoryinside()

            if (withdrawtype.length < 3) {
                val snack2 = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Select Payment Mode",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snack2.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                view.layoutParams = params
                snack2.show()

                return@OnClickListener
            }

            val with_point = with_Point_value.text.toString()
            if (with_point.isEmpty()) {
                with_Point_value.error = "Enter values!"
                return@OnClickListener
            }
            val withdrawalnumber = withdrawal_number.text.toString().trim()
            if (withdrawalnumber.length < 10 ) {
                withdrawal_number.error = "Enter Valid Number!"
                return@OnClickListener
            }

            var cast_with_point: Int = 0
            cast_with_point = with_point.toInt()
            var cast_max_point: Int = 0
            cast_max_point = maxwithdrawamt.toInt()
            var cast_min_point: Int = 0
            cast_min_point = minwithdrawamt.toInt()
            val temp = walletpoint.text.toString().replace("\u20B9 ", "")
            var cast_wallet_point: Int = 0
            cast_wallet_point = temp.toInt()

            Log.d("withdrawwww", "onCreate: $Withdraw_status")
            if (Withdraw_status.equals("1")) {
                if (cast_with_point <= cast_wallet_point) { //amount should less then account point

                    if (cast_with_point < cast_min_point) { //amount should greater then min point
                        val snack2 = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Should be greater than minimum point:$cast_min_point",
                            Snackbar.LENGTH_LONG
                        )
                        val view: View = snack2.view
                        val params = view.layoutParams as FrameLayout.LayoutParams
                        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                        view.layoutParams = params
                        snack2.show()

                    } else {

                        if (last_request_status.equals("0")) {
                            val snack2 = Snackbar.make(
                                findViewById(android.R.id.content),
                                "Previous Request Pending!",
                                Snackbar.LENGTH_LONG
                            )
                            val view: View = snack2.view
                            val params = view.layoutParams as FrameLayout.LayoutParams
                            params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                            view.layoutParams = params
                            snack2.show()

                        } else {
                            if (cast_with_point < cast_max_point) {
                                val snack2 = Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Should be less than maximum withdraw limit : $Max_Withdraw",
                                    Snackbar.LENGTH_LONG
                                )

                                val view: View = snack2.view
                                val params = view.layoutParams as FrameLayout.LayoutParams
                                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                view.layoutParams = params
                                snack2.show()

                            } else {
                                RetrofitWithdrowfund()

//                                val intent = Intent(this, PinVerificationActivity::class.java)
//                                intent.putExtra("calling_activity", "pointWithdrawMoney")
//                                this.startActivityForResult(intent, 120)
                            }
                        }

                    }

                } else {
                    val snack2 = Snackbar.make(
                        findViewById(android.R.id.content),
                        "Should be less than wallet point:$cast_wallet_point",
                        Snackbar.LENGTH_LONG
                    )
                    val view: View = snack2.view
                    val params = view.layoutParams as FrameLayout.LayoutParams
                    params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                    view.layoutParams = params
                    snack2.show()
                }
            }
            else {
                val snak = Snackbar.make(
                    findViewById(android.R.id.content),
                    "You can withdraw only between $withdraw_open_time to $withdraw_close_time",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snak.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                view.layoutParams = params
                snak.show()
            }
        })
    }

    override fun onResume() {
        Retrofitwithdrawhistory()
        retrofitCheckWithdrawCondition()
        super.onResume()
    }

    private fun initValues() {
        userback = findViewById(R.id.userbackbut)
        progressBar = findViewById(R.id.progressbar2)
        session = MySession(applicationContext)
        walletpoint = findViewById(R.id.updatedpointtxt)
        submitButton = findViewById(R.id.user_submit_Button)
        recyclerView = findViewById(R.id.withdraw_point_recyclerview)
        timingstv = findViewById(R.id.timingstv)
        bidHistory = findViewById(R.id.bidHistory)
        menu_home = findViewById(R.id.menu_home)
        addpointButton = findViewById(R.id.home_add_point)
        wanumberButton = findViewById(R.id.home_whatappbut)
        withdraw_point = findViewById(R.id.withdraw_point)

        with_Point_value = findViewById(R.id.withdraw_pointsvalue)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        Retrofitwithdrawhistory()
        retrofitCheckWithdrawCondition()


    }

    private fun retrofitCheckWithdrawCondition() {
        categories.clear()
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        RetrofitClient.service.PaymentmethodListAPI(jsonvalues)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        categories.clear()

//                        println("++++++ r "+ Gson().toJson(response.body()))

                        val status = response.body()?.get("status").toString().replace("\"", "")
                        val minWithdrawal =
                            response.body()?.get("min_amt").toString().replace("\"", "")
                        minwithdrawamt = minWithdrawal
                        println("+++++"+ minwithdrawamt)


                        notewithdrawal.text = "--> Minimum $minwithdrawamt ₹ \n--> Maximum Unlimited\n--> Instant money withdrawal\n" +
                                "--> कम से कम $minwithdrawamt ₹ \n--> कोई अधिकतम सीमा नहीं \n--> तुरंत पैसा प्राप्त करें"

                        if (status == "true") {
                            var jsonObject = JsonObject()
                            val jsonArray = response.body()?.getAsJsonArray("result")!!
                            var i = 0
                            while (i < jsonArray.size()) {
                                jsonObject = jsonArray[i] as JsonObject
                                ++i
                                val type = jsonObject.get("type").toString().replace("\"", "")
                                val phone_value =
                                    jsonObject.get("value").toString().replace("\"", "")
                                jsonObject.get("name").toString().replace("\"", "")

                                if (type == "2") {
                                    categories.add("Paytm      $phone_value")
                                }
                                if (type == "3") {
                                    categories.add("Google Pay $phone_value")
                                }
                                if (type == "4") {
                                    categories.add("Phonepe      $phone_value")
                                }
                            }

                            myHideShowProgress(false)
                        } else {
                            myHideShowProgress(false)
                        }

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

    private fun RetrofitWithdrowfund() {
        val withdrountamount = with_Point_value.text.toString()
        val withdrawalnumber = withdrawal_number.text.toString().trim()
        val spinnermainval: String = withdrawtype.substring(0, 3)

        with_Point_value.text.clear()
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("request_amount", withdrountamount)
        jsonvalues.addProperty("mobile_number", withdrawalnumber)
        if (spinnermainval.equals("Pay")) {
            jsonvalues.addProperty("payment_method", "2")
        } else if (spinnermainval.equals("Goo")) {
            jsonvalues.addProperty("payment_method", "3")
        } else if (spinnermainval.equals("Pho")) {
            jsonvalues.addProperty("payment_method", "4")
        }

        RetrofitClient.service.WithdrowpointRequest(jsonvalues)
            .enqueue(object : Callback<JsonObject> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<JsonObject>, response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")
                        val msg = response.body()?.get("msg").toString().replace("\"", "")

                        if (status == "true") {
                            val withcon = withdrountamount
                            val withcast: Int = withcon.toInt()
                            val temp = walletpoint.text.toString().replace("\u20B9 ", "")
                            var castWalletpoint: Int = temp.toInt()
                            castWalletpoint -= withcast
                            walletpoint.text = "\u20B9 $castWalletpoint"

                            Snackbar.make(
                                findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG
                            ).show()

                        } else {
                            Snackbar.make(
                                findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG
                            ).show()

                        }

                    }
                    onResume()
                    myHideShowProgress(false)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Unable to Connect to Internet",
                        Snackbar.LENGTH_LONG
                    ).show()
                    onResume()
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

    private fun Retrofitwithdrawhistory() {
        adapterarrylist.clear()
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())

        val call = RetrofitClient.service.WithdrowTransactionHistory(jsonvalues)

        call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: Response<JsonObject>


                ) {
                    adapterarrylist.clear()
                    if (response.isSuccessful) {
                        val status = response.body()?.get("status").toString().replace(
                            "\"", "")
                        timingstv.text =
                            "You can withdraw only between $withdraw_open_time to $withdraw_close_time"

                        val opentime =
                            response.body()?.get("withdraw_open_time").toString().replace(
                                "\"", "")
                        withdraw_open_time = opentime

                        val closetime =
                            response.body()?.get("withdraw_close_time").toString().replace(
                                "\"", "")

                        withdraw_close_time = closetime

                        val msg = response.body()?.get("msg").toString().replace(
                            "\"", "")
                        val last_request_stat =
                            response.body()?.get("last_request_status").toString()
                                .replace("\"", "")
                        last_request_status = last_request_stat

                        val walletamt =
                            response.body()?.get("wallet_amt").toString().replace("\"", "")
                        walletpoint.text = "\u20B9 $walletamt"
                        if (status.equals("true")) {

                            var gameObject: JsonObject
                            val gamerateArrayObject =
                                response.body()?.getAsJsonArray("withdrawdata")!!

                            var i = 0
                            while (i < gamerateArrayObject.size()) {
                                gameObject = gamerateArrayObject[i] as JsonObject
                                i++
                                val request_no =
                                    gameObject.get("request_number").toString().replace("\"", "")
                                val request_amount =
                                    gameObject.get("request_amount").toString().replace("\"", "")
                                val request_status =
                                    gameObject.get("request_status").toString().replace("\"", "")
                                val payment_method =
                                    gameObject.get("payment_method").toString().replace("\"", "")


                                val insert_date =
                                    gameObject.get("insert_date").toString().replace("\"", "")
                                val remark = gameObject.get("remark").toString().replace("\"", "")
                                val paymetrecept =
                                    gameObject.get("payment_receipt").toString().replace("\"", "")

                                adapterarrylist.add(
                                    WithdrawTransHistoryDataHolder(
                                        request_no,
                                        request_amount,
                                        request_status,
                                        payment_method,
                                        remark,
                                        insert_date,
                                        paymetrecept
                                    )
                                )
                            }

                            val adaptercalling = WithdrawTransHistoryRecyclerAdapter(
                                applicationContext, adapterarrylist
                            )
                            recyclerView.adapter = adaptercalling
                            myHideShowProgress(false)
                        } else {
                            Snackbar.make(
                                findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG
                            ).show()
                        }
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

    private fun Retrofitwithdrawhistoryinside() {
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())

        RetrofitClient.service.WithdrowTransactionHistory(jsonvalues)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val last_request_stat =
                            response.body()?.get("last_request_status").toString()
                                .replace("\"", "")
                        last_request_status = last_request_stat

                        val walletamt =
                            response.body()?.get("wallet_amt").toString().replace("\"", "")
                        walletpoint.text = "\u20B9 $walletamt"
                    }

                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 120) {
            if (resultCode == RESULT_OK) {
                val response_status = data?.getStringExtra("Result").toString()
                if (response_status.equals("Success")) {
//                    RetrofitWithdrowfund()
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

}