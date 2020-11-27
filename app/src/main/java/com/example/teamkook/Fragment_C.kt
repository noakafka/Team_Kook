package com.example.teamkook


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_c.*

/**
 * A simple [Fragment] subclass.
 */
class Fragment_C() : Fragment() {

    lateinit var layoutManager: LinearLayoutManager
    lateinit var reviewAdapter : ReviewAdapter
    lateinit var rdb : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v : View = inflater.inflate(R.layout.fragment_c, container, false)


        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    fun init(){
        layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        review_recyclerview.layoutManager = layoutManager

        rdb = FirebaseDatabase.getInstance().getReference("Review")

//        val info = ReviewInfo("ldh1", "https://www.youtube.com/watch?v=qWbHSOplcvY&feature=youtu.be", "이대로 끓이니까 너무 맛있었어요!!~", 4.0.toFloat(), 2.0.toFloat())
//        rdb.push().setValue(info)
        val query = FirebaseDatabase.getInstance().getReference("Review")

        
        val option = FirebaseRecyclerOptions.Builder<ReviewInfo>()
            .setQuery(query, ReviewInfo::class.java)
            .build()
        reviewAdapter = ReviewAdapter(option)
        reviewAdapter.itemClickListener = object : ReviewAdapter.OnItemClickListener{
            override fun onReviewItemClick(view: View, position: Int) {
                val id = this@Fragment_C.id
                val link = reviewAdapter.getItem(position).link
                var i = Intent(activity, PostActivity::class.java)
                i.putExtra("id", id)
                i.putExtra("link", link)
                Log.i("link", link)
                Log.i("클릭", id.toString())
                startActivity(i)
            }


        }
        review_recyclerview.adapter = reviewAdapter
    }

    override fun onStart() {
        super.onStart()
        reviewAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        reviewAdapter.stopListening()
    }
}
