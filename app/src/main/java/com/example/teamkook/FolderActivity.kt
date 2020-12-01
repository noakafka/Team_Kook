package com.example.teamkook

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_folder.*
import kotlinx.android.synthetic.main.add_folder_dialog.*

class FolderActivity() : AppCompatActivity() {

    lateinit var adapter : Folder_Adapter
    val mDatabase= FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)
        val i=intent
        val _id=i.getStringExtra("id") //로그인에서 전달한 아이디 전달받기

        // 여기에 DB에서 폴더리스트 받아오면 댐
        var folder_data : ArrayList<Folder> = ArrayList<Folder>()
        val database=mDatabase.getReference("Accounts").child(_id).child("Folder") //해당 유저 테이블
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                //check=true
                for(shot in snapshot.children){
                    if(shot.key.toString()=="Folder"){ //folder 이름 가져오기
                        val list=shot.getValue(Folder::class.java)
                        if (list != null) {
                            folder_data.add(list)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
                Log.e("database error",error.message)
            }
        })
//        folder_data.add(Folder("김치찌개", "주소"))
//        folder_data.add(Folder("한식", "주소"))

        adapter = Folder_Adapter(applicationContext, folder_data){ folder ->
            var intent = Intent(applicationContext, InFolderActivity::class.java)
            intent.putExtra("f_name", folder.folder_name)
            intent.putExtra("id", _id)
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
                        //폴더추가기능
                        val editText : EditText = v.findViewById(R.id.new_folder_name)
                        var new_fname = editText.text.toString()
                        val database=mDatabase.getReference("Accounts").child(_id)
                        database.child("Folder").child(new_fname).push()
                        Toast.makeText(applicationContext, new_fname, Toast.LENGTH_SHORT).show()
                    })
                .setView(v)

            val alert = dialogBuilder.create()
            alert.show()

        }
    }
}
