package com.example.teamkook

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Research_Adapter(val context: Context, var items:ArrayList<result_items>) : RecyclerView.Adapter<Research_Adapter.ViewHolder>() {

    var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //썸네일
        var id1: String? = items[position].id.videoId
        var url1 : String = "https://img.youtube.com/vi/"+id1+"/default.jpg"
        Glide.with(holder.image.context).load(url1).into(holder.image)

        //타이틀 파싱

        holder.title.text = items[position].snippet.title
        holder.content.text = items[position].snippet.channelTitle

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView
        var title: TextView
        var content: TextView

        init {
            image = itemView.findViewById(R.id.result_image)
            title = itemView.findViewById(R.id.result_title)
            content = itemView.findViewById(R.id.result_content)
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(itemView, adapterPosition)
            }
        }
    }
}
