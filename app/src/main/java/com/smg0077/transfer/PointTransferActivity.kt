package com.smg0077.transfer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.smg0077.R
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.session.MySession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PointTransferActivity : AppCompatActivity() {
    lateinit var userback: ImageView
    lateinit var progressBar: RelativeLayout
    lateinit var walletpoint: TextView
    lateinit var mobileNumber: EditText
    lateinit var transferPoints: EditText
    lateinit var SubmitBut: Button
    lateinit var useraccountfound: String
    lateinit var Min_Transfer: String
    lateinit var Max_Transfer: String
    lateinit var Transfer_status: String
    var transfer_minamt: Int = 0
    var transfer_maxamt: Int = 0
    var transfer_status_point: String = "0"
    lateinit var app_key: AppKeyHolderClass
    lateinit var session: MySession
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_transfer)
        session = MySession(applicationContext)
        Min_Transfer = intent.getStringExtra("min_transfer").toString()
        Max_Transfer = intent.getStringExtra("max_transfer").toString()
        Transfer_status = intent.getStringExtra("transfer_status").toString()
        initvalues()

        userback.setOnClickListener {
            onBackPressed()
        }

        SubmitBut.setOnClickListener(View.OnClickListener {
            val mobile_number = mobileNumber.text.toString().trim()
            val points_trans = transferPoints.text.toString().replace(".", "").trim()
            if (mobile_number.equals(session.getSession_usermobile())) { //self transfer
                val snack2 = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Self Transfer not allowed!",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snack2.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                view.layoutParams = params
                snack2.show()
                return@OnClickListener
            }

            val point = walletpoint.text.toString().replace("\u20B9 ", "")
            if (Transfer_status.equals("0")) {
                val snack2 = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Transfer not allowed at the moment!",
                    Snackbar.LENGTH_LONG
                )
                val view: View = snack2.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                view.layoutParams = params
                snack2.show()
                return@OnClickListener
            }

            if (mobile_number.isEmpty()) {
                mobileNumber.error = "Empty!"
                return@OnClickListener
            } else if (mobile_number.length <= 9) {
                mobileNumber.error = "Invalid Number"
                return@OnClickListener
            } else {

                var pointint: Int = 0
                pointint = point.toInt()
                if (points_trans.isEmpty()) {
                    transferPoints.error = "Empty!"
                    return@OnClickListener
                } else {
                    val max_amt: Int
                    max_amt = Max_Transfer.toInt()
                    var points: Int = 0
                    points = points_trans.toInt()
                    if (points > pointint) {
                        val snack2 = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Should be less than wallet point!",
                            Snackbar.LENGTH_LONG
                        )
                        val view: View = snack2.view
                        val params = view.layoutParams as FrameLayout.LayoutParams
                        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                        view.layoutParams = params
                        snack2.setBackgroundTint(resources.getColor(R.color.red))
                        snack2.show()
                        return@OnClickListener
                    } else if (points < transfer_minamt) {
                        val snack2 = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Should be greater than minimum transfer $transfer_minamt",
                            Snackbar.LENGTH_LONG
                        )
                        val view: View = snack2.view
                        val params = view.layoutParams as FrameLayout.LayoutParams
                        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                        view.layoutParams = params
                        snack2.setBackgroundTint(resources.getColor(R.color.red))
                        snack2.show()
                        return@OnClickListener

                    } else if (points >= max_amt) {
                        val snack2 = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Should be less than maximum transfer $transfer_maxamt",
                            Snackbar.LENGTH_LONG
                        )
                        val view: View = snack2.view
                        val params = view.layoutParams as FrameLayout.LayoutParams
                        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                        view.layoutParams = params
                        snack2.setBackgroundTint(resources.getColor(R.color.red))
                        snack2.show()
                        return@OnClickListener
                    } else RetrofitCheckwithdrawCondition()


                }


            }
        })

        mobileNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val lengh = mobileNumber.text.length
                if (lengh == 10) {
                    RetrofitCheckMobileNumberCondition()
                } else {
                    mobileNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


    }

    private fun initvalues() {
        userback = findViewById(R.id.userbackbut)
        progressBar = findViewById(R.id.progressbar2)
        walletpoint = findViewById(R.id.updatedpointtxt)
        mobileNumber = findViewById(R.id.transfer_mobileno)
        transferPoints = findViewById(R.id.transfer_points)
        SubmitBut = findViewById(R.id.user_submit_Button)
        useraccountfound = ""
        walletpoint.text = "\u20B9 0"
        RetrofitCheckTransferCondition()
    }


    private fun RetrofitCheckTransferCondition() {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        RetrofitClient.service.refreshAmtApi(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val responsevalues = response
                        val status =
                            responsevalues.body()?.get("status").toString().replace("\"", "")
                        val msg = responsevalues.body()?.get("msg").toString().replace("\"", "")
                        if (status.equals("true")) {
                            val walletamt = responsevalues.body()?.get("wallet_amt").toString()
                                .replace("\"", "")
                            walletpoint.text = "\u20B9 " + walletamt
                            val usernamefound =
                                responsevalues.body()?.get("user_name").toString().replace("\"", "")
                            val tranferstatus =
                                responsevalues.body()?.get("transfer_point_status").toString()
                                    .replace("\"", "")
                            val min_transfer = responsevalues.body()?.get("min_transfer").toString()
                                .replace("\"", "")
                            val max_transfer = responsevalues.body()?.get("max_transfer").toString()
                                .replace("\"", "")
                            transfer_status_point = tranferstatus
                            val cast_min: Int = min_transfer.toInt()
                            val cast_max: Int = max_transfer.toInt()
                            transfer_minamt = cast_min
                            transfer_maxamt = cast_max
                            useraccountfound = usernamefound

                            myHideShowProgress(false)
                        } else {
                            val snak = Snackbar.make(
                                findViewById(android.R.id.content),
                                msg,
                                Snackbar.LENGTH_LONG
                            )
                            val view: View = snak.view
                            val params = view.layoutParams as FrameLayout.LayoutParams
                            params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                            view.layoutParams = params
                            snak.show()
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

    private fun RetrofitCheckwithdrawCondition() {
        val mobilenumber = mobileNumber.text.toString().trim()
        val poointss = transferPoints.text.toString().replace(".", "")
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("mobile_no", mobilenumber)

        RetrofitClient.service.CheckAccountTransferAmtAPI(jsonvalues).enqueue(
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
                            val usernamefound =
                                response.body()?.get("user_name").toString().replace("\"", "")
                            useraccountfound = usernamefound
                            ShowMessageDialog(usernamefound, poointss, mobilenumber).show()
                            myHideShowProgress(false)
                        } else {
                            val snak = Snackbar.make(
                                findViewById(android.R.id.content),
                                msg,
                                Snackbar.LENGTH_LONG
                            )
                            val view: View = snak.view
                            val params = view.layoutParams as FrameLayout.LayoutParams
                            params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                            view.layoutParams = params
                            snak.show()
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

    private fun RetrofitCheckMobileNumberCondition() {
        val mobilenumber = mobileNumber.text.toString().trim()
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("mobile_no", mobilenumber)
        RetrofitClient.service.CheckAccountTransferAmtAPI(jsonvalues).enqueue(
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
                            val usernamefound =
                                response.body()?.get("user_name").toString().replace("\"", "")
                            useraccountfound = usernamefound

                            mobileNumber.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.ic_baseline_check_24,
                                0
                            )

                        } else {
                            mobileNumber.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.ic_baseline_close_mobile_24,
                                0
                            )

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

    private fun RetrofitSendingPoints(mobileno: String, amountstr: String) {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("mobile_no", mobileno)
        jsonvalues.addProperty("amount", amountstr)
        jsonvalues.addProperty("transfer_note", "")
        RetrofitClient.service.TransferuserAmountAPI(jsonvalues).enqueue(
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
                            val temp = walletpoint.text.toString().replace("\u20B9 ", "")
                            var cast_wallet_point: Int = 0
                            cast_wallet_point = temp.toInt()
                            val cast_amt: Int = amountstr.toInt()
                            cast_wallet_point = cast_wallet_point - cast_amt
                            walletpoint.text = "\u20B9 " + cast_wallet_point.toString()
                            val snak = Snackbar.make(
                                findViewById(android.R.id.content),
                                msg,
                                Snackbar.LENGTH_LONG
                            )
                            val view: View = snak.view
                            val params = view.layoutParams as FrameLayout.LayoutParams
                            params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                            view.layoutParams = params
                            snak.show()
                            mobileNumber.text.clear()
                            transferPoints.text.clear()
                            mobileNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

                            myHideShowProgress(false)
                        } else {
                            val snak = Snackbar.make(
                                findViewById(android.R.id.content),
                                msg,
                                Snackbar.LENGTH_LONG
                            )
                            val view: View = snak.view
                            val params = view.layoutParams as FrameLayout.LayoutParams
                            params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                            view.layoutParams = params
                            snak.show()
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

    fun ShowMessageDialog(username: String, points: String, phonenumber: String): AlertDialog {
        val alert = AlertDialog.Builder(this)
        val myview = layoutInflater.inflate(R.layout.custom_dialog_message_tranfer_point, null)
        alert.setView(myview)
        val cancelbut = myview.findViewById<Button>(R.id.btn_cancel)
        val ConfirmBut = myview.findViewById<Button>(R.id.btn_okay)
        val messagenote = myview.findViewById<TextView>(R.id.notetext)
        messagenote.text =
            "Are you sure you want to send $points points to $username ($phonenumber)"
        val alertDialog = alert.create()

        alertDialog.setCanceledOnTouchOutside(false)
        cancelbut.setOnClickListener {
            alertDialog.dismiss()
        }
        ConfirmBut.setOnClickListener {
            RetrofitSendingPoints(phonenumber, points)
            alertDialog.dismiss()
        }
        return alertDialog

    }
}