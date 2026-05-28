package com.smg0077.realtask

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.smg0077.R

class RealStarlineGameList : AppCompatActivity() {
    lateinit var backBUT: ImageView
    lateinit var singledigitImagV: RelativeLayout
    lateinit var singlepanaImagV: RelativeLayout
    lateinit var doublepanaImagV: RelativeLayout
    lateinit var triplepanaImagV: RelativeLayout
    lateinit var getgameid: String
    lateinit var windowtitle: TextView
    lateinit var getgamename: String

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_real_starline_game_list)

        initvalues()
        backBUT.setOnClickListener {
            onBackPressed()
        }
        singledigitImagV.setOnClickListener {
            callwindow("single_digit")
        }
        singlepanaImagV.setOnClickListener {
            callwindow("single_pana")
        }
        doublepanaImagV.setOnClickListener {
            callwindow("double_pana")
        }
        triplepanaImagV.setOnClickListener {
            callwindow("triple_pana")
        }
    }

    private fun initvalues() {
        backBUT = findViewById(R.id.userbackbut)
        getgameid = intent.getStringExtra("game_id").toString()
        getgamename = intent.getStringExtra("game_name").toString()
        singledigitImagV = findViewById(R.id.substarline_singledigit)
        singlepanaImagV = findViewById(R.id.substarline_singlepana)
        doublepanaImagV = findViewById(R.id.substarline_doublepana)
        triplepanaImagV = findViewById(R.id.substarline_triplepana)
        windowtitle = findViewById(R.id.welcometxt)
        windowtitle.text = getgamename
    }

    private fun callwindow(gametype: String) {
        val intent = Intent(this, RealStarlineTaskBid::class.java)
        intent.putExtra("game_id", getgameid)
        intent.putExtra("game_name", getgamename)
        intent.putExtra("pana", gametype)
        startActivity(intent)
    }
}