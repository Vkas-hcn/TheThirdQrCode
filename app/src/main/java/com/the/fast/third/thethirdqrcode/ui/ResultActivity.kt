package com.the.fast.third.thethirdqrcode.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
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

    private fun initData() {
        binding.textView.setOnClickListener {
            finish()
        }
        binding.tvSave.setOnClickListener {
            bitmap?.let { it1 -> ThirdQrUtils.saveBitmapToGallery(this, it1) }
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
}