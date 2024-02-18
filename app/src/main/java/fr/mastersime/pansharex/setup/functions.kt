package fr.mastersime.pansharex.setup

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import fr.mastersime.pansharex.ml.ModelOptimisationExperimentalSparsity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Locale

private const val IMAGE_WEIGHT = 224
private const val IMAGE_HEIGHT = 224

// Define the classes
private val classes = arrayOf(
    "Limitation_30",
    "Limitation_40",
    "Limitation_50",
    "Limitation_60",
    "Limitation_70",
    "Limitation_80",
    "panneau_danger",
    "panneau_de_ville",
    "sens_interdit",
    "stationnement_interdit"
)

fun runModelInference(context: Context, bitmap: Bitmap): String {

    // Load the model
    val model = ModelOptimisationExperimentalSparsity.newInstance(context)

    // Create an ImageProcessor object
    val imageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(IMAGE_HEIGHT, IMAGE_HEIGHT, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
        .add(NormalizeOp(0f, 255f)) // Normalize pixel values to [0,1]
        .build()

    // Preprocess the image and convert it into a TensorImage for classification
    val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

    // Convert the TensorImage to a ByteBuffer
    val byteBuffer = tensorImage.buffer.rewind() as ByteBuffer

    // Creates inputs for reference.
    val inputFeature0 =
        TensorBuffer.createFixedSize(intArrayOf(1, IMAGE_HEIGHT, IMAGE_HEIGHT, 3), DataType.FLOAT32)
    inputFeature0.loadBuffer(byteBuffer)

    // Runs model inference and gets result.
    val outputs = model.process(inputFeature0)
    val outputFeature0 = outputs.outputFeature0AsTensorBuffer

    // Get the index of the class with the highest confidence
    val maxIndex =
        outputFeature0.floatArray.indices.maxByOrNull { outputFeature0.floatArray[it] }
            ?: -1

    Log.d(
        "take Picture",
        "Hello From outputFeature0: ${outputFeature0.floatArray.contentToString()}"
    )

    val className = classes[maxIndex]

    // Releases model resources if no longer used.
    model.close()

    return className
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
