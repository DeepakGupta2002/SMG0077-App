package com.smg0077

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.denzcoskun.imageslider.BuildConfig
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import com.smg0077.all_adapter.HomeRecyclerAdapter
import com.smg0077.all_adapter.HomeRecyclerHideModeAdapter
import com.smg0077.bating.BatTimeActivity
import com.smg0077.bating.ToperHistoryActivity
import com.smg0077.common.*
import com.smg0077.login.LoginActivity
import com.smg0077.login.OtpVerifyActivity
import com.smg0077.model.gamedataholder
import com.smg0077.profile.PasswordBadloActivity
import com.smg0077.profile.ProfileDetails
import com.smg0077.realtask.GalidesawarGameActivity
import com.smg0077.realtask.RealStarlineActivity
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.session.MySession
import com.smg0077.transfer.PointAddViaQRActivity
import com.smg0077.transfer.PointTransferActivity
import com.smg0077.transfer.PointWithdrawActivity
import com.smg0077.transfer.UserWalletActivity
import com.smg0077.web.ChartDraw
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.content_home_layout.linearmain
import kotlinx.android.synthetic.main.content_home_layout.walletLinear
import kotlinx.android.synthetic.main.content_home_layout.withdraw_point
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    AdvancedWebView.Listener {

    var updateMessage: String = "none"
    var updateLinkBtn: String = "none"
    var updateActionBtn: String = "none"
    private lateinit var checkUpdateFlag: CheckUpdateFlag
    private var appVersion: String = "empty"
    var appCurrentVersion: String = "empty"
    var appMinimumVersion: String = "empty"
    lateinit var menuItemHome: RelativeLayout
    private lateinit var profileNavigationItem: RelativeLayout
    lateinit var menuItemAddpoints: LinearLayout
    lateinit var menuItemWithdrawpoints: LinearLayout
    lateinit var menuItemTransferpoints: LinearLayout
    private lateinit var menuJoinTelegram: LinearLayout
    lateinit var menuItempaytm: RelativeLayout
    lateinit var menuItemphonepe: RelativeLayout
    lateinit var menuItemgpay: RelativeLayout
    lateinit var btnmenu: RelativeLayout
    lateinit var btnmenuhide: RelativeLayout
    lateinit var othergamebtn: LinearLayout
    lateinit var othergamebtnhide: LinearLayout
    lateinit var walletNavigationItem: LinearLayout
    lateinit var winHistoryNavigationItem: LinearLayout
    lateinit var bidHistoryNavigationItem: LinearLayout
    lateinit var gameRateNavigationItem: RelativeLayout
    private lateinit var guessingFromNavigationItem: RelativeLayout
    lateinit var shareNavigationItem: RelativeLayout
    lateinit var contactUsNavigationItem: RelativeLayout
    lateinit var ratingNavigationItem: RelativeLayout
    lateinit var changePasswordNavigationItem: RelativeLayout
    lateinit var logoutNavigationItem: RelativeLayout
    lateinit var howToPlayNavigationItem: RelativeLayout
    lateinit var gameChartNavigationItem: RelativeLayout
    lateinit var naviUserFirstLetter: TextView
    lateinit var homesliderimage: ImageSlider
    lateinit var checkDeviceId: String
    var checkSecurityPin: String = "null"
    var checkLogoutStatus: String = "null"
    var deviceArrayList: MutableList<String> = ArrayList()


    var transferAmountStatus = ""
    private lateinit var waNumberGetIntent: String
    var Betting_status = ""
    var Share_msg = ""
    var appLink = ""
    private var telegramLink = ""
    var registernumberget_Paytm: String = ""
    var registernumberget_Googlepay: String = ""
    var registernumberget_phonepe: String = ""
    private var upiCode: Int = 0
    lateinit var phonenumerget: String
    lateinit var maxWithdraw: String
    lateinit var minTransfer: String
    lateinit var Max_Transfer: String
    lateinit var Min_Withdraw: String
    var whatsapp_mobilenumber: String = ""
    var accountBlockStatus = ""
    var withdrowStatus = ""
    lateinit var maintainMessage: String
    lateinit var maintainStatus: String
    lateinit var callButton: AppCompatButton
    lateinit var home_whatappbuthide: AppCompatButton
    lateinit var swipetorefresh: SwipeRefreshLayout
    lateinit var swipetorefreshhide: SwipeRefreshLayout
    lateinit var playStarlineBUT: AppCompatButton
    lateinit var realplayStarlineBUT: AppCompatButton
    lateinit var toolbar: Toolbar
    lateinit var progressBar: RelativeLayout
    lateinit var naviUserName: TextView
    private lateinit var naviUserPhone: TextView

    private lateinit var txtMarquee: TextView
    private lateinit var bidHistory: LinearLayout
    private lateinit var menu_home: RelativeLayout
    lateinit var wanumberButton: LinearLayout
    lateinit var addpointButton: LinearLayout

    lateinit var userpointtxttop: TextView
    lateinit var walleticon: ImageView
    lateinit var notificationicon: ImageView
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var recyclerView: RecyclerView
    var adapterArrayList: ArrayList<gamedataholder> = ArrayList()
    lateinit var app_key: AppKeyHolderClass
    lateinit var session: MySession
    var imagearry = ArrayList<SlideModel>()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private val notificationChannelId = "notification"

    var globalbattingstatus = ""

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, you can now send notifications
            } else {
                // Permission denied, you can't send notifications
            }
        }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.primarydark)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        session = MySession(applicationContext)

        checkDeviceId = Settings.Secure.getString(
            applicationContext.contentResolver, Settings.Secure.ANDROID_ID
        )
        checkUpdateFlag = CheckUpdateFlag(true)
        appVersion = BuildConfig.VERSION_NAME

        initValues()
        retrofitImageslider()
        retrofitgamedata()

        FirebaseApp.initializeApp(this)
        firebaseAnalytics = Firebase.analytics
        createNotificationChannel()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // We don't have the permission, request it
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // We already have the permission, do whatever you need to do
        }

        getToken()

//        FirebaseMessaging.getInstance().subscribeToTopic("Notification")

        walletLinear.setOnClickListener {
            val intent = Intent(this, UserWalletActivity::class.java)
            intent.putExtra("transfer_status", transferAmountStatus)
            intent.putExtra("withdraw_status", withdrowStatus)
            startActivity(intent)
        }
        profileNavigationItem.setOnClickListener {
            startActivity(Intent(this, ProfileDetails::class.java))
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        walletNavigationItem.setOnClickListener {
            val intent = Intent(this, UserWalletActivity::class.java)
            intent.putExtra("transfer_status", transferAmountStatus)
            intent.putExtra("withdraw_status", withdrowStatus)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        menuItemWithdrawpoints.setOnClickListener {
            val intent = Intent(this, PointWithdrawActivity::class.java)
            intent.putExtra("min_transfer", minTransfer)
            intent.putExtra("max_transfer", Max_Transfer)
            intent.putExtra("withdraw_status", withdrowStatus)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        withdraw_point.setOnClickListener {
            val intent = Intent(this, PointWithdrawActivity::class.java)
            intent.putExtra("min_transfer", minTransfer)
            intent.putExtra("max_transfer", Max_Transfer)
            intent.putExtra("withdraw_status", withdrowStatus)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        menuItemTransferpoints.setOnClickListener {
            if (transferAmountStatus == "0") {
                Toast.makeText(
                    this@HomeActivity,
                    "Transfer is not Available at this moment, Please Contact to Admin",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, PointTransferActivity::class.java)
                intent.putExtra("min_transfer", minTransfer)
                intent.putExtra("max_transfer", Max_Transfer)
                intent.putExtra("transfer_status", transferAmountStatus)
                startActivity(intent)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        menuItempaytm.setOnClickListener {
            val phonenum = showdialog("1")
            phonenum.show()
        }

        menuItemphonepe.setOnClickListener {
            val phonenum = showdialog("3")
            phonenum.show()
        }

        menuItemgpay.setOnClickListener {
            val phonenum = showdialog("2")
            phonenum.show()
        }
        menuJoinTelegram.setOnClickListener {
            AppDelegate.openTelegramLink(this, telegramLink)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        winHistoryNavigationItem.setOnClickListener {
            val intent = Intent(this, ToperHistoryActivity::class.java)
            intent.putExtra("history_win", "home")
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        bidHistoryNavigationItem.setOnClickListener {
            val intent = Intent(this, BatTimeActivity::class.java)
            intent.putExtra("history_bid", "home")
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        gameRateNavigationItem.setOnClickListener {
            startActivity(Intent(this, GameRates::class.java))
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        guessingFromNavigationItem.setOnClickListener {
            startActivity(Intent(this, GuessingFormActivity::class.java))
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        shareNavigationItem.setOnClickListener {
            AppDelegate.openWebPage(this, "https://matka.justneed.in/")

//            val message =
//                "Download *Kalyan App* " + "https://play.google.com/store/apps/details?id=" + packageName
//            try {
//                val sharingIntent =
//                    Intent(Intent.ACTION_SEND)
//                sharingIntent.type = "text/plain"
//                sharingIntent.putExtra(Intent.EXTRA_TEXT, message)
//                startActivity(
//                    Intent.createChooser(sharingIntent, "Share Via")
//                )
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }


//            val shareintent = Intent()
//            shareintent.action = Intent.ACTION_SEND
//            shareintent.type = "text/plain"
//            shareintent.putExtra(Intent.EXTRA_TEXT, Share_msg)
//            startActivity(Intent.createChooser(shareintent, "Share Via"))
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        contactUsNavigationItem.setOnClickListener {
            startActivity(Intent(this, ContactUs::class.java))
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        ratingNavigationItem.setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(appLink))
            )
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        changePasswordNavigationItem.setOnClickListener {
            startActivity(Intent(this, PasswordBadloActivity::class.java))
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        howToPlayNavigationItem.setOnClickListener {
            startActivity(Intent(this, HowtoplayActivity::class.java))
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        gameChartNavigationItem.setOnClickListener {
            startActivity(Intent(this, ChartDraw::class.java))
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        logoutNavigationItem.setOnClickListener {
            AlertDialog.Builder(this).setMessage("Are you sure want to logout?")
                .setCancelable(false)
                .setPositiveButton(
                    "Yes"
                ) { _, _ ->
                    session.setLogin(false)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finishAffinity()
                }.setNegativeButton("No", null).show()
        }

        swipetorefresh.setOnRefreshListener {
            retrofitgamedata()
            swipetorefresh.isRefreshing = false
        }
        swipetorefreshhide.setOnRefreshListener {
            retrofitgamedata()
            swipetorefreshhide.isRefreshing = false
        }
        callButton.setOnClickListener {
            callfuntion()
        }
        wanumberButton.setOnClickListener {
            AppDelegate.openWhatsapp(whatsapp_mobilenumber, this@HomeActivity)
        }

        home_whatappbuthide.setOnClickListener {
            AppDelegate.openWhatsapp(whatsapp_mobilenumber, this@HomeActivity)
        }

        walleticon.setOnClickListener {
            val intent = Intent(this, UserWalletActivity::class.java)
            intent.putExtra("transfer_status", transferAmountStatus)
            intent.putExtra("withdraw_status", withdrowStatus)
            startActivity(intent)
        }
        playStarlineBUT.setOnClickListener {
            val intent = Intent(this, GalidesawarGameActivity::class.java)
            startActivity(intent)
        }
        realplayStarlineBUT.setOnClickListener {
            val intent = Intent(this, RealStarlineActivity::class.java)
            startActivity(intent)
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


        val notificationSwitch = findViewById<Switch>(R.id.notificationSwitch)


        val notificationStatus = session.getNotificationStatus()

        // Set the initial state of the switch
        notificationSwitch.isChecked = notificationStatus

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                session.setNotification_Status(isChecked)
                sendNotificationcontrol("1")
                AppDelegate.showToast(this, "Notification Enabled")
            } else {
                session.setNotification_Status(false)
                sendNotificationcontrol("0")
                AppDelegate.showToast(this, "Notification Disabled")
            }


        }
    }

    override fun onResume() {
        retrofitCheckNumber()
        retrofitRefreshAmt()
        val firstLetter = session.getSession_username().substring(0, 1)
        naviUserFirstLetter.text = firstLetter
        naviUserName.text = session.getSession_username()
        retrofitgamedata()
        super.onResume()
    }


    private fun retrofitImageslider() {
        imagearry.clear()
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)

        RetrofitClient.service.SliderimageApi(jsonvalues).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>, response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    val status = response.body()?.get("status").toString().replace(
                        "\"", ""
                    )

                    if (status.equals("true")) {
                        var SliderObject: JsonObject
                        val gamerateArrayObject =
                            response.body()?.getAsJsonArray("sliderdata")!!
                        var i = 0
                        while (i < gamerateArrayObject.size()) {
                            SliderObject = gamerateArrayObject[i] as JsonObject
                            i++
                            val Image_url = SliderObject.get("slider_image").toString().replace(
                                "\"", ""
                            )

                            imagearry.add(SlideModel(Image_url, ScaleTypes.FIT))
                        }
                        homesliderimage.setImageList(imagearry)
                        myHideShowProgress(false)
                    } else {
                        Toast.makeText(
                            applicationContext, "No Record Found!", Toast.LENGTH_LONG
                        ).show()
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

    private fun callfuntion() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + whatsapp_mobilenumber)
        startActivity(intent)
    }

    private fun initValues() {
        waNumberGetIntent = session.getSession_Whatappnumber()

        swipetorefresh = findViewById(R.id.homeswipe_refresh)
        swipetorefreshhide = findViewById(R.id.homeswipe_refreshhide)
        drawerLayout = findViewById(R.id.drawer_layout)
        progressBar = findViewById(R.id.progressbar2)
        notificationicon = findViewById(R.id.notificationicon)
        navigationView = findViewById(R.id.nav_view)
        recyclerView = findViewById(R.id.myhomerecyclerview)
        recyclerView = findViewById(R.id.gameHidetTV)
        playStarlineBUT = findViewById(R.id.galiDesawarBut)
        realplayStarlineBUT = findViewById(R.id.starlineBut)
        walleticon = findViewById(R.id.walleticon)
        userpointtxttop = findViewById(R.id.userpointtxttop)
        homesliderimage = findViewById(R.id.home_image_slider)
        bidHistory = findViewById(R.id.bidHistory)
        menu_home = findViewById(R.id.menu_home)
        addpointButton = findViewById(R.id.home_add_point)
        wanumberButton = findViewById(R.id.home_whatappbut)
        btnmenu = findViewById(R.id.btnmenu)
        btnmenuhide = findViewById(R.id.btnmenuhide)
        othergamebtn = findViewById(R.id.othergamebtn)
        othergamebtnhide = findViewById(R.id.othergamebtnhide)
        home_whatappbuthide = findViewById(R.id.home_whatappbuthide)
        txtMarquee = findViewById(R.id.marqueeText)



        profileNavigationItem =
            navigationView.findViewById(R.id.menuItemUserProfile)


        menuItemHome = navigationView.findViewById(R.id.menuItemHome)
        menuItemAddpoints = navigationView.findViewById(R.id.menuItemAddpoints)
        menuItemWithdrawpoints = navigationView.findViewById(R.id.menuItemWithdrawpoints)
        menuItemTransferpoints = navigationView.findViewById(R.id.menuItemTransferpoints)
        menuJoinTelegram = navigationView.findViewById(R.id.menuJointelegram)
        menuItempaytm = navigationView.findViewById(R.id.menuItempaytm)
        menuItemphonepe = navigationView.findViewById(R.id.menuItemphonepe)
        menuItemgpay = navigationView.findViewById(R.id.menuItemgpay)

        walletNavigationItem = navigationView.findViewById(R.id.menuItemUserWallet)
        winHistoryNavigationItem =
            navigationView.findViewById(R.id.menuItemUserWinHistory)
        bidHistoryNavigationItem =
            navigationView.findViewById(R.id.menuItemBidHistory)
        gameRateNavigationItem = navigationView.findViewById(R.id.menuItemGameRate)
        guessingFromNavigationItem = navigationView.findViewById(R.id.menuItemGuessingFrom)
        shareNavigationItem = navigationView.findViewById(R.id.menuItemShare)
        contactUsNavigationItem = navigationView.findViewById(R.id.menuItemContact)
        ratingNavigationItem = navigationView.findViewById(R.id.menuItemRating)
        changePasswordNavigationItem =
            navigationView.findViewById(R.id.menuItemChangePassword)
        logoutNavigationItem = navigationView.findViewById(R.id.menuItemLogout)
        howToPlayNavigationItem =
            navigationView.findViewById(R.id.menuItemKseKhleGame)
        gameChartNavigationItem =
            navigationView.findViewById(R.id.menuItemGameChart)
        naviUserFirstLetter = navigationView.findViewById(R.id.userFirstLetter)

        naviUserName = navigationView.findViewById(R.id.nav_usernametxt)
        naviUserPhone = navigationView.findViewById(R.id.nav_phonenumbertxt)
        naviUserName.text = session.getSession_username()
        naviUserPhone.text = session.getSession_usermobile()
        naviUserPhone.text = session.getSession_usermobile()

        callButton = findViewById(R.id.home_callbut)

        txtMarquee.isSelected = true
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )

        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.white)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)


        realplayStarlineBUT.visibility = View.INVISIBLE


        winHistoryNavigationItem.visibility = View.GONE
        bidHistoryNavigationItem.visibility = View.GONE
        gameRateNavigationItem.visibility = View.GONE
    }

    private fun checkConditions() {
        if (maintainStatus == "1") {
            val intent = Intent(this, UnderMaintenance::class.java)
            intent.putExtra("message", maintainMessage)
            startActivity(intent)
        }

        if (accountBlockStatus == "0") {
            session.setLogin(false)
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }

        if (globalbattingstatus == "0") {
            addpointButton.visibility= View.GONE
            menuItemAddpoints.visibility= View.GONE
            menuItemWithdrawpoints.visibility= View.GONE
            playStarlineBUT.visibility= View.VISIBLE
            walleticon.visibility = View.GONE
            menuItemTransferpoints.visibility = View.GONE
            withdraw_point.visibility = View.GONE
            walletNavigationItem.visibility = View.GONE
            walleticon.visibility = View.GONE
            userpointtxttop.visibility = View.GONE
        } else {
            walleticon.visibility = View.VISIBLE
            addpointButton.visibility= View.VISIBLE
            menuItemAddpoints.visibility= View.VISIBLE
            playStarlineBUT.visibility= View.GONE
            menuItemWithdrawpoints.visibility= View.VISIBLE
            menuItemTransferpoints.visibility = View.VISIBLE
            withdraw_point.visibility = View.VISIBLE
            walletNavigationItem.visibility = View.VISIBLE
            walleticon.visibility = View.VISIBLE
            userpointtxttop.visibility = View.VISIBLE

            addpointButton.setOnClickListener {
                startActivity(Intent(this, PointAddViaQRActivity::class.java))
            }

            menuItemAddpoints.setOnClickListener {
                startActivity(Intent(this, PointAddViaQRActivity::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            menuItemHome.setOnClickListener {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }

        println("++++++Betting_status  "+ Betting_status)

              if (Betting_status == "1") {
        howToPlayNavigationItem.visibility = View.GONE
        realplayStarlineBUT.visibility = View.VISIBLE
        playStarlineBUT.visibility = View.VISIBLE
        notificationicon.visibility = View.GONE

        menuItempaytm.visibility = View.VISIBLE
        menuItemphonepe.visibility = View.VISIBLE
        menuItemgpay.visibility = View.VISIBLE





        winHistoryNavigationItem.visibility = View.VISIBLE
        bidHistoryNavigationItem.visibility = View.VISIBLE
        gameRateNavigationItem.visibility = View.VISIBLE
        gameChartNavigationItem.visibility = View.GONE
//        withdraw_point.visibility = View.VISIBLE

        walletLinear.visibility = View.VISIBLE
        btnmenu.visibility = View.VISIBLE
                  btnmenuhide.visibility = View.GONE
                  othergamebtn.visibility = View.VISIBLE
                  swipetorefresh.visibility = View.VISIBLE
                  othergamebtnhide.visibility = View.GONE
       } else {
            addpointButton.visibility = View.GONE
            howToPlayNavigationItem.visibility = View.GONE
            playStarlineBUT.visibility = View.INVISIBLE
            realplayStarlineBUT.visibility = View.INVISIBLE
            menuItemAddpoints.visibility = View.GONE
            menuItemTransferpoints.visibility = View.GONE
            menuItemWithdrawpoints.visibility = View.GONE
            notificationicon.visibility = View.GONE
            menuItempaytm.visibility = View.GONE
            menuItemphonepe.visibility = View.GONE
            menuItemgpay.visibility = View.GONE

            walletNavigationItem.visibility = View.GONE
            winHistoryNavigationItem.visibility = View.GONE
            bidHistoryNavigationItem.visibility = View.GONE
            gameRateNavigationItem.visibility = View.GONE

            walleticon.visibility = View.GONE
            userpointtxttop.visibility = View.GONE
            gameChartNavigationItem.visibility = View.GONE
            btnmenu.visibility = View.GONE
            btnmenuhide.visibility = View.VISIBLE
                  othergamebtnhide.visibility = View.VISIBLE
                  swipetorefresh.visibility = View.GONE
                  swipetorefreshhide.visibility = View.VISIBLE
                  othergamebtn.visibility = View.GONE

       }

        if (checkLogoutStatus == "1") {
            logoutSession()
        }
        if (checkSecurityPin == "1") {
            logoutSession()
        }
        if (!deviceArrayList.contains(checkDeviceId)) {
            logoutSession()
        }

        val floatCurrentVersion = appCurrentVersion.replace(".", "").toInt()
        val floatAppVersion = appVersion.replace(".", "").toInt()
        val floatAppMinimumVersion = appMinimumVersion.replace(".", "").toInt()

        if (floatAppVersion < floatAppMinimumVersion) {
//            val show = showUpdateDialog("Hide")
//            show.show()

        } else if (floatCurrentVersion > floatAppVersion) {
            if (checkUpdateFlag.check) {
                checkUpdateFlag = CheckUpdateFlag(false)
                val show = showUpdateDialog("show")
                show.show()
            }

        }

    }

    private fun logoutSession() {
        session.setLogin(false)
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun retrofitgamedata() {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())

        RetrofitClient.service.getdashboardData(jsonvalues).enqueue(object : Callback<JsonObject> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<JsonObject>, response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {



                    val status = response.body()?.get("status").toString().replace(
                        "\"", ""
                    )

                    if (status.equals("true")) {
                        val walletamt =
                            response.body()?.get("wallet_amt").toString().replace("\"", "")
                        userpointtxttop.text = "\u20B9 $walletamt"
                        val mobilenumber =
                            response.body()?.get("mobile_no").toString().replace(
                                "\"", ""
                            )

                        whatsapp_mobilenumber = mobilenumber
                        session.setSession_Whatappnumber(whatsapp_mobilenumber)
                        telegramLink = response.body()?.get("telegram_no").toString().replace("\"", "")

                        globalbattingstatus = response.body()?.get("global_batting_status")?.asString.toString()



                        val maintainmessage =
                            response.body()?.get("app_maintainence_msg").toString()
                                .replace("\"", "")
                        val maintainstatus =
                            response.body()?.get("maintainence_msg_status").toString()
                                .replace("\"", "")
                        maintainMessage = maintainmessage
                        maintainStatus = maintainstatus
                        val Sharemsg =
                            response.body()?.get("share_msg").toString().replace("\"", "")
                        Share_msg = Sharemsg
                        val applink =
                            response.body()?.get("app_link").toString().replace("\"", "")
                        appLink = applink

                        val minimumAppVersion =
                            response.body()?.get("user_minimum_version")?.asString
                        val currentAppVersion =
                            response.body()?.get("user_current_version")?.asString
                        appCurrentVersion = currentAppVersion.toString()
                        appMinimumVersion = minimumAppVersion.toString()

                        val withdrowStatusRes =
                            response.body()?.get("withdraw_status").toString()

                        withdrowStatus = withdrowStatusRes
                        val trans = response.body()?.get("transfer_point_status").toString()
                            .replace("\"", "")
                        transferAmountStatus = trans

                        val betting = response.body()?.get("betting_status").toString()
                            .replace("\"", "")
                        Betting_status = betting
                        val accounts = response.body()?.get("account_block_status").toString()
                            .replace("\"", "")
                        accountBlockStatus = accounts
                        val updatemsg = response.body()?.get("message")?.asString.toString()
                        updateMessage = updatemsg
                        val updatelinkbtn =
                            response.body()?.get("link_btn_text")?.asString.toString()
                        updateLinkBtn = updatelinkbtn
                        val updateactionbtn =
                            response.body()?.get("action_btn_text")?.asString.toString()
                        updateActionBtn = updateactionbtn

                        val deviceArray = response.body()?.getAsJsonArray("device_result")!!
                        deviceArrayList.clear()
                        var j = 0
                        while (j < deviceArray.size()) {
                            val jsonObject = deviceArray[j] as JsonObject
                            j++
                            val device = jsonObject.get("device_id").toString().replace("\"", "")
                            val securityPin =
                                jsonObject.get("security_pin_status").toString().replace("\"", "")
                            val logoutStatus =
                                jsonObject.get("logout_status").toString().replace("\"", "")

                            if (device == checkDeviceId) {
                                checkSecurityPin = securityPin
                                checkLogoutStatus = logoutStatus
                            }
                            deviceArrayList.add(device)
                        }

                        var gameObject: JsonObject
                        val gamerateArrayObject = response.body()?.getAsJsonArray("result")!!
                        var i = 0
                        adapterArrayList.clear()
                        while (i < gamerateArrayObject.size()) {
                            gameObject = gamerateArrayObject[i] as JsonObject
                            i++
                            val opentime = gameObject.get("open_time").toString().replace(
                                "\"", ""
                            )
                            val closetime = gameObject.get("close_time").toString().replace(
                                "\"", ""
                            )
                            val gamename = gameObject.get("game_name").toString().replace(
                                "\"", ""
                            )
                            val gameopenResult = gameObject.get("open_result").toString().replace(
                                "\"", ""
                            )
                            val gamecloseResult = gameObject.get("close_result").toString().replace(
                                "\"", ""
                            )
                            var resultopenandclose: String
                            if (gameopenResult.equals("") && gamecloseResult.equals("")) {
                                resultopenandclose = "XXX-XX-XXX"
                            } else {
                                if (gameopenResult.equals("") && !gamecloseResult.equals("")) {
                                    resultopenandclose = "XXX-X" + gamecloseResult
                                } else if (!gameopenResult.equals("") && gamecloseResult.equals("")) {
                                    resultopenandclose = gameopenResult + "X-XXX"
                                } else {
                                    resultopenandclose = gameopenResult + gamecloseResult
                                }
                            }

                            val marketstatus = gameObject.get("msg").toString().replace(
                                "\"", ""
                            )
                            val weburl = gameObject.get("web_chart_url").toString().replace(
                                "\"", ""
                            )
                            val marketstate = gameObject.get("msg_status").toString().replace(
                                "\"", ""
                            )
                            val gameId = gameObject.get("game_id").toString().replace(
                                "\"", ""
                            )

                            adapterArrayList.add(
                                gamedataholder(
                                    opentime,
                                    closetime,
                                    gamename,
                                    resultopenandclose,
                                    marketstatus,
                                    weburl,
                                    marketstate,
                                    gameId,
                                    betting
                                )
                            )

                        }
                        if (Betting_status.equals("1")) {
                            val adaptercalling = HomeRecyclerAdapter(
                                applicationContext, adapterArrayList
                            )
                            recyclerView.setHasFixedSize(true)
                            adaptercalling.notifyDataSetChanged()
                            recyclerView.adapter = adaptercalling
                        } else {
                            val adaptercalling = HomeRecyclerHideModeAdapter(
                                applicationContext, adapterArrayList
                            )
                            recyclerView.setHasFixedSize(true)
                            adaptercalling.notifyDataSetChanged()
                            recyclerView.adapter = adaptercalling
                        }
                        myHideShowProgress(false)
                    } else {
                        Toast.makeText(
                            applicationContext, "No Record Found!", Toast.LENGTH_LONG
                        ).show()
                        myHideShowProgress(false)
                    }
                }
                checkConditions()
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(
                    applicationContext, "Unable to Connect to Internet", Toast.LENGTH_LONG
                ).show()
                myHideShowProgress(false)

            }
        })
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).setMessage("Are you sure want to close application?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { _, _ -> call() }.setNegativeButton("No", null).show()
    }

    fun call() {
        super.onBackPressed()
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

    private fun showUpdateDialog(checkCondition: String): AlertDialog {
        val alert = AlertDialog.Builder(this)
        val myview = layoutInflater.inflate(R.layout.custom_dialog_app_update, null)
        alert.setView(myview)
        val message = myview.findViewById<TextView>(R.id.sucessmessage)
        val cancelbut = myview.findViewById<Button>(R.id.cancelBut)
        val updateBut = myview.findViewById<Button>(R.id.updateBut)
        cancelbut.text = updateActionBtn
        message.text = updateMessage
        updateBut.text = updateLinkBtn
        if (checkCondition == "Hide") {
            cancelbut.visibility = View.GONE
        }
        val alertDialog = alert.create()

        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        cancelbut.setOnClickListener {
            alertDialog.dismiss()
        }
        updateBut.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(appLink)))
        }
        return alertDialog
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showdialog(upi_type: String): AlertDialog {
        val alert = AlertDialog.Builder(this)
        val myview = layoutInflater.inflate(R.layout.custom_dialog_box, null)
        alert.setView(myview)
        val phoneEd = myview.findViewById<EditText>(R.id.txt_input)
        val cancelbut = myview.findViewById<Button>(R.id.btn_cancel)
        val addphoneBut = myview.findViewById<Button>(R.id.btn_okay)

        val alertDialog = alert.create()
        if (upi_type == "1") {
            phoneEd.setText(registernumberget_Paytm)
            upiCode = 63254
        }
        if (upi_type == "2") {
            phoneEd.setText(registernumberget_Googlepay)
            upiCode = 25468
        }
        if (upi_type == "3") {
            phoneEd.setText(registernumberget_phonepe)
            upiCode = 10235
        }

        alertDialog.setCanceledOnTouchOutside(false)
        cancelbut.setOnClickListener {
            alertDialog.dismiss()
        }

        addphoneBut.setOnClickListener {
            phonenumerget = phoneEd.text.toString()
            if (phonenumerget.isEmpty()) {
                phoneEd.error = "Can't be empty!"
            } else {
                if (phonenumerget.length < 10) {
                    phoneEd.error = "Invalid number!"
                } else {
                    val intent = Intent(this, OtpVerifyActivity::class.java)
                    intent.putExtra("calling_activity", "UserWalletPaisa")
                    this.startActivityForResult(intent, upiCode)
                    alertDialog.dismiss()
                }
            }
        }

        return alertDialog
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 63254) {
            if (resultCode == RESULT_OK) {
                val responseStatus = data?.getStringExtra("Result").toString()
                if (responseStatus == "Success") {
                    retrofitAddnumber(phonenumerget, "1")
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
                    snak.setTextColor(Color.WHITE)
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
                snak.setTextColor(Color.WHITE)
                snak.setBackgroundTint(Color.RED)
                snak.show()
                Handler().postDelayed({}, 200)
            }

        }

        if (requestCode == 10235) {
            if (resultCode == RESULT_OK) {
                val responseStatus = data?.getStringExtra("Result").toString()
                if (responseStatus == "Success") {
                    retrofitAddnumber(phonenumerget, "3")
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
                    snak.setTextColor(Color.WHITE)
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
                snak.setTextColor(Color.WHITE)
                snak.setBackgroundTint(Color.RED)
                snak.show()
                Handler().postDelayed({}, 200)
            }

        }

        if (requestCode == 25468) {
            if (resultCode == RESULT_OK) {
                val responseStatus = data?.getStringExtra("Result").toString()
                if (responseStatus == "Success") {
                    retrofitAddnumber(phonenumerget, "2")
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
                    snak.setTextColor(Color.WHITE)
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
                snak.setTextColor(Color.WHITE)
                snak.setBackgroundTint(Color.RED)
                snak.show()
                Handler().postDelayed({}, 200)
            }

        }
    }

    private fun retrofitAddnumber(phonenumerget: String, upi_type: String) {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("upi_type", upi_type)
        if (upi_type.equals("1")) {
            jsonvalues.addProperty("paytm_no", phonenumerget)
            jsonvalues.addProperty("google_pay_no", "")
            jsonvalues.addProperty("phon_pay_no", "")
        } else if (upi_type.equals("2")) {
            jsonvalues.addProperty("paytm_no", "")
            jsonvalues.addProperty("google_pay_no", phonenumerget)
            jsonvalues.addProperty("phon_pay_no", "")
        } else {
            jsonvalues.addProperty("paytm_no", "")
            jsonvalues.addProperty("google_pay_no", "")
            jsonvalues.addProperty("phon_pay_no", phonenumerget)
        }
        RetrofitClient.service.AddusernumberAPI(jsonvalues).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>, response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    val status = response.body()?.get("status").toString().replace("\"", "")
                    val msg = response.body()?.get("msg").toString().replace("\"", "")
                    if (status.equals("true")) {
                        if (upi_type.equals("1")) {
                            registernumberget_Paytm = phonenumerget
                        } else if (upi_type.equals("2")) {
                            registernumberget_Googlepay = phonenumerget
                        } else {
                            registernumberget_phonepe = phonenumerget
                        }

                        Toast.makeText(
                            applicationContext, msg, Toast.LENGTH_LONG
                        ).show()

                        myHideShowProgress(false)
                    } else {
                        Toast.makeText(
                            applicationContext, "Invalid User!", Toast.LENGTH_LONG
                        ).show()
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

    private fun retrofitCheckNumber() {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())

        RetrofitClient.service.CheckuserUpinumberAPI(jsonvalues)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")

                        if (status.equals("true")) {
                            val paymentdetailarray =
                                response.body()?.getAsJsonArray("payment_details")
                            val itemsjsonobject: JsonObject
                            itemsjsonobject = paymentdetailarray?.get(0) as JsonObject
                            val paytmno =
                                itemsjsonobject.get("paytm_number").toString().replace("\"", "")
                            val googleno = itemsjsonobject.get("google_pay_number").toString()
                                .replace("\"", "")
                            val phonepeno =
                                itemsjsonobject.get("phone_pay_number").toString().replace("\"", "")

                            registernumberget_Paytm = paytmno
                            registernumberget_Googlepay = googleno
                            registernumberget_phonepe = phonepeno

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

    private fun retrofitRefreshAmt() {
        myHideShowProgress(true)
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())

        RetrofitClient.service.refreshAmtApi(jsonvalues).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>, response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    val status = response.body()?.get("status").toString().replace("\"", "")
                    val msg = response.body()?.get("msg").toString().replace("\"", "")
                    if (status.equals("true")) {
                        response.body()?.get("wallet_amt").toString().replace("\"", "")

                        val min_transfer =
                            response.body()?.get("min_transfer").toString().replace("\"", "")
                        val max_transfer =
                            response.body()?.get("max_transfer").toString().replace("\"", "")
                        val min_Withdraw = response.body()?.get("min_withdrawal").toString()
                            .replace("\"", "")
                        val max_Withdraw = response.body()?.get("max_withdrawal").toString()
                            .replace("\"", "")
                        minTransfer = min_transfer
                        Max_Transfer = max_transfer
                        Min_Withdraw = min_Withdraw
                        maxWithdraw = max_Withdraw

                        myHideShowProgress(false)
                    } else {
                        Toast.makeText(
                            applicationContext, msg, Toast.LENGTH_LONG
                        ).show()
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

    override fun onPageStarted(url: String?, favicon: Bitmap?) {

    }

    override fun onPageFinished(url: String?) {

    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {

    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {

    }

    override fun onExternalPageRequest(url: String?) {

    }


    private fun getToken() {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        ContentValues.TAG,
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }

                var devicetoken = task.result
                println("++++++token "+ devicetoken)

                if (devicetoken != null && devicetoken != "") {
                    senddevicetoken(devicetoken)
                }
            })

        } catch (e: Exception) {
        }
    }


    private fun senddevicetoken(devicetoken: String) {
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("fcm_token", devicetoken)

        RetrofitClient.service.updatefcm(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val msg = response.body()?.get("msg").toString().replace("\"", "")
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")

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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notificationChannelId,
                "Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = "App notifications"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    private fun sendNotificationcontrol(notifications: String) {
        val jsonvalues = JsonObject()
        jsonvalues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonvalues.addProperty("env_type", env_type)
        jsonvalues.addProperty("unique_token", session.getSession_userid())
        jsonvalues.addProperty("notification_status", notifications)

        RetrofitClient.service.notificationsetting(jsonvalues).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val msg = response.body()?.get("msg").toString().replace("\"", "")
                        val status =
                            response.body()?.get("status").toString().replace("\"", "")

//                        println("+++++"+ msg)

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

