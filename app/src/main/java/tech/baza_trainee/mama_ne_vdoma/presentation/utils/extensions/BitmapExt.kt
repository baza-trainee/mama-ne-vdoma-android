package tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap.encodeToBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()

    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun Bitmap.getSize(): Int {
    val byteArrayOutputStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray().size
}

private const val PNG_QUALITY = 50