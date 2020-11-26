package com.example.teamkook

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.os.Trace
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeOp.ResizeMethod
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.MappedByteBuffer
import kotlin.math.min


class Predictor(val context: Context) {

    private val IMAGE_MEAN = 127.5f
    private val IMAGE_STD = 127.5f

    private val PROBABILITY_MEAN = 0F
    private val PROBABILITY_STD = 255F

    /** The loaded TensorFlow Lite model.  */
    private val tfliteModel: MappedByteBuffer? = null

    /** Image size along the x axis.  */
    private val imageSizeX = 224

    /** Image size along the y axis.  */
    private val imageSizeY = 224

    private val channelSize = 3

    /** An instance of the driver class to run model inference with Tensorflow Lite.  */
    private val tflite: Interpreter = Interpreter(
        FileUtil.loadMappedFile(context, "learned.tflite"))

    /** Options for configuring the Interpreter.  */
    private val tfliteOptions = Interpreter.Options()


    /** Output probability TensorBuffer.  */
    private val outputProbabilityBuffer: TensorBuffer = TensorBuffer.createFixedSize(
        tflite.getOutputTensor(0).shape(),
        tflite.getOutputTensor(0).dataType()
    )

    /** Processer to apply post processing of the output probability.  */
    private val probabilityProcessor: TensorProcessor =
        TensorProcessor.Builder().build()

    private val labelGender = listOf("apple pie",  "back ribs", "cheese cake","curry")

    /** Loads input image, and applies preprocessing.  */
    private fun loadImage(
        bitmap: Bitmap,
        inputImageBuffer: TensorImage
    ): TensorImage? {
        // Loads bitmap into a TensorImage.

        inputImageBuffer.load(bitmap)

        // Creates processor for the TensorImage.
        val cropSize = min(bitmap.width, bitmap.height)


        val imageProcessor: ImageProcessor = ImageProcessor.Builder()
            .add(ResizeWithCropOrPadOp(cropSize, cropSize))
            .add(ResizeOp(imageSizeX, imageSizeY, ResizeMethod.BILINEAR))
            .add(NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD))
            .build()

        return imageProcessor.process(inputImageBuffer)
    }

    /** Runs inference and returns the classification results.  */
    fun recognizeImage(bitmap: Bitmap): Map<String, Float> {

        Trace.beginSection("recognizeImage")
        Trace.beginSection("loadImage")
        val startTimeForLoadImage = SystemClock.uptimeMillis()
        val initialInputImageBuffer = TensorImage(tflite.getInputTensor(0).dataType())
        val inputImageBuffer = loadImage(bitmap, initialInputImageBuffer)
        val endTimeForLoadImage = SystemClock.uptimeMillis()
        Trace.endSection()

        Trace.beginSection("runInference")
        val startTimeForReference = SystemClock.uptimeMillis()
        tflite.run(inputImageBuffer?.buffer, outputProbabilityBuffer.buffer.rewind())
        val endTimeForReference = SystemClock.uptimeMillis()
        Trace.endSection()

        val labeledProbability: Map<String, Float> =
            TensorLabel(
                labelGender,
                probabilityProcessor.process(outputProbabilityBuffer)
            ).mapWithFloatValue

        Trace.endSection()



        return labeledProbability
    }
}