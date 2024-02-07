package com.the.fast.third.thethirdqrcode.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.the.fast.third.thethirdqrcode.R
import com.the.fast.third.thethirdqrcode.utils.ThirdQrUtils.analyzeBitmap
import com.the.fast.third.thethirdqrcode.databinding.ActivityQrsacnBinding
import com.the.fast.third.thethirdqrcode.lib_zxing.activity.CaptureFragment
import com.the.fast.third.thethirdqrcode.lib_zxing.activity.CodeUtils
import com.the.fast.third.thethirdqrcode.utils.BlackHelp

class QrScanActivity : AppCompatActivity() {
    val binding : ActivityQrsacnBinding by lazy {
        ActivityQrsacnBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        iniData()
    }
     private fun iniData() {
        setQrScan()
         binding.imgBack.setOnClickListener {
            finish()
         }
        binding.tvChoose.setOnClickListener {
            toClickGallery()
        }
        binding.imgFlash.setOnClickListener {
            toClickFlash()
        }
    }
    var isOpen = false
    private fun setQrScan() {
        val analyzeCallback: CodeUtils.AnalyzeCallback = object : CodeUtils.AnalyzeCallback {
            override fun onAnalyzeSuccess(mBitmap: Bitmap, result: String) {
                toEndPage(result)
            }

            override fun onAnalyzeFailed() {
                Toast.makeText(
                    applicationContext, "Identification failed, please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        val captureFragment = CaptureFragment()
        CodeUtils.setFragmentArgs(captureFragment, R.layout.layout_qr)
        captureFragment.analyzeCallback = analyzeCallback
        supportFragmentManager.beginTransaction().replace(R.id.fl_my_container, captureFragment)
            .commit()
    }


    private fun toClickGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, 0x9527)
    }
    fun toClickFlash(){
        isOpen =!isOpen
        CodeUtils.isLightEnable(isOpen,binding)
    }

    fun toEndPage(data:String){
        if (data.isNotEmpty()) {
            BlackHelp.scan_data = data
            startActivity(Intent(this, EndActivity::class.java))
            finish()
        } else {
            Toast.makeText(
                applicationContext, "Identification failed, please try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x9527) {
            if (data != null) {
                val uri: Uri? = data.data
                val cr = contentResolver
                try {
                    val mBitmap = MediaStore.Images.Media.getBitmap(cr, uri)
                    analyzeBitmap(mBitmap,object : CodeUtils.AnalyzeCallback {
                        override fun onAnalyzeSuccess(mBitmap: Bitmap, result: String) {
                            toEndPage(result)
                        }

                        override fun onAnalyzeFailed() {
                            Toast.makeText(this@QrScanActivity, "Identification failed, please try again", Toast.LENGTH_LONG).show()
                        }
                    })
                    mBitmap?.recycle()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}