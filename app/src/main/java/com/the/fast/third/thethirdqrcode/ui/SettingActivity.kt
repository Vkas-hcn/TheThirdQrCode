package com.the.fast.third.thethirdqrcode.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.the.fast.third.thethirdqrcode.databinding.ActivitySettingBinding

class SettingActivity:AppCompatActivity() {
    val binding : ActivitySettingBinding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.textView.setOnClickListener {
            finish()
        }
        binding.atvPrivacyPolicy.setOnClickListener {
            startActivity(Intent(this,PpActivity::class.java))
        }
    }
}