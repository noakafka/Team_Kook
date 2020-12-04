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
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_b.*
import kotlinx.android.synthetic.main.search_dialog.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class Fragment_B(var c: Context) : Fragment() {

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

        add_realtime_key()

        cam_btn.setOnClickListener {
            val nextIntent = Intent(c, CamActivity::class.java)
            startActivityForResult(nextIntent, 0)

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
        val database=mDatabase.getReference("Accounts").child(ID)
        search.setOnClickListener {

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
            database.child("search word").push().setValue(search_edit.text.toString())

                //검색어 합치기
                for (i in 0 until checkboxes.size) {
                    search_str+=checkboxes[i]+" "
                }
                checkboxes.clear()



            //검색어 합치기
            search_str+=search_edit.text.toString()

            //검색어 확인
            Toast.makeText(c, search_str,Toast.LENGTH_SHORT).show()

            //최종검색어(search_str) 화면 넘기기
//            val searchIntent=Intent(c, )
//            searchIntent.putExtra("str",search_str)
//            startActivity(searchIntent)
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
