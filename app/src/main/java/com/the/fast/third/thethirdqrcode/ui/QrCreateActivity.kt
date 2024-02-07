package com.the.fast.third.thethirdqrcode.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.the.fast.third.thethirdqrcode.databinding.ActivityGuideBinding
import com.the.fast.third.thethirdqrcode.databinding.ActivityQrCreatBinding
import com.the.fast.third.thethirdqrcode.utils.BlackHelp
import com.the.fast.third.thethirdqrcode.utils.ThirdQrUtils

class QrCreateActivity : AppCompatActivity() {
    val binding : ActivityQrCreatBinding by lazy {
        ActivityQrCreatBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.textView.setOnClickListener {
            finish()
        }
        binding.tvGenerate.setOnClickListener {

            val content = binding.etResult.text.toString()
            if (content.isEmpty()) {
                Toast.makeText(this, "Please enter the content to be generated", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            BlackHelp.create_data = content
            startActivity(Intent(this, ResultActivity::class.java))
        }
    }


}