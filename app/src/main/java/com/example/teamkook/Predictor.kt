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
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
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

    private val labelGender = listOf("전복죽", "호박죽", "김치찌개", "닭계장", "동태찌개", "된장찌개", "순두부찌개", "갈비찜","계란찜",
        "김치찜", "꼬막찜", "닭볶음탕", "수육", "순대", "족발", "찜닭", "해물찜", "갈비탕","감자탕", "곰탕_설렁탕",
        "매운탕", "삼계탕", "추어탕", "고추튀김","새우튀김","오징어튀김","보쌈","떡꼬치",
        "계란국", "떡국_만두국", "무국","미역국", "북엇국", "시래기국", "육개장", "콩나물국", "감자전", "계란말이",
        "김치전", "동그랑땡", "생선전", "파전", "호박전", "갈치조림","감자조림", "고등어조림", "꽁치조림","두부조림",
        "땅콩조림", "메추리알장조림", "연근조림", "우엉조림", "장조림", "코다리조림", "갈비구이", "닭갈비",
        "더덕구이","떡갈비","불고기","황태구이", "감자채볶음","건새우볶음","고추장진미채볶음","두부김치","떡볶이",
        "라볶이", "멸치볶음", "소세지볶음", "어묵볶음", "오징어채볶음", "제육볶음", "쭈꾸미볶음","라면", "막국수",
        "물냉면", "비빔냉면", "수제비", "열무국수", "잔치국수", "짜장면", "짬뽕", "쫄면", "칼국수", "콩국수",
        "도토리묵","잡채", "콩나물무침", "홍어무침", "회무침", "김밥", "김치볶음밥", "누룽지", "비빔밥", "새우볶음밥",
        "알밥", "유부초밥","주먹밥")

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
            .add(ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.BILINEAR))
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
