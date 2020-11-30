package com.example.teamkook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAddAdapter (var folders : ArrayList<String>)
    : RecyclerView.Adapter<PostAddAdapter.MyViewHolder>(){
    var itemClickListener : OnItemClickListener ?=null
    inner class MyViewHolder (itemView : View)
        :RecyclerView.ViewHolder(itemView){
        var folderName : TextView
        init{
            folderName = itemView.findViewById(R.id.post_folder_name)
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(itemView, adapterPosition)
            }
        }
    }

    interface  OnItemClickListener{
        fun onItemClick(view : View, position : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_add_folder, parent , false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.folderName.text = folders[position]
    }

    override fun getItemCount(): Int {
        return folders.size
    }


}
