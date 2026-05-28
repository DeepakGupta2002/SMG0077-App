package com.smg0077.transfer

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.JsonObject
import com.smg0077.AppDelegate
import com.smg0077.R
import com.smg0077.retrofit.AppKeyHolderClass
import com.smg0077.retrofit.RetrofitClient
import com.smg0077.session.MySession
import kotlinx.android.synthetic.main.activity_upload_qrimage.pointtxt
import kotlinx.android.synthetic.main.activity_upload_qrimage.progressUploadImage
import kotlinx.android.synthetic.main.activity_upload_qrimage.setImage
import kotlinx.android.synthetic.main.activity_upload_qrimage.uploadsreeenshotButton
import kotlinx.android.synthetic.main.activity_upload_qrimage.userbackbut
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.File

class UploadQRImageActivity : AppCompatActivity() {
    private var bitmap: Bitmap? = null
    lateinit var session: MySession

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_qrimage)
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.primarydark)
        session = MySession(applicationContext)


        userbackbut.setOnClickListener {
            onBackPressed()
            finish()
        }

        setImage.setOnClickListener {
             ImagePicker.with(this@UploadQRImageActivity)
                 .compress(2048)
                 .start()
         }

        uploadsreeenshotButton.setOnClickListener {
            if (pointtxt.text.toString().trim().length < 2){
                AppDelegate.showToast(this, "Enter Valid Amount !")
            }else if (bitmap== null){
                AppDelegate.showToast(this, "Please Choose Image")
            }else {
                updateImg()
            }
        }
    }

    private fun setScreenshotImage(resultUri: Uri) {
        try {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(
                    contentResolver, resultUri
                )
            } else {
                val source = ImageDecoder.createSource(contentResolver, resultUri)
                bitmap = ImageDecoder.decodeBitmap(source)
            }
            setImage.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun updateImg() {
        myHideShowProgress(true)
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("unique_token", session.getSession_userid())
        builder.addFormDataPart("request_amount", pointtxt.text.toString().trim())
        builder.addFormDataPart("env_type", RetrofitClient.env_type)
        builder.addFormDataPart("app_key", AppKeyHolderClass().getAppKey())

        if (bitmap != null) {
            var filePath = AppDelegate.saveImage(this, bitmap!!)
            var screenshot = File(filePath)
            var reqFile = RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                screenshot
            )

            var body =
                MultipartBody.Part.createFormData("payment_slip", screenshot.name, reqFile)
            builder.addPart(body)
        }

        val requestBody: MultipartBody = builder.build()
        RetrofitClient.service.uploadscreenshot(requestBody).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>, response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    val msg = response.body()?.get("msg").toString().replace("\"", "")
                    val status = response.body()?.get("status").toString().replace("\"", "")

                    if (status.equals("true")) {
                        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()

                    } else {
                        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
                    }
                    myHideShowProgress(false)

                    onBackPressed()
                    finish()
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

    private fun myHideShowProgress(check: Boolean) {
        if (check) {
            progressUploadImage.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            progressUploadImage.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            setScreenshotImage(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            AppDelegate.showToast(this, ImagePicker.getError(data))

        } else {
            AppDelegate.showToast(this, "Task Cancelled")
        }
    }
}