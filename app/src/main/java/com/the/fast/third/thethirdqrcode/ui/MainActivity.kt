package com.the.fast.third.thethirdqrcode.ui

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.the.fast.third.thethirdqrcode.databinding.ActivityMainBinding
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.tvSetting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
        binding.imgSacn.setOnClickListener {
            requestCameraPermission()
        }
        binding.imgCreate.setOnClickListener {
            startActivity(Intent(this, QrCreateActivity::class.java))
        }
    }

    private val CAMERA_PERMISSION_CODE = 26747

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        } else {
            openCamera()
        }
    }

    // 处理权限请求结果
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                showPermissionDeniedDialog()
            }
        }
    }

    fun openCamera() {
        startActivity(Intent(this, QrScanActivity::class.java))
    }



    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission denied")
            .setMessage("You need to grant camera permissions to use this feature")
            .setPositiveButton("Go to settings") { dialog, _ ->
                goToAppSettings()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun goToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

}