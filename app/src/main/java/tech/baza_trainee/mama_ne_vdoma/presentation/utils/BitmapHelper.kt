package tech.baza_trainee.mama_ne_vdoma.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Base64
import androidx.core.graphics.applyCanvas
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
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

    fun bitmapToFile(bitmap: Bitmap): File {
        val filename = "${System.currentTimeMillis()}_$AVATAR"
        context.openFileOutput(filename, Context.MODE_PRIVATE)
            .use {
                bitmap.compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, it)
                it.flush()
                it.close()
            }
        return File(context.filesDir, filename)
    }

    fun bitmapUriFromBody(body: ResponseBody?): Uri {
        body?.let {
            val fileContents = body.bytes()
            val bitmap = BitmapFactory.decodeByteArray(fileContents, 0, fileContents.size)
            return bitmapToFile(bitmap).toUri()
        } ?: return Uri.EMPTY
    }

    suspend fun bitmapFromUri(selectedImage: Uri): Bitmap {
        return withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(selectedImage)?.let {
                    val img =  BitmapFactory.decodeStream(it)
                    val orientation = ExifInterface(it).getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )
                    return@withContext when(orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
                        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
                        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
                        else -> img
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext DEFAULT_BITMAP
        }
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    companion object {

        private const val PNG_QUALITY = 75
        private const val AVATAR = "avatar.png"

        val DEFAULT_BITMAP = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888).applyCanvas {
            drawColor(Color.GRAY)
        }
    }
}