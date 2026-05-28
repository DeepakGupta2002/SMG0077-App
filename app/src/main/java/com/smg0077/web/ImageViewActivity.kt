package com.smg0077.web

import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView
import com.smg0077.R

class ImageViewActivity : AppCompatActivity() {
    lateinit var photoView: PhotoView
    lateinit var backBut: ImageView
    lateinit var imageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_imageview)
        photoView = findViewById(R.id.photoView)
        backBut = findViewById(R.id.closebtn)

        if (intent!=null) {
            imageUrl = intent.getStringExtra("web_url").toString()

            if (imageUrl != null && imageUrl != "") {
                Glide.with(this)
                    .load(imageUrl)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .dontAnimate()
                            .dontTransform()
                    )
                    .into(photoView)
            }
        }


        backBut.setOnClickListener {
            onBackPressed()
        }
    }
}