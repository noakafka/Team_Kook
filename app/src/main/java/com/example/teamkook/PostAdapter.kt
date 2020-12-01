package com.example.teamkook

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class PostAdapter (var post : ArrayList<Review_linkInfo>)
    :RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        var comment : TextView
        var commentscore : RatingBar
        var commentspicy : RatingBar
        init{
            comment = itemView.findViewById(R.id.post_comment)
            commentscore = itemView.findViewById(R.id.post_score)
            commentspicy = itemView.findViewById(R.id.post_spicy)

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_post, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.comment.text = post[position].content
        holder.commentscore.rating = post[position].rating
        holder.commentspicy.rating = post[position].spicy
    }

    override fun getItemCount(): Int {
        return post.size
    }

}