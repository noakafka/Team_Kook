package com.example.teamkook

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class MyReviewAdapter (var reviews : ArrayList<ReviewInfo>)
    : RecyclerView.Adapter<MyReviewAdapter.MyViewHolder>(){


    var itemClickListener:OnItemClickListener ?= null
    inner class MyViewHolder(itemView : View)
        :RecyclerView.ViewHolder(itemView){
        var thumbnail : ImageView
        var title : TextView
        var content : TextView
        init{
            thumbnail = itemView.findViewById(R.id.my_review_thumbnail)
            title = itemView.findViewById(R.id.my_review_title)
            content = itemView.findViewById(R.id.my_review_content)
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(itemView, adapterPosition)
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClick(view : View, position:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_my_reviews, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var id1 : String = reviews[position].link.substring(reviews[position].link.lastIndexOf("=")+1)
        var id2 :String= reviews[position].link.substring(reviews[position].link.lastIndexOf("/")+1)
        var id = id1;
        if(id2.length < id1.length)
            id = id2;
        Log.i("파싱한 아이디값", id);
        var url1 : String = "https://img.youtube.com/vi/"+id+"/0.jpg"
        Glide.with(holder.thumbnail.context).load(url1).into(holder.thumbnail)


        holder.title.text = reviews[position].title
        holder.content.text = reviews[position].content

    }

    override fun getItemCount(): Int {
        return reviews.size
    }


}