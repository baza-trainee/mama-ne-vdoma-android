package tech.baza_trainee.mama_ne_vdoma.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.decodeBitmap
import java.io.ByteArrayOutputStream

class BitmapHelper(private val context: Context) {

    fun encodeToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun getSize(bitmap: Bitmap): Int {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray().size
    }

    fun bitmapFromUri(uri: Uri): Bitmap {
        return uri.decodeBitmap(context.contentResolver)
    }

    companion object {

        private const val PNG_QUALITY = 75
    }
}