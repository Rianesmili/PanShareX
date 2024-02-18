package fr.mastersime.pansharex.setup

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import fr.mastersime.pansharex.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.Locale


private const val IMAGE_WEIGHT = 224
private const val IMAGE_HEIGHT = 224

// Define the classes
private val classes = arrayOf(
    "Limitation_30_km",
    "Limitation_40_km",
    "Limitation_50_km",
    "Limitation_60_km",
    "Limitation_70_km",
    "Limitation_80_km",
    "Panneau_de_ville",
    "Sens_Interdit",
    "Stationnement_Interdit"
)

val min_accepted = 0.3


fun classifyImage(bitmap: Bitmap, context: Context ) : String {
    // Load the optimized model
    val model = Model.newInstance(context)

    // Preprocess the image
    val inputBuffer = preprocessImage(bitmap)

    // Create input tensor
    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
    inputFeature0.loadBuffer(inputBuffer)

    // Run inference and get result
    val outputs = model.process(inputFeature0)
    val outputFeature0 = outputs.outputFeature0AsTensorBuffer

    // Extract the most likely class and probability
    val predictedClass = extractMostLikelyClass(outputFeature0.floatArray)
    val probability = outputFeature0.floatArray[predictedClass]

    // Release resources
    model.close()

    val className = classes[predictedClass]

    Log.d("take Picture", "Hello From className: $className")
    Log.d("take Picture", "Hello From outputFeature0: ${outputFeature0.floatArray.contentToString()}")

    if (probability < min_accepted) {
        return "Unknown"
    }
    // Return classification result
    return className
}

private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
    // Resize the image
    val resizedImage = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

    // Allocate a ByteBuffer for the preprocessed data
    val buffer = ByteBuffer.allocateDirect(224 * 224 * 3 * 4) // 4 bytes per float

    // Normalize pixel values and write to the buffer
    buffer.order(ByteOrder.nativeOrder())
    for (y in 0 until resizedImage.height) {
        for (x in 0 until resizedImage.width) {
            val pixel = resizedImage.getPixel(x, y)
            buffer.putFloat(Color.red(pixel) / 255.0f)
            buffer.putFloat(Color.green(pixel) / 255.0f)
            buffer.putFloat(Color.blue(pixel) / 255.0f)
        }
    }

    // Flip the byte order if necessary
    if (ByteOrder.nativeOrder() != ByteOrder.LITTLE_ENDIAN) {
        buffer.flip()
    }

    return buffer
}


private fun extractMostLikelyClass(output: FloatArray): Int {
    var maxIndex = 0
    var maxProb = output[0]
    for (i in 1 until output.size) {
        if (output[i] > maxProb) {
            maxIndex = i
            maxProb = output[i]
        }
    }
    return maxIndex
}

fun addImageToGallery(context: Context, photoFile: File, bitmap: Bitmap) {
    // Add the image to the gallery
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, photoFile.name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES
            )
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
    }

    val uri = context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )

    context.contentResolver.openOutputStream(uri!!)?.use { outputStream ->
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            outputStream
        )
    } ?: Log.e("CameraView", "Failed to open output stream")

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        contentValues.clear()
        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
        context.contentResolver.update(uri, contentValues, null, null)
    }
}

fun createPhotoFile(outputDirectory: File?): File {
    return File(
        outputDirectory,
        SimpleDateFormat(Screen.FILENAME_FORMAT, Locale.FRANCE)
            .format(System.currentTimeMillis()) + ".jpg"
    )
}

fun correctBitmapOrientation(photoFile: File, bitmap: Bitmap): Bitmap {
    val exif = ExifInterface(photoFile.absolutePath)
    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> bitmap.rotate(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> bitmap.rotate(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> bitmap.rotate(270f)
        else -> bitmap
    }
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
        bitmap.copy(Bitmap.Config.ARGB_8888, true)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
