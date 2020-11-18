package com.example.teamkook

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_cam.*
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import android.util.Log
import android.widget.Toast
import kotlin.math.log
import kotlin.random.Random


class CamActivity : AppCompatActivity() {
    val OPEN_GALLERY = 100
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cam)

        btn_picture.setOnClickListener {
            startCapture()
        }

        btn_gallery.setOnClickListener {
            openGallery()
        }

    }

    fun openGallery(){
        val intent :Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)
    }

    fun startCapture(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(applicationContext.packageManager)?.also {
                val photoFile: File? = try{
                    createImageFile()
                }catch(ex:IOException){
                    null
                }
                photoFile?.also{
                    val photoURI : Uri = FileProvider.getUriForFile(
                        applicationContext,
                        "com.example.teamkook.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile() : File {
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply{
            currentPhotoPath = absolutePath
        }
    }

    fun rotateImage(source : Bitmap, angle : Float): Bitmap? {
        var matrix : Matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source,0,0, source.width, source.height,matrix, true)
    }



    fun getOrientation(filepath : String?): Int{
        val exif : ExifInterface = ExifInterface(filepath)
        Log.d("shit", filepath)
        var orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        Log.d("shit", orientation.toString())
        when(orientation){
            ExifInterface.ORIENTATION_ROTATE_90 -> return 90
            ExifInterface.ORIENTATION_ROTATE_180 -> return 180
            ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            else -> return 0
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val file = File(currentPhotoPath)

            val decode = ImageDecoder.createSource(applicationContext.contentResolver,
                Uri.fromFile(file))
            val bitmap = ImageDecoder.decodeBitmap(decode)
            img_picture.setImageBitmap(bitmap)

        }

        if(requestCode == OPEN_GALLERY && resultCode == Activity.RESULT_OK){
            var dataUri = data?.data
            var ori = data?.data.toString()
            //var path = currentPhotoPath + dataUri?.path
            //var ori = getOrientation(path)
            Toast.makeText(applicationContext, ori, Toast.LENGTH_SHORT).show()
            var bitmap : Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, dataUri)

            val matrix = Matrix()
            matrix.postRotate(90F)
            bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.width, bitmap.height, matrix, true)

            img_picture.setImageBitmap(bitmap)
            //var matrix : Matrix = Matrix()
            //matrix.setRotate(getOrientation(getRealPathFromURI(dataUri!!)).toFloat(), (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat())
            //img_picture.setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true))

            //var dataUri = data?.data
            //val file = File(absolutelyPath(dataUri!!))
            //val decode = ImageDecoder.createSource(applicationContext.contentResolver, Uri.fromFile(file))
            //val bitmap = ImageDecoder.decodeBitmap(decode)



        }

    }

}
