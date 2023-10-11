package tech.baza_trainee.mama_ne_vdoma.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.util.Base64
import androidx.core.graphics.applyCanvas
import okhttp3.ResponseBody
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.decodeBitmap
import java.io.ByteArrayOutputStream
import java.io.File

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

    fun bitmapToMultiPart(bitmap: Bitmap): File {
        val filename = AVATAR
        context.openFileOutput(filename, Context.MODE_PRIVATE)
            .use {
                bitmap.compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, it)
                it.flush()
                it.close()
            }
        return File(context.filesDir, filename)
    }

    fun bitmapFromBody(body: ResponseBody?): Bitmap {
        body?.let {
            val fileContents = body.bytes()
            return BitmapFactory.decodeByteArray(fileContents, 0, fileContents.size)
        } ?: return DEFAULT_BITMAP
    }

    companion object {

        private const val PNG_QUALITY = 75
        private const val AVATAR = "avatar.png"

        val DEFAULT_BITMAP = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888).applyCanvas {
            drawColor(Color.RED)
        }
    }
}