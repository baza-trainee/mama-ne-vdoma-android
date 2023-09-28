package tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

fun Uri.decodeBitmap(
    contentResolver: ContentResolver
): Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, this))
    } else {
        MediaStore.Images.Media.getBitmap(contentResolver, this)
    }