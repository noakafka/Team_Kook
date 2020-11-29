package com.example.teamkook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_in_folder.*

class InFolderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_folder)
        if(intent.hasExtra("f_name")){
            top_folder_name.text = intent.getStringExtra("f_name")
        }
        back_btn.setOnClickListener {
            finish()
        }
    }
}
