package com.example.teamkook


import android.app.ActionBar
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
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
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.core.view.marginBottom
import kotlinx.android.synthetic.main.fragment_b.*
import kotlinx.android.synthetic.main.search_dialog.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class Fragment_B(var c :Context) : Fragment() {

    val arr = arrayListOf<String>("1", "2", "3","4", "5", "6","7", "8", "9","10")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_b, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        add_realtime_key()

        cam_btn.setOnClickListener {
            val nextIntent = Intent(c, CamActivity::class.java)
            startActivity(nextIntent)
        }

        search_checkbox.setOnClickListener {
            /*var builder = AlertDialog.Builder(c)
            builder.setTitle("검색 조건")
            builder.setIcon(R.mipmap.search)

            var v1 = layoutInflater.inflate(R.layout.search_dialog, null)
            var c1 = v1.findViewById<CheckBox>(R.id.checkbox1)
            var c2 = v1.findViewById<CheckBox>(R.id.checkbox2)
            var c3 = v1.findViewById<CheckBox>(R.id.checkbox3)
            var c4 = v1.findViewById<CheckBox>(R.id.checkbox4)
            var c5 = v1.findViewById<CheckBox>(R.id.checkbox5)
            var c6 = v1.findViewById<CheckBox>(R.id.checkbox6)
            var c7 = v1.findViewById<CheckBox>(R.id.checkbox7)

            builder.setView(v1)


            builder.setPositiveButton("확인"){dialogInterface, i ->

            }
            builder.setNegativeButton("취소"){dialogInterface, i ->

            }

            builder.show()
            */

            val dialogBuilder = AlertDialog.Builder(activity!!)
            var v1 = layoutInflater.inflate(R.layout.search_dialog, null)


            dialogBuilder.setView(v1)
                .setCancelable(false)
                .setPositiveButton("확인", DialogInterface.OnClickListener{
                    dialog, id ->
                    dialog.dismiss()
                })
            val alert = dialogBuilder.create()
            alert.setTitle("검색 조건")
            alert.show()
        }
    }

    fun add_realtime_key(){
        for(str in arr){
            val text = TextView(c)
            text.text = str
            text.textSize = 22f
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            params.bottomMargin = 20
            text.layoutParams = params
            realtime_keywords.addView(text)
        }
    }


}
