package com.smg0077.web

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.smg0077.HomeActivity
import com.smg0077.R
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.retrofit.RetrofitClient.env_type
import com.smg0077.session.MySession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirebaseMessage : FirebaseMessagingService() {
    private val channel_id = "notification"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        println("++++++ " + Gson().toJson(remoteMessage))

        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body

//            println("++++++ Title: $title, Body: $body")
            if (title != null && body != null) {
                println("+++++t"+ title + body)
                showNotification(title, body)
            }
        }
    }




    private fun showNotification(titleHtml: String, messageHtml: String) {
        val title = Html.fromHtml(titleHtml)
        val message = Html.fromHtml(messageHtml)

        val intent: Intent =  Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_MUTABLE
        )


        val contentView = RemoteViews(packageName, R.layout.notification)
        contentView.setTextViewText(R.id.title, title)
        contentView.setTextViewText(R.id.message, message)


        val notificationBuilder = NotificationCompat.Builder(this, channel_id)
            .setSmallIcon(R.drawable.ic_logo1)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setLights(Color.BLUE, 500, 500)// Set priority to PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
        }

        notificationBuilder.setCustomContentView(contentView)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channel_id,
                "web_app",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }


    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        val session = MySession(applicationContext)
        val uniqueToken = session.getSession_userid()

        if (uniqueToken.isBlank()) {
            Log.d(TAG, "FCM token refreshed before login; token will be uploaded when HomeActivity starts.")
            return
        }

        val jsonValues = JsonObject()
        jsonValues.addProperty("app_key", AppKeyHolderClass().getAppKey())
        jsonValues.addProperty("env_type", env_type)
        jsonValues.addProperty("unique_token", uniqueToken)
        jsonValues.addProperty("fcm_token", token)

        RetrofitClient.service.updatefcm(jsonValues).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d(TAG, "FCM token upload response: ${response.code()} ${response.body()}")
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e(TAG, "FCM token upload failed", t)
            }
        })
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    private fun isAppInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false

        for (appProcess in appProcesses) {
            if (appProcess.processName == context.packageName && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }
        return false
    }
}
