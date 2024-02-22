package com.the.fast.third.thethirdqrcode.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.the.fast.third.thethirdqrcode.databinding.ActivityQrCreatBinding
import com.the.fast.third.thethirdqrcode.utils.BlackHelp

class QrCreateActivity : AppCompatActivity() {
    private val binding: ActivityQrCreatBinding by lazy {
        ActivityQrCreatBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        binding.textView.setOnClickListener { finish() }

        binding.tvGenerate.setOnClickListener {
            binding.etResult.text.toString().takeIf { it.isNotEmpty() }?.let { content ->
                BlackHelp.create_data = content
                Intent(this, ResultActivity::class.java).also {
                    startActivity(it)
                }
            } ?: run {
                Toast.makeText(this, "Please enter the content to be generated", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
