package com.example.teamkook

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class ReviewAdapter (options : FirebaseRecyclerOptions<ReviewInfo>)
    : FirebaseRecyclerAdapter<ReviewInfo, ReviewAdapter.ViewHolder>(options) {


    inner class ViewHolder(itemView : View)
        : RecyclerView.ViewHolder(itemView){
        var reviewImage : ImageView
        var reviewYoutube : TextView
        var reviewContent : TextView
        var reviewScore : RatingBar
        var reviewSpicy : RatingBar
        init{
            reviewImage = itemView.findViewById(R.id.review_image)
            reviewYoutube = itemView.findViewById(R.id.review_youtube)
            reviewContent = itemView.findViewById(R.id.review_content)
            reviewScore = itemView.findViewById(R.id.rating_score)
            reviewSpicy = itemView.findViewById(R.id.rating_spicy)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_review, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ReviewInfo) {
        //유튜브 썸네일 이미지 가져오기
        var id1 : String = model.link.substring(model.link.lastIndexOf("=")+1)
        var id2 :String= model.link.substring(model.link.lastIndexOf("/")+1)
        var id = id1;
        if(id2.length < id1.length)
            id = id2;
        Log.i("파싱한 아이디값", id);
        var url1 : String = "https://img.youtube.com/vi/"+id+"/0.jpg"
        Glide.with(holder.reviewImage.context).load(url1).into(holder.reviewImage)
        holder.reviewYoutube.text = model.link
        holder.reviewContent.text = model.content
        holder.reviewScore.rating = model.rating
        holder.reviewSpicy.rating = model.spicy
    }
}

