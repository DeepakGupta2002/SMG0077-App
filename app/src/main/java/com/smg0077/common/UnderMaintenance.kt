package com.smg0077.common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.smg0077.R

class UnderMaintenance : AppCompatActivity() {
    private lateinit var messageText: TextView
    private lateinit var getMessage: String
    private lateinit var appCloseBut: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_mantaince)
        messageText = findViewById(R.id.mantaintext)
        appCloseBut = findViewById(R.id.appcloseBut)
        getMessage = intent.getStringExtra("message").toString()
        messageText.text = getMessage

        appCloseBut.setOnClickListener {
            closeApplication()
        }

    }

    private fun closeApplication() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    override fun onBackPressed() {
        closeApplication()

    }
}