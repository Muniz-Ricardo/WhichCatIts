package com.ricardomuniz.whichcatits.domain.usecase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.ricardomuniz.whichcatits.R
import java.io.File
import java.io.FileOutputStream

class ShareImageUseCase(
    private val context: Context, private val activity: Activity
) {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 144
    }

    suspend fun execute(bitmap: Bitmap) {
        if (checkStoragePermission()) {
            shareImage(bitmap)
        } else {
            requestStoragePermission()
        }
    }

    private fun checkStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun shareImage(bitmap: Bitmap) {
        val cachePath = File(context.externalCacheDir, "images")
        cachePath.mkdirs()

        val stream = FileOutputStream("$cachePath/image.png")
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        val uri = FileProvider.getUriForFile(
            context,
            "com.ricardomuniz.whichcatits.fileprovider",
            File("$cachePath/image.png")
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
        }

        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        context.startActivity(
            Intent.createChooser(
                shareIntent,
                activity.getString(R.string.text_share_image)
            )
        )
    }
}