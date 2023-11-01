package tech.baza_trainee.mama_ne_vdoma.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.util.Base64
import android.view.OrientationEventListener
import androidx.core.graphics.applyCanvas
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    fun resizeBitmap(
        scope: CoroutineScope,
        image: Bitmap,
        onSuccess: (Bitmap) -> Unit,
        onError: () -> Unit
    ) {
        if (image != DEFAULT_BITMAP) {
            scope.launch(Dispatchers.IO) {
                val newImage = if (image.height > IMAGE_HEIGHT)
                    Bitmap.createScaledBitmap(
                        image,
                        IMAGE_WIDTH,
                        IMAGE_HEIGHT,
                        true
                    )
                else image
                val newImageSize = getSize(newImage)
                if (newImageSize < IMAGE_SIZE) {
                    onSuccess(newImage)
                } else {
                    onError()
                }
            }
        }
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
                    val deviceOrientation = context.resources.configuration.orientation
                    val cameraManager =
                        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                    val frontCamera = getFrontCameraId(cameraManager).orEmpty()
                    val characteristics = cameraManager.getCameraCharacteristics(frontCamera)
                    val orientation = getJpegOrientation(characteristics, deviceOrientation)
                    val img = BitmapFactory.decodeStream(it)
                    return@withContext when (orientation) {
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

    private fun getSize(bitmap: Bitmap): Int {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray().size
    }

    private fun getFrontCameraId(cameraManager: CameraManager?): String? {
        cameraManager?.cameraIdList?.forEach {
            val characteristics = cameraManager.getCameraCharacteristics(it)
            val cameraFacing = characteristics.get(CameraCharacteristics.LENS_FACING)

            if (cameraFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                return it
            }
        }

        // Front camera not found
        return null
    }

    private fun getJpegOrientation(
        cameraCharacteristics: CameraCharacteristics,
        deviceOrientation: Int
    ): Int {
        var _deviceOrientation = deviceOrientation
        if (_deviceOrientation == OrientationEventListener.ORIENTATION_UNKNOWN) return 0
        val sensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0

        // Round device orientation to a multiple of 90
        _deviceOrientation = (_deviceOrientation + 45) / 90 * 90

        // Reverse device orientation for front-facing cameras
        val facingFront =
            cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
        if (facingFront) _deviceOrientation = -_deviceOrientation

        // Calculate desired JPEG orientation relative to camera orientation to make
        // the image upright relative to the device orientation
        return (sensorOrientation + _deviceOrientation + 360) % 360
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
        private const val IMAGE_SIZE = 1 * 1024 * 1024
        private const val IMAGE_HEIGHT = 960
        private const val IMAGE_WIDTH = 360

        val DEFAULT_BITMAP = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888).applyCanvas {
            drawColor(Color.GRAY)
        }
    }
}