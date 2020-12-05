package com.example.teamkook

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyFolderAdapter(var folderName : ArrayList<String>)
    : RecyclerView.Adapter<MyFolderAdapter.MyViewHolder>(){
    var itemClickListener : OnItemClickListener ?= null
    inner class MyViewHolder(itemView : View)
        :RecyclerView.ViewHolder(itemView){
        var image : ImageView
        var name : TextView
        init{
            image = itemView.findViewById(R.id.my_folder_image)
            name = itemView.findViewById(R.id.my_folder_name)
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(itemView, adapterPosition)
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClick(view : View, position : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_my_folders, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.image.setImageResource(R.mipmap.favorite_folder)
        holder.name.text = folderName[position]
    }

    override fun getItemCount(): Int {
        return folderName.size
    }


}