package com.example.teamkook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_in_folder.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class InFolderActivity : AppCompatActivity() {

    lateinit var adapter : InFolderAdapter
    var ID : String = ""
    var folderName : String = ""

    val search1 = "https://www.googleapis.com/youtube/v3/videos?id="
    val search2 = "&key=AIzaSyAONAWO0Dta_zwAnMMBmNqkwBjCgSNGVSU&part=snippet"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_folder)
        if(intent.hasExtra("f_name")){
            folderName = intent.getStringExtra("f_name")
        }
        if(intent.hasExtra("id")){
            ID = intent.getStringExtra("id")
        }
        Log.i("infolderID", ID)
        back_btn.setOnClickListener {
            finish()
        }
        init()

    }
    fun init(){
        initSwipe()
        top_folder_name.text = folderName

        var array_in_folder : ArrayList<FolderMoreInfo> = ArrayList<FolderMoreInfo>()
        val rdatabase = FirebaseDatabase.getInstance().getReference("Accounts").child(ID).child("Folder")
            .child(folderName)
        rdatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){

                    val file = snap.getValue(Folder::class.java)



                    if(file!=null){
                        var id1 : String = file?.link!!.substring(file?.link!!.lastIndexOf("=")+1)
                        var id2 :String= file?.link!!.substring(file?.link!!.lastIndexOf("/")+1)
                        var id = id1;
                        if(id2.length < id1.length)
                            id = id2;
                        var youtubeURL = search1+id+search2

                        val client = OkHttpClient()
                        val request = Request.Builder().url(youtubeURL).build()

                        client.newCall(request).enqueue(object: Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                Log.i("유튜브 파싱", "fail")
                            }

                            override fun onResponse(call: Call, response: Response) {
                                val jsonData = response.body()?.string()
                                if(jsonData!=null){
                                    val jsonobj : JSONObject = JSONObject(jsonData)
                                    var json_arr : JSONArray = jsonobj.getJSONArray("items")
                                    var items : JSONObject = json_arr.getJSONObject(0)
                                    var snippet : JSONObject = items.getJSONObject("snippet")

                                    var youtube_title = snippet.getString("title")

                                    array_in_folder.add(FolderMoreInfo(file.folder_name, file.link, youtube_title ))
                                }
                            }

                        })
                    }
                }
                    //adapter.notifyDataSetChanged()
                Log.i("infolder 개수", array_in_folder.size.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })
        in_folder_rview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = InFolderAdapter(this, array_in_folder, ID)
        adapter.itemClickListener = object :InFolderAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                var i = Intent(this@InFolderActivity, PostActivity::class.java)
                i.putExtra("id", ID)
                i.putExtra("link", adapter.items[position].link)
                startActivity(i)
                finish()
            }

        }
        in_folder_rview.adapter = adapter

    }

    fun initSwipe(){
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    if(direction == ItemTouchHelper.LEFT){//왼쪽으로 밀었을 때
                        val rdatabase = FirebaseDatabase.getInstance().getReference("Accounts")
                        var deletequery = rdatabase.child(ID).child("Folder").child(folderName)
                            .orderByChild("link").equalTo(adapter.items[position].link)
                        deletequery.addListenerForSingleValueEvent(object :ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists()){//링크가 존재하는 경우
                                    for(snap in snapshot.children){
                                        snap.ref.removeValue()
                                        break
                                    }
                                }
                                adapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                //TODO("Not yet implemented")
                            }

                        })
                    }
                }

        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(in_folder_rview)
    }
}
