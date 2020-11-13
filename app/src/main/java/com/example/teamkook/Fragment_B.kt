package com.example.teamkook


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.fragment_b.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class Fragment_B(var c : Context) : Fragment() {

    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_b, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_picture.setOnClickListener {
            startCapture()
        }


    }

    fun startCapture(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(c.packageManager)?.also {
                val photoFile: File? = try{
                    createImageFile()
                }catch(ex:IOException){
                    null
                }
                photoFile?.also{
                    val photoURI : Uri = FileProvider.getUriForFile(
                        c,
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
    private fun createImageFile() : File{
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply{
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val file = File(currentPhotoPath)

            val decode = ImageDecoder.createSource(c.contentResolver,
                Uri.fromFile(file))
            val bitmap = ImageDecoder.decodeBitmap(decode)
            img_picture.setImageBitmap(bitmap)

        }
    }
}
