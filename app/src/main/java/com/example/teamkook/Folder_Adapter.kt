package com.example.teamkook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Folder_Adapter(val context: Context,
                     var items:ArrayList<Folder>): RecyclerView.Adapter<Folder_Adapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.item_folder, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]

        //set friend's profile image
        //holder.f_image.setImageResource()
        holder.f_name.text = data.folder_name

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var f_name: TextView

        init {
            f_name = itemView.findViewById(R.id.rview_f_name)


            /*itemView.setOnClickListener {
                lastSelectedPosition = adapterPosition
                notifyDataSetChanged()
            }*/
        }
    }





}