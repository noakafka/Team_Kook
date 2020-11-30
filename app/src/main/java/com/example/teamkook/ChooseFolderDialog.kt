package com.example.teamkook

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.internal.v

class ChooseFolderDialog (context: Context){
    lateinit var recyclerAdapter : PostAddAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var cancelBtn : Button
    lateinit var addBtn : Button
    val dlg = Dialog(context)
    val context = context
    var currentChoose : Int = -1
    fun init(ID : String, url : String, title : String){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.post_add_folder_dialog)

        recyclerView = dlg.findViewById(R.id.choose_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerAdapter = PostAddAdapter(ArrayList<String>())
        recyclerAdapter.itemClickListener = object : PostAddAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                //recyclerview에서 폴더 선택했을 때
                currentChoose = position

            }

        }


        cancelBtn = dlg.findViewById(R.id.post_cancel)
        addBtn = dlg.findViewById(R.id.post_add_folder)

        cancelBtn.setOnClickListener {
            dlg.dismiss()
        }
        addBtn.setOnClickListener { //추가할 폴더 선택한 경우
            if(currentChoose == -1){
                Toast.makeText(context, "폴더를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{//폴더에 추가

            }
        }
        dlg.show()
    }
}