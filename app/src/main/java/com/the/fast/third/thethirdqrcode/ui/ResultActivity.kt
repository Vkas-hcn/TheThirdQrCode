package com.the.fast.third.thethirdqrcode.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.the.fast.third.thethirdqrcode.databinding.ActivityEndBinding
import com.the.fast.third.thethirdqrcode.databinding.ActivityResultBinding
import com.the.fast.third.thethirdqrcode.databinding.ActivitySettingBinding
import com.the.fast.third.thethirdqrcode.utils.BlackHelp
import com.the.fast.third.thethirdqrcode.utils.ThirdQrUtils
import com.the.fast.third.thethirdqrcode.utils.ThirdQrUtils.hasWriteExternalStoragePermission
import com.the.fast.third.thethirdqrcode.utils.ThirdQrUtils.requestWriteExternalStoragePermission
import com.the.fast.third.thethirdqrcode.utils.ThirdQrUtils.saveBitmapToGallery

class ResultActivity : AppCompatActivity() {
    val binding: ActivityResultBinding by lazy {
        ActivityResultBinding.inflate(layoutInflater)
    }
    var bitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initData()
    }
    private fun saveBitmap(bitmap: Bitmap) {
        val requestCode = 2333
        if (!this.hasWriteExternalStoragePermission()) {
            this.requestWriteExternalStoragePermission(requestCode)
        } else {
            saveBitmapToGallery(bitmap){}
        }
    }

    private fun initData() {
        binding.textView.setOnClickListener {
            finish()
        }
        binding.tvSave.setOnClickListener {
            bitmap?.let { it1 -> saveBitmap(it1) }
        }
        binding.tvShare.setOnClickListener {
            bitmap?.let { it1 -> ThirdQrUtils.shareBitmap(it1,this) }
        }

        bitmap = ThirdQrUtils.generateQrCodeFromText(BlackHelp.create_data, dp2px(270), dp2px(270))
        binding.imgCreate.setImageBitmap(bitmap)
    }

    private fun dp2px(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2333 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            bitmap?.let { saveBitmap(it) }
        }
    }
}