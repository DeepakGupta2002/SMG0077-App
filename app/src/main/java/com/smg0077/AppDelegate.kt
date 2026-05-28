package com.smg0077

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.*
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object AppDelegate {

    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }

    fun stopScreenShot(mActivity: Activity) {
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            mActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        try {
            mActivity.window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        } catch (e: Exception) {
        }
    }

    fun isNetworkAvailable(context: Context?): Boolean {


        var isNetworkAvailable = false
        val connectivityManager =
            context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder = NetworkRequest.Builder()
        builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        val networkRequest = builder.build()
        connectivityManager.registerNetworkCallback(networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network!!)
                    isNetworkAvailable = true
                }

                override fun onLost(network: Network) {
                    super.onLost(network!!)
                    isNetworkAvailable = false
                }
            })

        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo: NetworkInfo?
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting

        return isNetworkAvailable
    }


    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) {
            if (activity.currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            }
        }
    }

    fun showToast(context: Context, msg: String) {
        try {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    fun showToastlong(mContext: Context, message: String) {
        val toast = Toast.makeText(
            mContext,
            message,
            Toast.LENGTH_LONG
        )
        toast.show()
        val handler = Handler()
        handler.postDelayed(Runnable { toast.cancel() }, 500)
    }

    fun showSnackBarOnError(view: View, message: String, context: Context) {
        val snackBar = Snackbar.make(
            view, message, Snackbar.LENGTH_LONG
        )
//        snackBar.changeFont()
        snackBar.setBackgroundTint(ContextCompat.getColor(context, R.color.red))
        snackBar.setTextColor(ContextCompat.getColor(context, R.color.white))

        var view = snackBar.view
        val params: FrameLayout.LayoutParams = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        snackBar.show()
    }

    fun showNetworkError(view: View, context: Context) {
        val snackBar = Snackbar.make(
            view, context.getString(R.string.no_internet), Snackbar.LENGTH_LONG
        )
        snackBar.changeFont()
        snackBar.setBackgroundTint(ContextCompat.getColor(context, R.color.primary))
        snackBar.setTextColor(ContextCompat.getColor(context, R.color.white))

        var view = snackBar.view
        val params: FrameLayout.LayoutParams = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        snackBar.show()
    }


    fun showSnackBarOnSuccess(view: View, message: String, context: Context) {
        val snackBar = Snackbar.make(
            view, message, Snackbar.LENGTH_LONG
        )
        snackBar.changeFont()
        snackBar.setBackgroundTint(ContextCompat.getColor(context, R.color.primary))
        snackBar.setTextColor(ContextCompat.getColor(context, R.color.white))
        var view = snackBar.view
        val params: FrameLayout.LayoutParams = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        snackBar.show()
    }


    private fun Snackbar.changeFont() {
        val tv = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
//        val font = Typeface.createFromAsset(context.assets, "font/poppins_regular.ttf")
//        tv.typeface = font
    }


    fun saveImage(mContext: Context, myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
        val wallpaperDirectory = mContext.getDir("images", Context.MODE_PRIVATE)

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        try {
            val f = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .timeInMillis).toString() + ".jpg")
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                mContext,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }


    fun shareApp(mActivity: Activity) {
        val message =
            "Download *Path of News App* " + "https://play.google.com/store/apps/details?id=com.newsalo"
        try {
            val sharingIntent =
                Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, message)
            mActivity.startActivity(
                Intent.createChooser(
                    sharingIntent,
                    "Share Via"
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            showToast(mActivity, "Error, Please try later")
        }
    }

    fun sendMail(mActivity: Activity) {
        val mailIntent = Intent(Intent.ACTION_VIEW)
        val data =
            Uri.parse("mailto:?subject=" + "Path of News App Feedback!" + "&body=" + "Write feedback here... " + "&to=" + "timewebtarget@gmail.com")
        mailIntent.data = data
        mActivity.startActivity(Intent.createChooser(mailIntent, "Send mail using..."))

    }

    fun shareAppWithText(mActivity: Activity, title: String) {
        val message =
            title + "Download *Yozex App* " + "https://play.google.com/store/apps/details?id=com.yozex"
        try {
            val sharingIntent =
                Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, message)
            mActivity.startActivity(
                Intent.createChooser(
                    sharingIntent,
                    "Share Via"
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            showToast(mActivity, "Error, Please try later")
        }
    }


    fun setUniversalImage(
        context: Context,
        imageView: ImageView,
        imgUrl: String,
        placeHolder: Int
    ) {
        Glide.with(context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .placeholder(placeHolder)
                    .dontAnimate()
                    .dontTransform()
            )
            .into(imageView)
    }


    fun setUniversalImage(context: Context, imageView: ImageView, imgUrl: String) {
        if (imgUrl != null && imgUrl != "") {
            Glide.with(context)
                .load(imgUrl)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .dontAnimate()
                        .dontTransform()
                )
                .into(imageView)
        } else {
            /*   var bitMap: Bitmap =
                   BitmapFactory.decodeResource(context.resources, R.mipmap.default_profile)
               imageView.setImageBitmap(bitMap)*/
        }
    }

    fun setUserImage(context: Context, imageView: ImageView?, imgUrl: String) {
        if (imgUrl != null && !imgUrl.equals("")) {
            Glide.with(context)
                .load(imgUrl).apply(RequestOptions().override(100, 100))
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .dontAnimate()
                        .dontTransform()
                )
                .into(imageView!!)
        } else {
            /*  var bitMap: Bitmap =
                  BitmapFactory.decodeResource(context.resources, R.mipmap.default_profile)
              imageView!!.setImageBitmap(bitMap)*/
        }
    }

    /* full screen*/
    fun fullScreenWIthStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            /*  activity.window.setFlags(
                   WindowManager.Layoutdata.FLAG_LAYOUT_IN_SCREEN,
                   WindowManager.Layoutdata.FLAG_LAYOUT_NO_LIMITS
               )*/
            /*  activity.window.addFlags(WindowManager.Layoutdata.FLAG_LAYOUT_IN_SCREEN);*/
        }
    }

    fun getMobileDeviceName(): String {
        var deviceInfo = ""
        try {
            val manufacturer = Build.MANUFACTURER
            var model = Build.MODEL
            deviceInfo = manufacturer + " " + model
        } catch (e: Exception) {
        }
        return deviceInfo
    }


    fun getUniqueDeviceId(mContext: Context): String? {
        var android_id = ""
        try {
            android_id = Settings.Secure.getString(
                mContext.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } catch (e: Exception) {
        }
        return android_id
    }

    val sslSocketFactory: SSLSocketFactory?
        get() {
            try {
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<java.security.cert.X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun checkServerTrusted(
                        chain: Array<java.security.cert.X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                        return arrayOf()
                    }
                })
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, java.security.SecureRandom())

                return sslContext.socketFactory
            } catch (e: KeyManagementException) {
                return null
            } catch (e: NoSuchAlgorithmException) {
                return null
            }
        }





    fun openWebPage(mContext: Context, url: String) {
        try {
            val myIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(url))
            mContext.startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                mContext, "No application found to handle it."
                        + " Please install a web browser", Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }

    fun openTelegramLink(context: Context, telegramValue: String) {
        val value = telegramValue.trim()
        if (value.isEmpty() || value == "null") {
            Toast.makeText(context, "Telegram link not available", Toast.LENGTH_LONG).show()
            return
        }

        val url = when {
            value.startsWith("http://", true) || value.startsWith("https://", true) || value.startsWith("tg://", true) -> value
            value.startsWith("@") -> "https://t.me/${value.substring(1)}"
            value.startsWith("t.me/", true) -> "https://$value"
            value.startsWith("telegram.me/", true) -> "https://$value"
            else -> "https://t.me/$value"
        }

        openWebPage(context, url)
    }


//    fun openCustomWebPage(context: Context, url: String ) {
//        val builder = CustomTabsIntent.Builder()
//        val customTabsIntent = builder.build()
//        builder.setToolbarColor(ContextCompat.getColor(context, R.color.white))
//        val packageName = "com.android.chrome"
//        if (packageName != null) {
//            customTabsIntent.intent.setPackage(packageName)
//            customTabsIntent.launchUrl(context, Uri.parse(url))
//        } else {
//            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
//        }
//    }


    fun saveImagePNG(mContext: Context, myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val wallpaperDirectory = mContext.getDir("images", Context.MODE_PRIVATE)

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        try {
            val f = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .timeInMillis).toString() + ".png")
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                mContext,
                arrayOf(f.path),
                arrayOf("image/png"), null
            )
            fo.close()
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }


    fun ChangeLanguage(mActivity: Activity, LanguageCode: String) {
        val lang =
            LanguageCode // replace "es" with the language code for the language you want to use
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = mActivity.resources.configuration
        config.setLocale(locale)
        mActivity.resources.updateConfiguration(config, mActivity.resources.displayMetrics)
        mActivity?.recreate()
    }

    fun formatAmount(amount: Double): String {
        val lakh = 100000.0
        val crore = 10000000.0
        val hazar = 1000.0

        return when {
            amount >= crore -> String.format("%.2f Cr", amount / crore)
            amount >= lakh -> String.format("%.2f Lakh", amount / lakh)
            amount >= hazar -> String.format("%.2f Thousand", amount / hazar)
            else -> String.format("%.2f", amount)
        }
    }

    fun formatWhatsAppPhoneNumber(phoneNumber: String): String {
        var digits = phoneNumber.filter { it.isDigit() }
        while (digits.startsWith("0")) {
            digits = digits.substring(1)
        }
        return if (digits.length == 10) "91$digits" else digits
    }

    fun openWhatsapp(whatappno: String, context: Context) {
        val formattedNumber = formatWhatsAppPhoneNumber(whatappno)
        if (formattedNumber.isEmpty()) {
            Toast.makeText(context, "WhatsApp number not available", Toast.LENGTH_LONG).show()
            return
        }

        val uri = Uri.parse("smsto:+$formattedNumber")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.setPackage("com.whatsapp")

        if (intent.resolveActivity(context.packageManager) == null) {
            val uri1 = Uri.parse("smsto:+$formattedNumber")
            val intent1 = Intent(Intent.ACTION_SENDTO, uri1)
            intent1.setPackage("com.whatsapp.w4b")

            if (intent1.resolveActivity(context.packageManager) == null) {
                val url = "https://api.whatsapp.com/send?phone=$formattedNumber&text=Hi"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                context.startActivity(i)
            } else {
                context.startActivity(intent1)
            }
        } else {
            context.startActivity(Intent.createChooser(intent, ""))
        }
    }


    private fun openTelegram(context: Context) {
        val intent = telegramIntent(context, "lotusin365")
        context.startActivity(intent)
    }

    private fun telegramIntent(context: Context,telegramppno: String ): Intent {
        val intent: Intent? = try {
            try {
                context.packageManager.getPackageInfo(
                    "org.telegram.messenger", 0
                )
            } catch (e: Exception) {
                context.packageManager.getPackageInfo(
                    "org.thunderdog.challegram", 0
                )
            }
            Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=${telegramppno}"))
        } catch (e: Exception) {
            Toast.makeText(context, "Telegram App Not Installed! Please Install Telegram app from play store.", Toast.LENGTH_LONG).show()
            Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/$telegramppno"))
        }
        return intent!!
    }

    fun makePhoneCall(context: Context, phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")
            context.startActivity(intent)
        } else {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 1)
        }
    }
    fun openCustomWebPage(context: Context, url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.white))
        val packageName = "com.android.chrome"
        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(context, Uri.parse(url))
        } else {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

}
