package com.example.teamkook

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAddAdapter (var folders : ArrayList<String>)
    : RecyclerView.Adapter<PostAddAdapter.MyViewHolder>(){
    var itemClickListener : OnItemClickListener ?=null
    var selected: Int = -1
    inner class MyViewHolder (itemView : View)
        :RecyclerView.ViewHolder(itemView){
        var folderName : TextView
        var folderImage : ImageView
        init{
            folderName = itemView.findViewById(R.id.post_folder_name)
            folderImage = itemView.findViewById(R.id.image_folder)
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
        if(position == selected){
//            holder.folderImage.imageTintList = ColorStateList.valueOf(Color.parseColor("FFFAFAFA"))
//            holder.folderName.setTextColor(ColorStateList.valueOf(Color.parseColor("FFFAFAFA")))
            //holder.folderImage.setColorFilter(Color.parseColor("#FFFAFAFA"))
            holder.folderName.setTextColor(Color.parseColor("#FFFAFAFA"))
        }
        else{
//            holder.folderImage.imageTintList = ColorStateList.valueOf(Color.parseColor("E48C34"))
//            holder.folderName.setTextColor(ColorStateList.valueOf(Color.parseColor("E48C34")))
            //holder.folderImage.setColorFilter(Color.parseColor("#E48C34"))
            holder.folderName.setTextColor(Color.parseColor("#E48C34"))
        }
        //#E48C34
    }

    override fun getItemCount(): Int {
        return folders.size
    }


}
