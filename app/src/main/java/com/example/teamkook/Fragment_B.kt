package com.example.teamkook


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.youtube.player.internal.c
import com.google.android.youtube.player.internal.i
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_in_folder.*
import kotlinx.android.synthetic.main.fragment_b.*
import kotlinx.android.synthetic.main.search_dialog.*
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class Fragment_B(var c: Context) : Fragment() {

    val APIKEY = "AIzaSyCLAfLcEQvBA5zrat3nReaT28iI-E3QH5c"
    //val APIKEY = "AIzaSyAONAWO0Dta_zwAnMMBmNqkwBjCgSNGVSU"
    val mDatabase= FirebaseDatabase.getInstance()
    lateinit var ID:String
    var checkboxes= ArrayList<String>() //체크박스 항목 추가
    val data= arrayListOf<String>("초보","매운","백종원 레시피","김수미 레시피","만개의 레시피","다이어트","비건")
    var search_str=""
    var rank = arrayListOf<String>()
    var arr = arrayListOf<String>("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(activity!=null){
            val intent=activity!!.intent
            if(intent!=null){
                ID=intent.getStringExtra("id")
            }
        }
        return inflater.inflate(R.layout.fragment_b, container, false)
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        search_result_rview.layoutManager = LinearLayoutManager(c, LinearLayoutManager.VERTICAL, false)
        add_realtime_key()

        cam_btn.setOnClickListener {
            val nextIntent = Intent(c, CamActivity::class.java)
            startActivityForResult(nextIntent, 0)

        }


        search_checkbox.setOnClickListener {

            val dialogBuilder = AlertDialog.Builder(activity!!)
//            var v1 = layoutInflater.inflate(R.layout.search_dialog, null)
//            dialogBuilder.setView(v1)

            dialogBuilder.setTitle("검색 조건")
                .setMultiChoiceItems(R.array.filter,null,DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                    if(isChecked){
                        checkboxes.add(data[which])
                    }else if(checkboxes.contains(data[which])){
                        checkboxes.remove(data[which])
                    }
                }).setCancelable(false)
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        //dialog.dismiss()
                       // Toast.makeText(c,checkboxes[0]+ checkboxes[1],Toast.LENGTH_SHORT).show()
                    })

            val alert = dialogBuilder.create()
            alert.show()
        }

        manageDB()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var s : String? = data!!.getStringExtra("learned_food")
        search_edit.setText(s)
        Toast.makeText(c, s, Toast.LENGTH_SHORT).show()
    }

    fun manageDB(){
        lateinit var adapter : Research_Adapter
        lateinit var items : ArrayList<result_items>

        val database=mDatabase.getReference("Accounts").child(ID)
        search.setOnClickListener {

            realtime_text.visibility = View.GONE
            realtime_layout.visibility = View.GONE
            search_result_rview.visibility = View.VISIBLE

            /*
            database.child("search word").addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.childrenCount>10){ //list 10개 유지
                        for(shot in snapshot.children) {
                            database.child("search word").child(shot.key.toString()).removeValue()
                            break
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    //TODO("Not yet implemented")
                }
            })
            */
            //database.child("search word").push().setValue(search_edit.text.toString())



            //검색어 합치기
            search_str+=search_edit.text.toString()

            for (i in 0 until checkboxes.size) {
                search_str+=checkboxes[i]+" "
            }
            checkboxes.clear()

            var final_search_str = search_str
            search_str = ""
            //검색어 확인
            Toast.makeText(c, final_search_str,Toast.LENGTH_SHORT).show()





            //val url1 = "https://www.googleapis.com/youtube/v3/search?/q=김치찌개&part=snippet&key=AIzaSyAONAWO0Dta_zwAnMMBmNqkwBjCgSNGVSU&maxResults=10"
            val url1 = "https://www.googleapis.com/youtube/v3/search?q="+ final_search_str + "레시피" + "&part=snippet&key=" + APIKEY + "&maxResults=10"
            println("final : " + final_search_str)
            val client = OkHttpClient()
            val request = Request.Builder().url(url1).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call : Call, e : IOException){

                }
                override fun onResponse(call:Call, response: Response){
                    val body = response.body()!!.string()
                    println(body)

                    val gson = GsonBuilder().create()
                    val parser = JsonParser()
                    val rootObj = parser.parse(body)
                        .getAsJsonObject()



                    val s_result =  gson.fromJson(rootObj, search_json::class.java)
                    val items = s_result.items


                    adapter = Research_Adapter(c, items)
                    adapter.itemClickListener = object :Research_Adapter.OnItemClickListener{
                        override fun onItemClick(view: View, position: Int) {
                            Toast.makeText(c, "hi", Toast.LENGTH_SHORT).show()
                        }
                    }
                    activity!!.runOnUiThread {
                        search_result_rview.adapter = adapter
                    }


                }
            })


        }
    }

    fun add_realtime_key(){

        // get data to arr

        // db에서 max cnt를 불러오기


        val CountListener = object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var food = arrayListOf<food_count>()
                for(item in dataSnapshot.children){
                    var temp_food : food_count = food_count(item.key.toString(), item.child("count").value.toString().toInt())
                    food.add(temp_food)
                    //Log.e("db", item.child("count").value.toString())
                }
                //food를 sort as "count" DESC
                food.sortWith(Comparator { data1, data2 -> data2.count-data1.count})
                for(i in 0..9){
                    Log.e("db", food[i].name + " : " + food[i].count.toString())
                    rank.add(food[i].name)
                }
                for(i in rank.indices){
                    val ttext = TextView(c)
                    ttext.id = i
                    if(i == 9){
                        ttext.text = arr[i] + ". " + rank[i]
                    }
                    else{
                        ttext.text = arr[i] + ".   " + rank[i]
                    }

                    ttext.textSize = 22f
                    ttext.setOnClickListener {
                        search_edit.setText(rank[ttext.id])
                    }
                    val params = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    params.bottomMargin = 20
                    ttext.layoutParams = params
                    realtime_keywords.addView(ttext)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        val database = mDatabase.getReference("Foods")
        database.addValueEventListener(CountListener)

    }


}
