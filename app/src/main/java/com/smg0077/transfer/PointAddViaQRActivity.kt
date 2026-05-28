package com.smg0077.transfer

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.smg0077.AppDelegate
import com.smg0077.R
import com.smg0077.all_adapter.AddpointHistoryAdapter
import com.smg0077.model.AddpointHistoryDataholder
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.session.MySession
import kotlinx.android.synthetic.main.activity_add_payment_via_qr.addpoint_apppoints
import kotlinx.android.synthetic.main.activity_add_payment_via_qr.addpoint_whatappbut
import kotlinx.android.synthetic.main.activity_add_payment_via_qr.phonepe_note
import kotlinx.android.synthetic.main.activity_add_payment_via_qr.phonepe_note1
import kotlinx.android.synthetic.main.activity_add_payment_via_qr.phonepe_note2
import kotlinx.android.synthetic.main.activity_add_payment_via_qr.qrimage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PointAddViaQRActivity : AppCompatActivity() {
    lateinit var userback: ImageView
    lateinit var progressBar: RelativeLayout
    lateinit var app_key: AppKeyHolderClass
    lateinit var session: MySession
    lateinit var Ac_holder_name: String
    lateinit var radioGroup: RadioGroup
    lateinit var recyclerView: RecyclerView
    lateinit var proceedButton: Button
    lateinit var My_Random_Refrence_number: String
    lateinit var RadioButton: RadioButton
    lateinit var google_RadioButton: RadioButton
    lateinit var paytm_RadioButton: RadioButton


    //    lateinit var phonepe_RadioButton: RadioButton
    lateinit var others_RadioButton: RadioButton
    lateinit var qr_RadioButton: RadioButton
    lateinit var Account_number: String
    lateinit var Upi_payment_id: String
    lateinit var points_ED: TextView
    lateinit var Google_upi_payment_id: String
    lateinit var Phonepay_upi_payment_id: String
    var Min_amt = "10"
    lateinit var Account_Holder_Name: String
    lateinit var Transaction_Amount: String
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    var adapterarrylist: ArrayList<AddpointHistoryDataholder> = ArrayList()
    lateinit var Max_amt: String
    lateinit var Paytment_Google_upi_option: String
    lateinit var Paytment_Phone_upi_option: String
    lateinit var Paytment_Other_upi_option: String
    val GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"

    val PHONEPE_PACKAGE_NAME = "com.phonepe.app"
    val PAYTM_PACKAGE_NAME = "net.one97.paytm"

    val GOOGLE_PAY_REQUEST_CODE = 14574

    val Phonepe_REQUEST_CODE = 65454
    val Paytm_REQUEST_CODE = 65486
    val Other_REQUEST_CODE = 58595

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_payment_via_qr)
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.primarydark)

        initvalues()

        phonepe_note.text = "upi id पर पेमेंट करके एडमिन को व्हाट्सएप (" +  session.getSession_Whatappnumber()+") पर स्क्रीनशॉट भेजे! Make payment on UPI and message screenshot to admin on WhatsApp!."
        phonepe_note1.text = "2 मिनट में पेमेंट ऐड कर दिया जाएगा Payment will be added within 2 minutes."


        swipeRefreshLayout.setOnRefreshListener {
            RetrofitAddedpointhistory()
            swipeRefreshLayout.isRefreshing = false
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            RadioButton = findViewById(checkedId)
//            if (RadioButton.id == R.id.addpoint_others) {
//                othertext.visibility =View.VISIBLE
//                othertext.text= "भुगतान के बाद 10 मिनट तक प्रतीक्षा करें. यदि किसी ग्राहक का बैलेंस 10 मिनट के भीतर वॉलेट में नहीं जुड़ता है तो वह एडमिन को व्हाट्सएप ("+  session.getSession_Whatappnumber()+ ") पर मैसेज करें। Wait for 10 minutes after payment. If any customer's balance is not added to the wallet within 10 minutes then he/she should message the administrator on WhatsApp ("+  session.getSession_Whatappnumber()+ ")."
//            } else {
//                othertext.visibility =View.GONE
//            }
        }

        addpoint_whatappbut.setOnClickListener {
            AppDelegate.openWhatsapp(session.getSession_Whatappnumber(), this)
        }

        proceedButton.setOnClickListener {
            startActivity(Intent(this, UploadQRImageActivity::class.java))
        }

//        proceedButton1.setOnClickListener(View.OnClickListener {
//            if (points_ED.text.isEmpty()) {
//                points_ED.error = "Enter Points"
//                return@OnClickListener
//            }
//            if (radioGroup.checkedRadioButtonId == -1) {
//                Toast.makeText(applicationContext, "select valid option", Toast.LENGTH_LONG).show()
//                return@OnClickListener
//            }
//            val getpoints = points_ED.text.toString()
//
//            val cast_point: Long = getpoints.toLong()
//            val cast_min_amount: Long = Min_amt.toLong()
//            val cast_max_amount: Long = Max_amt.toLong()
//
//
//
//            if (cast_point >= cast_min_amount) {
//                if (cast_point <= cast_max_amount) {
//                    val radiooption = RadioButton.text
//                    Transaction_Amount = getpoints
//
//                    if (radiooption.equals("Google Pay")) {
//                        googlepay_payment()
//                    }
//                    if (radiooption.equals("Paytm")) {
//                        Paytm_payment()
//                    }
//                    if (radiooption.equals("PhonePe")) {
//                        Phonepe_payment()
//                    }
//                    if (radiooption.equals("Other")) {
//                        payUsingUpi()
//                    }
//                } else {
//                    val snack2 = Snackbar.make(
//                        findViewById(android.R.id.content),
//                        "Should be less than maximum amount $Max_amt",
//                        Snackbar.LENGTH_LONG
//                    )
//                    val view: View = snack2.getView()
//                    val params = view.layoutParams as FrameLayout.LayoutParams
//                    params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
//                    view.layoutParams = params
//                    snack2.setBackgroundTint(Color.RED)
//                    snack2.show()
//                }
//
//            } else {
//                val snack2 = Snackbar.make(
//                    findViewById(android.R.id.content),
//                    "Should be greater than minimum amount $Min_amt",
//                    Snackbar.LENGTH_LONG
//                )
//                val view: View = snack2.view
//                val params = view.layoutParams as FrameLayout.LayoutParams
//                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
//                view.layoutParams = params
//                snack2.setBackgroundTint(Color.RED)
//                snack2.show()
//            }
//        })

        userback.setOnClickListener {
            onBackPressed()
        }

        points_ED.setOnClickListener {
            var myClipboard = ContextCompat.getSystemService(
                this,
                ClipboardManager::class.java
            ) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("", points_ED.text)
            myClipboard.setPrimaryClip(clip)
            Toast.makeText(
                this, "UPI:-${points_ED.text} copied",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResume() {
        RetrofitAddedpointhistory()
        super.onResume()
    }

    private fun initvalues() {
        userback = findViewById(R.id.userbackbut)
        progressBar = findViewById(R.id.progressbar2)
        session = MySession(applicationContext)
        radioGroup = findViewById(R.id.addpoint_Radiogroup)
        google_RadioButton = findViewById(R.id.addpoint_googlepay)
        paytm_RadioButton = findViewById(R.id.addpoint_paytm)
//        phonepe_RadioButton = findViewById(R.id.addpoint_phonepay)
        others_RadioButton = findViewById(R.id.addpoint_others)
//        qr_RadioButton = findViewById(R.id.addpoint_qr)
        proceedButton = findViewById(R.id.user_proceed_Button)
        points_ED = findViewById(R.id.addpoint_apppoints)

        radioGroup.isEnabled = false
        swipeRefreshLayout = findViewById(R.id.addpoint_swiprefresh)
        recyclerView = findViewById(R.id.Add_point_recyclerview)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm

        retrofitGetAdminDetais()
        RetrofitGetpaymentList()
        RetrofitAddedpointhistory()
    }


    private fun retrofitGetAdminDetais() {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())

        RetrofitClient.service.AddpointbankdetailsApi(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {

//                        val url = call.request().url()
//                        Log.d("TAG", "URL: $url")

                        val status =
                            response.body()?.get("status").toString().replace("\"", "")
                        response.body()?.get("msg").toString().replace("\"", "")

                        if (status.equals("true")) {
                            val paymentdetailarray =
                                response.body()?.getAsJsonArray("bank_details")
                            val itemsjsonobject: JsonObject
                            itemsjsonobject = paymentdetailarray?.get(0) as JsonObject
                            val ac_holder_name =
                                itemsjsonobject.get("ac_holder_name").toString().replace("\"", "")
                            val account_number =
                                itemsjsonobject.get("account_number").toString().replace("\"", "")
                            val upi_payment_id =
                                itemsjsonobject.get("upi_payment_id").toString().replace("\"", "")
                            val google_upi_payment_id =
                                itemsjsonobject.get("google_upi_payment_id").toString()
                                    .replace("\"", "")
                            val phonepay_upi_payment_id =
                                itemsjsonobject.get("phonepay_upi_payment_id").toString()
                                    .replace("\"", "")
                            Ac_holder_name = ac_holder_name
                            Account_number = account_number
                            Upi_payment_id = upi_payment_id
                            Google_upi_payment_id = google_upi_payment_id
                            Phonepay_upi_payment_id = phonepay_upi_payment_id
                            Account_Holder_Name = ac_holder_name

                            addpoint_apppoints.setText("$Upi_payment_id")

                            val qrCodeBitmap = generateQRCode("upi://pay?pa=$phonepay_upi_payment_id&pn=$ac_holder_name&mc=0000&mode=02&purpose=00")
                            qrimage.setImageBitmap(qrCodeBitmap)


//                           //  println("++++1 "+ Gson().toJson(itemsjsonobject) )

//                           //  println("+++++2 $Ac_holder_name $Account_number $Upi_payment_id  $Google_upi_payment_id $Phonepay_upi_payment_id")

                            myHideShowProgress(false)
                        } else {
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

    private fun generateQRCode(text: String): Bitmap? {
        val size = 600 // Size of the QR code
        val qrCodeWriter = QRCodeWriter()
        return try {
            val bitMatrix: BitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
            bmp
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    private fun RetrofitGetpaymentList() {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())

        RetrofitClient.service.Addpoint_lastpointrequest_Api(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                @RequiresApi(Build.VERSION_CODES.M)
                @SuppressLint("LongLogTag")
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {

                        val min_amt =
                            response.body()?.get("min_amt").toString().replace("\"", "")

                        val max_amt =
                            response.body()?.get("max_amt").toString().replace("\"", "")
                        phonepe_note2.text = "Pay Minimum $min_amt Rupees."
//                         println("+++++01"+ min_amt)
//                         println("+++++02"+ max_amt)

                        val status =
                            response.body()?.get("status").toString().replace("\"", "")
                        val google_upi_option =
                            response.body()?.get("google_upi_option").toString()
                                .replace("\"", "")
                        val phone_upi_option =
                            response.body()?.get("phone_upi_option").toString()
                                .replace("\"", "")
                        val other_upi_option =
                            response.body()?.get("other_upi_option").toString()
                                .replace("\"", "")

                        if (status.equals("true")) {
                            val paymentdetailarray =
                                response.body()?.getAsJsonArray("last_req_detail")
                            var itemsjsonobject = JsonObject()
                            itemsjsonobject = paymentdetailarray?.get(0) as JsonObject
                        }
                        Min_amt = min_amt
                        Max_amt = max_amt
                        Paytment_Google_upi_option = google_upi_option
                        Paytment_Phone_upi_option = phone_upi_option
                        Paytment_Other_upi_option = other_upi_option
                        addRadioButton_data(google_upi_option, phone_upi_option, other_upi_option)
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

    private fun RetrofitAddMoneyApicall(
        getAmount: String,
        getTransID: String,
        getTransrefrence: String,
        getGpay: String,
        getPhonepe: String
    ) {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("amount", getAmount)
        jsonvalues.addProperty("txn_id", getTransID)
        jsonvalues.addProperty("txn_ref", getTransrefrence)
        jsonvalues.addProperty("request_amount", getTransID)
        if (getGpay == "1") {
            jsonvalues.addProperty("upigpay", "1")
            jsonvalues.addProperty("upiphonepe", "0")
            jsonvalues.addProperty("otherupi", "0")
        } else if (getPhonepe == "1") {
            jsonvalues.addProperty("upigpay", "0")
            jsonvalues.addProperty("upiphonepe", "1")
            jsonvalues.addProperty("otherupi", "0")
        } else {
            jsonvalues.addProperty("upigpay", "0")
            jsonvalues.addProperty("upiphonepe", "0")
            jsonvalues.addProperty("otherupi", "1")
        }
        RetrofitClient.service.Addpoint_pointAdded_Api(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val messageGet =
                            response.body()?.get("msg").toString().replace("\"", "")
                        val statusGet =
                            response.body()?.get("status").toString().replace("\"", "")

                        if (statusGet.equals("true")) {
                            val snack2 = Snackbar.make(
                                findViewById(android.R.id.content),
                                messageGet,
                                Snackbar.LENGTH_LONG
                            )
                            val view: View = snack2.view
                            val params = view.layoutParams as FrameLayout.LayoutParams
                            params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                            view.layoutParams = params
                            snack2.setBackgroundTint(Color.GREEN)
                            snack2.show()

                        } else {
                            val snack2 = Snackbar.make(
                                findViewById(android.R.id.content),
                                messageGet,
                                Snackbar.LENGTH_LONG
                            )
                            val view: View = snack2.view
                            val params = view.layoutParams as FrameLayout.LayoutParams
                            params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                            view.layoutParams = params
                            snack2.setBackgroundTint(Color.RED)
                            snack2.show()
                        }
                        (radioGroup.getChildAt(0) as RadioButton).isChecked = false
                        (radioGroup.getChildAt(1) as RadioButton).isChecked = false
                        (radioGroup.getChildAt(2) as RadioButton).isChecked = false
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

    @RequiresApi(Build.VERSION_CODES.M)
    fun addRadioButton_data(
        google_upi_option: String,
        phone_upi_option: String,
        other_upi_option: String
    ) {
        if (google_upi_option.equals("1") || phone_upi_option.equals("1") || other_upi_option.equals(
                "1"
            )
        ) {
            radioGroup.isEnabled = true
            (radioGroup.getChildAt(0) as RadioButton).isEnabled = google_upi_option.equals("1")
            if (google_upi_option.equals("0")) {
                (radioGroup.getChildAt(0) as RadioButton).backgroundTintList =
                    ColorStateList.valueOf(getColor(R.color.gray))
                (radioGroup.getChildAt(0) as RadioButton).setTextColor(getColor(R.color.gray))
            }
            if (phone_upi_option.equals("0")) {
                (radioGroup.getChildAt(1) as RadioButton).backgroundTintList =
                    ColorStateList.valueOf(getColor(R.color.gray))
                (radioGroup.getChildAt(1) as RadioButton).setTextColor(getColor(R.color.gray))
            }
            if (other_upi_option.equals("0")) {
                (radioGroup.getChildAt(2) as RadioButton).backgroundTintList =
                    ColorStateList.valueOf(getColor(R.color.gray))
                (radioGroup.getChildAt(2) as RadioButton).setTextColor(getColor(R.color.gray))
            }

            (radioGroup.getChildAt(1) as RadioButton).isEnabled = phone_upi_option.equals("1")
            (radioGroup.getChildAt(2) as RadioButton).isEnabled = other_upi_option.equals("1")
        }

    }

    private fun googlepay_payment() {
        val paytmPackageInstalled = isAppInstalled(GOOGLE_PAY_PACKAGE_NAME)
        if (paytmPackageInstalled) {
            if (Account_Holder_Name.isEmpty()) {
                Account_Holder_Name = resources.getString(R.string.app_name)
            }
            My_Random_Refrence_number = getRandomString(14)
            val withrawAmount = points_ED.text.toString()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = getUpiPaymentUri(
                Account_Holder_Name,
                Google_upi_payment_id,
                "Paying to $Account_Holder_Name",
                withrawAmount,
                My_Random_Refrence_number
            )
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME)
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE)
        } else {
            Toast.makeText(this, "Google Pay app is not installed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun Phonepe_payment() {
        val paytmPackageInstalled = isAppInstalled(PHONEPE_PACKAGE_NAME)

        if (paytmPackageInstalled) {
            if (Account_Holder_Name.isEmpty()) {
                Account_Holder_Name = resources.getString(R.string.app_name)
            }
            My_Random_Refrence_number = getRandomString(14)
            val withrawAmount = points_ED.text.toString()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = getUpiPaymentUri(
                Account_Holder_Name,
                Phonepay_upi_payment_id,
                "Paying to $Account_Holder_Name",
                withrawAmount,
                My_Random_Refrence_number
            )
            intent.setPackage(PHONEPE_PACKAGE_NAME)
            startActivityForResult(intent, Phonepe_REQUEST_CODE)
        } else {
            // Paytm app is not installed, show a toast message
            Toast.makeText(this, "Phonepe app is not installed", Toast.LENGTH_SHORT).show()
        }
    }


    private fun Paytm_payment() {
        val paytmPackageInstalled = isAppInstalled(PAYTM_PACKAGE_NAME)

        if (paytmPackageInstalled) {
            if (Account_Holder_Name.isEmpty()) {
                Account_Holder_Name = resources.getString(R.string.app_name)
            }
            My_Random_Refrence_number = getRandomString(14)
            val withrawAmount = points_ED.text.toString()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = getUpiPaymentUri(
                Account_Holder_Name,
                Phonepay_upi_payment_id,
                "Paying to $Account_Holder_Name",
                withrawAmount,
                My_Random_Refrence_number
            )
            intent.setPackage(PAYTM_PACKAGE_NAME)
            startActivityForResult(intent, Paytm_REQUEST_CODE)
        } else {
            // Paytm app is not installed, show a toast message
            Toast.makeText(this, "Paytm app is not installed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        try {
            packageManager.getPackageInfo(packageName, 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    private fun payUsingUpi() {
        if (Account_Holder_Name.isEmpty()) {
            Account_Holder_Name = resources.getString(R.string.app_name)
        }
        My_Random_Refrence_number = getRandomString(14)
        val withrawAmount = points_ED.text.toString()
        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        val uriparse = getUpiPaymentUri(
            Account_Holder_Name,
            Upi_payment_id,
            "Paying to $Account_Holder_Name",
            withrawAmount,
            My_Random_Refrence_number
        )
        upiPayIntent.data = Uri.parse(uriparse.toString())
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")
        if (null != chooser.resolveActivity(packageManager)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivityForResult(chooser, Other_REQUEST_CODE, null)
            } else {
                Toast.makeText(
                    this,
                    "Does not support on your phone",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                this,
                "No UPI app found, please install one to continue",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    fun isConnectionAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val netInfo = connectivityManager.activeNetworkInfo
            if (netInfo != null && netInfo.isConnected
                && netInfo.isConnectedOrConnecting
                && netInfo.isAvailable
            ) {
                return true
            }
        }
        return false
    }

    private fun getUpiPaymentUri(
        name: String,
        upiId: String,
        transactionNote: String,
        amount: String,
        Reference_id: String
    ): Uri? {
        return Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", upiId)       // virtual ID
            .appendQueryParameter("pn", name)          // name
            .appendQueryParameter("tn", transactionNote)       // any note about payment
            .appendQueryParameter("am", amount)           // amount
            .appendQueryParameter("cu", "INR")                         // currency
            .build()
//            .appendQueryParameter("mc", Reference_id)          // optional
//            .appendQueryParameter("tr", Reference_id)     // optional
//            .appendQueryParameter("url", "https://www.kalyanapp.in")
    }

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    @SuppressLint("LongLogTag")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result_status = data?.getStringExtra("Status").toString()
        val respnse = data?.extras.toString()

//          println("++++ $respnse")

        if (respnse.isNullOrBlank() || respnse.isBlank() || respnse.isEmpty()) {
            val snack2 = Snackbar.make(
                findViewById(android.R.id.content),
                "Payment Failed!",
                Snackbar.LENGTH_LONG
            )
            val view: View = snack2.view
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
            view.layoutParams = params
            snack2.setBackgroundTint(Color.RED)
            snack2.show()
        }



        if (result_status == "SUCCESS" || result_status == "Success") {
            if (requestCode == GOOGLE_PAY_REQUEST_CODE || requestCode == Paytm_REQUEST_CODE || requestCode == Phonepe_REQUEST_CODE|| requestCode == Other_REQUEST_CODE) {
                val transactionRefrence = data?.getStringExtra("txnRef").toString()
                val transactionId = data?.getStringExtra("txnId").toString()
                //  println("+++++1+ $transactionRefrence $transactionId")
                //  println("+++++1+ $result_status ")
                if (requestCode == GOOGLE_PAY_REQUEST_CODE) {
                    RetrofitAddMoneyApicall(
                        Transaction_Amount,
                        transactionId,
                        transactionRefrence,
                        "1",
                        "0"
                    )
                }
                if (requestCode == Paytm_REQUEST_CODE) {
                    RetrofitAddMoneyApicall(
                        Transaction_Amount,
                        transactionId,
                        transactionRefrence,
                        "0",
                        "1"
                    )

                }
                if (requestCode == Phonepe_REQUEST_CODE) {
                    RetrofitAddMoneyApicall(
                        Transaction_Amount,
                        transactionId,
                        transactionRefrence,
                        "0",
                        "1"
                    )

                }
                if (requestCode == Other_REQUEST_CODE) {
                    RetrofitAddMoneyApicall(
                        Transaction_Amount,
                        transactionId,
                        transactionRefrence,
                        "0",
                        "1"
                    )
                }

            }

        } else if (result_status == "FAILURE" || result_status == "Failure" || result_status == "Failed") {
            val snack2 = Snackbar.make(
                findViewById(android.R.id.content),
                "Payment Failed",
                Snackbar.LENGTH_LONG
            )
            val view: View = snack2.view
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
            view.layoutParams = params
            snack2.setBackgroundTint(Color.RED)
            snack2.show()
        } else {
            val snack2 = Snackbar.make(
                findViewById(android.R.id.content),
                "Something went wrong contact admin!",
                Snackbar.LENGTH_LONG
            )
            val view: View = snack2.view
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
            view.layoutParams = params
            snack2.setBackgroundTint(Color.GRAY)
            snack2.show()
        }

    }


    private fun RetrofitAddedpointhistory() {
        adapterarrylist.clear()
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())

        RetrofitClient.service.Addpoint_pointAdded_HistoryApi(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    adapterarrylist.clear()
                    if (response.isSuccessful) {
                        val status = response.body()?.get("status").toString().replace(
                            "\"",
                            ""
                        )
                        val msg = response.body()?.get("msg").toString().replace(
                            "\"",
                            ""
                        )
                        if (status.equals("true")) {

                            var gameObject: JsonObject
                            val gamerateArrayObject =
                                response.body()?.getAsJsonArray("result")!!
                            var i = 0
                            while (i < gamerateArrayObject.size()) {
                                gameObject = gamerateArrayObject[i] as JsonObject
                                i++

                                val amount = gameObject.get("amount").toString().replace("\"", "")
                                val txn_id = gameObject.get("txn_id").toString().replace("\"", "")
                                val payment_method =
                                    gameObject.get("payment_method").toString().replace("\"", "")
                                val insert_date =
                                    gameObject.get("insert_date").toString().replace("\"", "")
                                val point_status =
                                    gameObject.get("fund_status").toString().replace("\"", "")
                                val deposit_type =
                                    gameObject.get("deposit_type").toString().replace("\"", "")
                                val reject_remark =
                                    gameObject.get("reject_remark").toString().replace("\"", "")

                                adapterarrylist.add(
                                    AddpointHistoryDataholder(
                                        amount,
                                        txn_id,
                                        payment_method,
                                        insert_date,
                                        point_status,
                                        deposit_type,
                                        reject_remark
                                    )
                                )
                            }
                            val adaptercalling = AddpointHistoryAdapter(
                                applicationContext,
                                adapterarrylist
                            )

                            adaptercalling.notifyDataSetChanged()
                            recyclerView.adapter = adaptercalling

                            myHideShowProgress(false)
                        } else {
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                msg,
                                Snackbar.LENGTH_LONG
                            ).show()
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

}