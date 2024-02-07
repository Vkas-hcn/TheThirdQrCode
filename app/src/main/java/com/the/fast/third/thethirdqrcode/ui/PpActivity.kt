package com.the.fast.third.thethirdqrcode.ui

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.the.fast.third.thethirdqrcode.databinding.ActivityPpBinding

class PpActivity  : AppCompatActivity() {
    val binding : ActivityPpBinding by lazy {
        ActivityPpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initWebView()
        binding.textView.setOnClickListener {
            finish()
        }
    }
    private fun initWebView() {
        binding.webView.loadUrl("https://www.baidu.com")
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
        }
        binding.webView.webChromeClient = object : android.webkit.WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    binding.progressBarWeb.visibility = android.view.View.GONE
                } else {
                    if (binding.progressBarWeb.visibility == android.view.View.GONE) {
                        binding.progressBarWeb.visibility = android.view.View.VISIBLE
                    }
                    binding.progressBarWeb.progress = newProgress
                }
            }
        }
    }
}