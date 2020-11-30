package com.example.teamkook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_in_folder.*

class InFolderActivity : AppCompatActivity() {

    lateinit var adapter : InFolderAdapter
    var ID : String = "ldh1"
    var folderName : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_folder)
        if(intent.hasExtra("f_name")){
            folderName = intent.getStringExtra("f_name")
        }
        if(intent.hasExtra("id")){
            ID = intent.getStringExtra("id")
        }
        back_btn.setOnClickListener {
            finish()
        }
        init()

    }
    fun init(){
        top_folder_name.text = folderName

        var array_in_folder : ArrayList<Folder> = ArrayList<Folder>()
        val rdatabase = FirebaseDatabase.getInstance().getReference("Accounts").child(ID).child("Folder")
            .child(folderName)
        rdatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    val file = snap.getValue(Folder::class.java)
                    if(file!=null){
                        array_in_folder.add(file)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })
        in_folder_rview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = InFolderAdapter(this, array_in_folder)
        adapter.itemClickListener = object :InFolderAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                var i = Intent(this@InFolderActivity, FolderActivity::class.java)
                i.putExtra("id", ID)
                i.putExtra("link", adapter.items[position].link)
                startActivity(i)
                finish()
            }

        }
        in_folder_rview.adapter = adapter

    }
}
