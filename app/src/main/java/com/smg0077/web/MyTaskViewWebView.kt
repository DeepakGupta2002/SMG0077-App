package com.smg0077.web

import android.os.Bundle
import android.view.Window
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.smg0077.R

class MyTaskViewWebView : AppCompatActivity() {
    lateinit var myWebView: WebView
    lateinit var backBut: ImageView
    lateinit var url: String
    var check: Boolean = true
    lateinit var topBarTitle: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        myWebView = findViewById(R.id.mywebView)
        backBut = findViewById(R.id.userbackbut)
        topBarTitle = findViewById(R.id.welcometxt)
        url = intent.getStringExtra("web_url").toString()
        check = intent.getBooleanExtra("status", false)
        if (check) {
            topBarTitle.text = "Charts"
        }
        myWebView.loadUrl(url)
        if (myWebView.canGoBack()) {
            myWebView.goBack()
        }
        backBut.setOnClickListener {
            onBackPressed()
        }
    }
}