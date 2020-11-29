package com.example.teamkook

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_folder.*
import kotlinx.android.synthetic.main.add_folder_dialog.*

class FolderActivity() : AppCompatActivity() {

    lateinit var adapter : Folder_Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)

        // 여기에 DB에서 폴더리스트 받아오면 댐
        var folder_data : ArrayList<Folder> = ArrayList<Folder>()
        folder_data.add(Folder("김치찌개", "주소"))
        folder_data.add(Folder("한식", "주소"))

        adapter = Folder_Adapter(applicationContext, folder_data){ folder ->
            var intent = Intent(applicationContext, InFolderActivity::class.java)
            intent.putExtra("f_name", folder.folder_name)
            startActivityForResult(intent, 0)
        }
        var layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        folder_rview.layoutManager = layoutManager
        folder_rview.adapter = adapter

        fav_btn_d.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val v = layoutInflater.inflate(R.layout.add_folder_dialog, null)
            dialogBuilder.setTitle("새 폴더 만들기")
                .setCancelable(false)
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        //폴더추가기능 (도헌)
                        val editText : EditText = v.findViewById(R.id.new_folder_name)
                        var new_fname = editText.text.toString()
                        Toast.makeText(applicationContext, new_fname, Toast.LENGTH_SHORT).show()
                    })
                .setView(v)

            val alert = dialogBuilder.create()
            alert.show()

        }
    }
}
