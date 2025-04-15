package com.uca.tflitetest
import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream

class TFLiteClassifier(context: Context) {

    private var interpreter: Interpreter
    private val classNames = arrayOf(
        "Actinic keratosis or Intraepithelial Carcinoma",
        "Basal Cell Carcinoma",
        "Benign Keratosis-like Lesions",
        "Dermatofibroma",
        "Melanoma",
        "Melanocytic Nevi",
        "Vascular Lesions"
    )

    init {
        interpreter = Interpreter(loadModelFile(context, "model.tflite"))
    }

    private fun loadModelFile(context: Context, modelPath: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun classify(image: ByteBuffer): Pair<String, Float> {
        val result = Array(1) { FloatArray(7) }
        interpreter.run(image, result)
        val maxIdx = result[0].indices.maxByOrNull { result[0][it] } ?: -1
        val confidence = result[0][maxIdx]
        val className = classNames[maxIdx]
        return Pair(className, confidence)
    }
}

