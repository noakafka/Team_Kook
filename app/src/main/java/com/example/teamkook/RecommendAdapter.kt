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
        var image1 : ImageView
        var explanation : TextView

        init{

            recommendStr = itemView.findViewById(R.id.recommend_id)
            firstfood = itemView.findViewById(R.id.first_food)
//            layout1.setOnClickListener {
//                itemClickListener?.onItemClick(itemView, adapterPosition, 1)
//            }
            image1 = itemView.findViewById(R.id.first_recommendation)
            explanation = itemView.findViewById(R.id.first_food_explanation)
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
        //holder.firstfood.text = info[position].food1
        when(position){
            0 -> {
                holder.image1.setImageResource(R.drawable.img2)
                holder.firstfood.text = "쫀득바삭 김치전"
                holder.explanation.text = "지난번 비오는 날에 김치전 레시피를 찾아보셨네요~! 오늘도 비가 오는데 김치전 어떠세요?"
            }
            1 -> {
                holder.image1.setImageResource(R.drawable.img5)
                holder.firstfood.text = "고소한 참치김밥"
                holder.explanation.text = "오늘은 날씨가 좋으니 소풍가는 기분으로 참치김밥 어떠세요?"
            }
            2 -> {
                holder.image1.setImageResource(R.drawable.img8)
                holder.firstfood.text = "닭가슴살 샐러드"
                holder.explanation.text = "다이어트 중이라면 오늘 점심은 닭가슴살 샐러드 어떠세요?"
            }
        }


    }

    override fun getItemCount(): Int {
        return info.size
    }

}
