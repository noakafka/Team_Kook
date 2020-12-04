package com.example.teamkook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.youtube.player.internal.c
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_my_review.*

class MyReviewActivity : AppCompatActivity() {

    lateinit var adapter : MyReviewAdapter
    var userID : String = ""
    var MyReviews : ArrayList<ReviewInfo> = ArrayList<ReviewInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_review)
        if(intent.hasExtra("id")){
            userID = intent.getStringExtra("id")
        }
        init()
    }

    fun init(){
        Myreview_back_btn.setOnClickListener {
            finish()
        }

        my_review_recyclerview.layoutManager = LinearLayoutManager(this,
        LinearLayoutManager.VERTICAL, false)
        val rdatabase = FirebaseDatabase.getInstance().getReference("Review").child("time")
        rdatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){

                    val review = snap.getValue(ReviewInfo::class.java)
                    if(review!=null){
                        if(review.ID.equals(userID)){
                            MyReviews.add(review)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })

        adapter = MyReviewAdapter(MyReviews)
        adapter.itemClickListener = object :MyReviewAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                var i = Intent(this@MyReviewActivity, PostActivity::class.java)
                i.putExtra("id", userID)
                i.putExtra("link", adapter.reviews[position].link)
                startActivity(i)
                finish()
            }

        }

        my_review_recyclerview.adapter = adapter



    }
}