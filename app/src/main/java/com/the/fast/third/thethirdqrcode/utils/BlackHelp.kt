package com.the.fast.third.thethirdqrcode.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import com.the.fast.third.thethirdqrcode.ui.App

object BlackHelp {
    private val sp by lazy {
        App.getAppContext().getSharedPreferences(
            "qr_code",
            Context.MODE_PRIVATE
        )
    }
    var uu_qr_code = ""
        set(value) {
            sp.edit().run {
                putString("uu_qr_code", value)
                commit()
            }
            field = value
        }
        get() = sp.getString("uu_qr_code", "").toString()
    var black_data_code = ""
        set(value) {
            sp.edit().run {
                putString("black_data_code", value)
                commit()
            }
            field = value
        }
        get() = sp.getString("black_data_code", "").toString()

    var scan_data = ""
        set(value) {
            sp.edit().run {
                putString("scan_data", value)
                commit()
            }
            field = value
        }
        get() = sp.getString("scan_data", "").toString()

    var create_data = ""
        set(value) {
            sp.edit().run {
                putString("create_data", value)
                commit()
            }
            field = value
        }
        get() = sp.getString("create_data", "").toString()
    fun cloakMapData(context: Context): Map<String, Any> {
        return mapOf<String, Any>(
            //distinct_id
            "hillel" to (uu_qr_code),
            //client_ts
            "mauve" to (System.currentTimeMillis()),
            //device_model
            "pasty" to Build.MODEL,
            //bundle_id
            "betrayal" to ("com.qr.easy.reader.creator"),
            //os_version
            "scrape" to Build.VERSION.RELEASE,
            //gaid
            "caret" to "",
            //android_id
            "picket" to context.getAppId(),
            //os
            "insecure" to "braun",
            //app_version
            "varistor" to context.getAppVersion(),
        )
    }

    private fun Context.getAppVersion(): String {
        try {
            val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)

            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "Version information not available"
    }

    private fun Context.getAppId(): String {
        return Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }
}