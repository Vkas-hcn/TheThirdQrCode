package com.the.fast.third.thethirdqrcode.ui

import android.app.Application
import android.content.Context
import android.util.Log
import com.the.fast.third.thethirdqrcode.utils.BlackHelp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.UUID

class App:Application() {
    val black_url = "https://platoon.qreasyreadercreator.com/worthy/gunplay/birdie"
    companion object {
        private lateinit var app: App
        fun getAppContext() = app
    }
    override fun onCreate() {
        super.onCreate()
        app = this
        if(BlackHelp.uu_qr_code.isEmpty()){
            BlackHelp.uu_qr_code = UUID.randomUUID().toString()
        }
        getBlackList(this)
    }

    private fun getBlackList(context: Context) {
        if (BlackHelp.black_data_code.isNotEmpty()) {
            return
        }
        GlobalScope.launch(Dispatchers.IO) {
            getMapData(black_url,BlackHelp.cloakMapData(context), onNext = {
                Log.e("TAG", "The blacklist request is successful：$it")
                BlackHelp.black_data_code = it
            }, onError = {
                retry(it,context)
            })
        }
    }

    private fun retry(it: String,context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            delay(2333)
            Log.e("TAG", "The blacklist request failed：$it")
            getBlackList(context)
        }
    }

    private fun getMapData(url: String, map: Map<String, Any>, onNext: (response: String) -> Unit, onError: (error: String) -> Unit) {
        val queryParameters = StringBuilder()
        for ((key, value) in map) {
            if (queryParameters.isNotEmpty()) {
                queryParameters.append("&")
            }
            queryParameters.append(URLEncoder.encode(key, "UTF-8"))
            queryParameters.append("=")
            queryParameters.append(URLEncoder.encode(value.toString(), "UTF-8"))
        }

        val urlString = if (url.contains("?")) {
            "$url&$queryParameters"
        } else {
            "$url?$queryParameters"
        }

        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 15000

        try {
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                inputStream.close()
                onNext(response.toString())
            } else {
                onError("HTTP error: $responseCode")
            }
        } catch (e: Exception) {
            onError("Network error: ${e.message}")
        } finally {
            connection.disconnect()
        }
    }
}