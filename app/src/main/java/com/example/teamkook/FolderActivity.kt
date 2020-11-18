package com.example.teamkook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_folder.*

class FolderActivity() : AppCompatActivity() {

    lateinit var adapter : Folder_Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)

        var folder_data : ArrayList<Folder> = ArrayList<Folder>()
        folder_data.add(Folder("김치찌개", "주소"))
        folder_data.add(Folder("한식", "주소"))

        adapter = Folder_Adapter(applicationContext, folder_data)
        var layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        folder_rview.layoutManager = layoutManager
        folder_rview.adapter = adapter
    }

}
