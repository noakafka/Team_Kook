package com.example.teamkook


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.youtube.player.internal.c
import com.google.android.youtube.player.internal.i
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_my_review.*
import kotlinx.android.synthetic.main.fragment_b.*
import kotlinx.android.synthetic.main.fragment_d.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class Fragment_D(var c: Context) : Fragment() {

    lateinit var adapter :MyFolderAdapter
    var folderNames : ArrayList<String> = ArrayList<String>()
    //val arr = arrayListOf<String>("이대로 끓이니까 너무 맛있었어요~!!!", "제 기준 굴소스를 추가하니까 더 맛있더라구요", "에어프라이기로 하면 더 쉽습니당~!~!","ㅜㅜ 제 입맛에는 아닌가 봐요.. 별로였어요..")
    var arr : ArrayList<ReviewInfo> = ArrayList<ReviewInfo>()
    val fav_arr = arrayListOf<String>("김치찌개", "한식")
    lateinit var ID:String

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

        return inflater.inflate(R.layout.fragment_d, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        username.setText(ID)

        find_my_review()
        //add_review_key()
        add_favorite_folder()

        go_user_setting.setOnClickListener {
            val userIntent = Intent(c, UserActivity::class.java)
            userIntent.putExtra("id",ID)
            startActivity(userIntent)
        }

        go_folders.setOnClickListener {
            val nextIntent = Intent(c, FolderActivity::class.java)
            nextIntent.putExtra("id",ID)
            startActivity(nextIntent)
        }
        my_review_button.setOnClickListener {
            val i = Intent(c, MyReviewActivity::class.java)
            i.putExtra("id", ID)
            startActivity(i)
        }
    }

    fun find_my_review(){
        val rdatabase = FirebaseDatabase.getInstance().getReference("Review").child("time")
        rdatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arr.clear()
                for(snap in snapshot.children){

                    val review = snap.getValue(ReviewInfo::class.java)
                    if(review!=null){
                        if(review.ID.equals(ID)){
                            arr.add(review)
                        }
                    }
                }
                add_review_key()
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })

    }
    fun add_review_key(){
        for(review in arr){
            val text = TextView(c)
            text.text = review.content
            text.textSize = 16f
            text.ellipsize = TextUtils.TruncateAt.END
            text.setOnClickListener{
                var i = Intent(c, PostActivity::class.java)
                i.putExtra("id", ID)
                i.putExtra("list", review.link)
                startActivity(i)
            }
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            params.bottomMargin = 20
            text.layoutParams = params
            my_reviews.addView(text)
        }

    }

    fun add_favorite_folder(){
        my_folders.layoutManager = LinearLayoutManager(c,
        LinearLayoutManager.HORIZONTAL, false)
        val rdatabase = FirebaseDatabase.getInstance().getReference("Accounts").child(ID).child("Folder")
        rdatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                folderNames.clear()
                for(snap in snapshot.children){

                    Log.i("폴더이름", snap.key.toString())
                    folderNames.add(snap.key.toString())
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            //adapter.notifyDataSetChanged()


        })



        adapter = MyFolderAdapter(folderNames)
        adapter.itemClickListener = object : MyFolderAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                var intent = Intent(c, InFolderActivity::class.java)
                intent.putExtra("f_name", adapter.folderName[position])
                intent.putExtra("id", ID)
                startActivity(intent)
            }

        }
        my_folders.adapter = adapter
    }

}
