package com.the.fast.third.thethirdqrcode.utils
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import java.util.*
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.the.fast.third.thethirdqrcode.lib_zxing.activity.CodeUtils
import com.the.fast.third.thethirdqrcode.lib_zxing.camera.BitmapLuminanceSource
import com.the.fast.third.thethirdqrcode.lib_zxing.decoding.DecodeFormatManager
import java.io.OutputStream

object ThirdQrUtils {
    fun analyzeBitmap(bitmap: Bitmap,analyzeCallback: CodeUtils.AnalyzeCallback?) {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        options.inJustDecodeBounds = false
        var sampleSize = (options.outHeight / 400f).toInt()
        if (sampleSize <= 0) sampleSize = 1
        options.inSampleSize = sampleSize
        val multiFormatReader = MultiFormatReader()
        val hints = Hashtable<DecodeHintType, Any?>(2)
        var decodeFormats = Vector<BarcodeFormat?>()
        if (decodeFormats.isEmpty()) {
            decodeFormats = Vector()
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS)
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS)
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS)
        }
        hints[DecodeHintType.POSSIBLE_FORMATS] = decodeFormats
        multiFormatReader.setHints(hints)
        var rawResult: Result? = null
        try {
            rawResult = multiFormatReader.decodeWithState(
                BinaryBitmap(
                    HybridBinarizer(
                        BitmapLuminanceSource(bitmap)
                    )
                )
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        if (rawResult != null) {
            analyzeCallback?.onAnalyzeSuccess(bitmap, rawResult.text)
        } else {
            analyzeCallback?.onAnalyzeFailed()
        }
    }

    fun generateQrCodeFromText(text: String,w:Int,h:Int): Bitmap {
        return CodeUtils.createImage(text, w, h, null)
    }


    fun saveBitmapToGallery(context: Context,bitmap: Bitmap): Boolean {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    2333
                )
                return false
            }
        }
        val filename = "${System.currentTimeMillis()}.jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val resolver = context.contentResolver
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            imageUri?.let {
                val outputStream: OutputStream? = resolver.openOutputStream(it)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream!!)
                outputStream.close()
                Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun shareBitmap(bitmap: Bitmap,context: Context) {
        val uri: Uri = getImageUri(context, bitmap)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }


    private fun getImageUri(context: Context, bitmap: Bitmap): Uri {
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, "shared_image.png")

        try {
            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return FileProvider.getUriForFile(context, context.packageName + ".fileprovider", imageFile)
    }
}