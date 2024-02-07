package com.the.fast.third.thethirdqrcode.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.the.fast.third.thethirdqrcode.databinding.ActivityEndBinding
import com.the.fast.third.thethirdqrcode.databinding.ActivitySettingBinding
import com.the.fast.third.thethirdqrcode.utils.BlackHelp
import com.the.fast.third.thethirdqrcode.utils.ThirdQrUtils

class EndActivity:AppCompatActivity() {
    val binding : ActivityEndBinding by lazy {
        ActivityEndBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initData()
    }
    private fun initData(){
        binding.tvResult.text = BlackHelp.scan_data
        binding.tvCopy.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", BlackHelp.scan_data)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }
        binding.tvSer.setOnClickListener {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val content_url = Uri.parse("https://www.google.com/search?q=${BlackHelp.scan_data}")
            intent.data = content_url
            startActivity(intent)
        }
        binding.tvShare.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, BlackHelp.scan_data)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share"))
        }
        binding.textView.setOnClickListener {
            finish()
        }
    }
}