package com.example.teamkook

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.android.synthetic.main.row_recommend.*

class RecommendAdapter (var info : ArrayList<RecommendInfo>)
    : RecyclerView.Adapter<RecommendAdapter.ViewHolder>(){

    var itemClickListener : OnItemClickListener ?= null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var recommendStr : TextView
        var firstfood : TextView
        var secondfood : TextView
        var thirdfood : TextView
        var layout1 : LinearLayout
        var layout2 : LinearLayout
        var layout3 : LinearLayout
        var image1 : ImageView
        var image2 : ImageView
        var image3 : ImageView

        init{
            recommendStr = itemView.findViewById(R.id.recommend_id)
            firstfood = itemView.findViewById(R.id.first_food)
            secondfood = itemView.findViewById(R.id.second_food)
            thirdfood = itemView.findViewById(R.id.third_food)
            layout1 = itemView.findViewById(R.id.first_layout)
            layout2 = itemView.findViewById(R.id.second_layout)
            layout3 = itemView.findViewById(R.id.third_layout)
            layout1.setOnClickListener {
                itemClickListener?.onItemClick(itemView, adapterPosition, 1)
            }
            layout2.setOnClickListener {
                itemClickListener?.onItemClick(itemView, adapterPosition, 2)
            }
            layout3.setOnClickListener {
                itemClickListener?.onItemClick(itemView, adapterPosition, 3)
            }
            image1 = itemView.findViewById(R.id.first_recommendation)
            image2 = itemView.findViewById(R.id.second_recommendation)
            image3 = itemView.findViewById(R.id.third_recommendation)
        }

    }

    interface OnItemClickListener{
        fun onItemClick(view : View, position : Int, foodPosition : Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_recommend, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: RecommendAdapter.ViewHolder, position: Int) {

        when(info[position].index){
            "1" -> holder.recommendStr.text = "지금은 비가 오네요. 이런 음식 어떠세요?"
            "2" -> holder.recommendStr.text = "점심시간이에요~! 저는 이 음식들이 끌리네요!"
            "3" -> holder.recommendStr.text = "다이어트를 하고 있나요? 단백질 위주의 식단을 준비해봤어요!"
        }
        holder.firstfood.text = info[position].food1
        holder.secondfood.text = info[position].food2
        holder.thirdfood.text = info[position].food3
        when(position){
            0 -> {
                holder.image1.setImageResource(R.drawable.img2)
                holder.image2.setImageResource(R.drawable.img3)
                holder.image3.setImageResource(R.drawable.img4)
            }
            1 -> {
                holder.image1.setImageResource(R.drawable.img5)
                holder.image2.setImageResource(R.drawable.img6)
                holder.image3.setImageResource(R.drawable.img7)
            }
            2 -> {
                holder.image1.setImageResource(R.drawable.img8)
                holder.image2.setImageResource(R.drawable.img9)
                holder.image3.setImageResource(R.drawable.img10)
            }
        }


    }

    override fun getItemCount(): Int {
        return info.size
    }

}
