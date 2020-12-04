package com.example.teamkook

import android.content.Context
import android.media.Image
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.row_recommend.*
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class RecommendAdapter (var info : ArrayList<RecommendInfo>, var standard:ArrayList<String>, val context: Context, val inputstream1: InputStream)
    : RecyclerView.Adapter<RecommendAdapter.ViewHolder>(){

    lateinit var foodRecommendation:FoodClassification
    var itemClickListener : OnItemClickListener ?= null
    val storage=FirebaseStorage.getInstance("gs://team-kook.appspot.com")
    val reference=storage.reference
    val commentArray= listOf<String>(
        "만약 오늘 힘든 일이 있으셨다면 제가 추천해주는 맛있는 음식들로 힘내보아요!",
        "오늘도 푸드서치와 함께 맛있는 요리를 해보세요:)",
        "뭐니뭐니해도 건강이 최고! 오늘은 간편조리 음식 대신 직접 요리해보는거 어떠세요?",
        "메뉴 고민은 이제 그만! 당신의 취향을 바탕으로 한 맞춤 메뉴를 추천드려요! 오늘은 이거 어떠세요?"
    )

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
        val weather=Random().nextInt(3)//0-비, 1-눈, 2-아무것도X

        var time:Int
        val currentTime=Calendar.getInstance().time
        var timeFormat=SimpleDateFormat("HH",Locale.KOREA).format(currentTime)
        Log.i("시간",timeFormat)
        if (timeFormat[0].toInt() in 0..11){//0-아침, 1-점심, 3-저녁
            time=0
        }else if(timeFormat[0].toInt() in 12..17){
            time=1
        }else{
            time=2
        }

        val spice=Random().nextInt(2)//0-매운 1-안매운
        var temp=0//0-추운 1-더운 --> 지금은 무조건 0으로 설정
        if(weather==1) temp=0



        when(info[position].index){ //상황 4개
            "0" -> {
                if(weather==0){
                    holder.recommendStr.text = "  지금은 비가 오네요. 이런 음식 어떠세요?"
                }else if(weather==1){
                    holder.recommendStr.text = "  눈과 잘 어울리는 음식을 골라봤어요!"
                }else{
                    holder.recommendStr.text = "  오늘 같은 날에 추천하는 음식"
                }
            }
            "1" -> {
                if(time==0){
                    holder.recommendStr.text = "  아침은 가볍게 이 음식 어떠세요?"
                }else if(time==1){
                    holder.recommendStr.text = "  점심시간이에요~! 저는 이 음식들이 끌리네요!"
                }else{
                    holder.recommendStr.text = "  고생 많았어요, 저녁 든든히 먹고 힘내요!"
                }
            }
            "2" ->{
                if(spice==0){
                    holder.recommendStr.text = "  스트레스 푸는데에는 매운 음식이 직빵!"
                }else{
                    holder.recommendStr.text = "  오늘은 순한 음식을 먹는게 어때요?"
                }
            }
            "3" ->{
                if(temp==0){
                    holder.recommendStr.text = "  추울 때에는 몸을 녹히는게 인지상정!"
                }else{
                    holder.recommendStr.text = "  오늘 유난히 덥네요! 이 음식으로 보양하는 건 어때요?"
                }

            }
        }
        ///이미지 어떻게 가져와~~~~~~~~~~~~~~~~~~~~~~~!!!!!!!!!!!!!!!!!!!!!!!!?????????
        foodRecommendation= FoodClassification(inputstream1,context)
        when(position){
            0 -> {
                when(weather){
                    0->{
                        val str=foodRecommendation.recommendCondition("RAIN",1,standard)
                        holder.firstfood.text = str!!.split(" ").last()
                        val img="img"+foodRecommendation.findIMG(str!!.split(" ").last())+".jpg"
                        reference.child(img).downloadUrl.addOnSuccessListener(object :OnSuccessListener<Uri>{
                            override fun onSuccess(p0: Uri?) {
                                Glide.with(context).load(p0).into(holder.image1)
                            }
                        }
                        )
                        holder.explanation.text ="비가 오는 날엔 뭐니뭐니 해도 "+str+"~~"
                    }
                    1->{
                        val str= foodRecommendation.recommendCondition("SNOW",1,standard)
                        holder.firstfood.text =str!!.split(" ").last()
                        val img="img"+foodRecommendation.findIMG(str!!.split(" ").last())+".jpg"
                        reference.child(img).downloadUrl.addOnSuccessListener(object :OnSuccessListener<Uri>{
                            override fun onSuccess(p0: Uri?) {
                                Glide.with(context).load(p0).into(holder.image1)
                            }
                        }
                        )
                        holder.explanation.text ="창 밖에 눈을 쳐다보면서 먹기에는 "+str+"이 제격!"
                    }
                    2->{
                        val str=foodRecommendation.recommendCondition("VEGAN",1,standard)
                        holder.firstfood.text =str!!.split(" ").last()
                        val img="img"+foodRecommendation.findIMG(str!!.split(" ").last())+".jpg"
                        reference.child(img).downloadUrl.addOnSuccessListener(object :OnSuccessListener<Uri>{
                            override fun onSuccess(p0: Uri?) {
                                Glide.with(context).load(p0).into(holder.image1)
                            }
                        }
                        )
                        holder.explanation.text ="오늘 날씨와 잘 어울리는 "+str+"를 드셔보세요!"
                    }
                }
            }
            1 -> {
                when(time){
                    0->{
                        val str=foodRecommendation.recommendCondition("BREAKFAST",1,standard)
                        holder.firstfood.text = str!!.split(" ").last()
                        val img="img"+foodRecommendation.findIMG(str!!.split(" ").last())+".jpg"
                        reference.child(img).downloadUrl.addOnSuccessListener(object :OnSuccessListener<Uri>{
                            override fun onSuccess(p0: Uri?) {
                                Glide.with(context).load(p0).into(holder.image1)
                            }
                        }
                        )
                        holder.explanation.text ="아침부터 "+str+"은 최고의 선택:)"
                    }
                    1->{
                        val str=foodRecommendation.recommendCondition("LUNCH",1,standard)
                        holder.firstfood.text = str!!.split(" ").last()
                        val img="img"+foodRecommendation.findIMG(str!!.split(" ").last())+".jpg"
                        reference.child(img).downloadUrl.addOnSuccessListener(object :OnSuccessListener<Uri>{
                            override fun onSuccess(p0: Uri?) {
                                Glide.with(context).load(p0).into(holder.image1)
                            }
                        }
                        )
                        holder.explanation.text = "점심에는 역시 든든한 "+str
                    }
                    2->{
                        val str=foodRecommendation.recommendCondition("DINNER",1,standard)
                        holder.firstfood.text = str!!.split(" ").last()
                        val img="img"+foodRecommendation.findIMG(str!!.split(" ").last())+".jpg"
                        reference.child(img).downloadUrl.addOnSuccessListener(object :OnSuccessListener<Uri>{
                            override fun onSuccess(p0: Uri?) {
                                Glide.with(context).load(p0).into(holder.image1)
                            }
                        }
                        )
                        holder.explanation.text =str+" 묻고 더블로 가!"
                    }
                }
            }
            2 -> {
                when(spice){
                    0->{
                        val str=foodRecommendation.recommendCondition("SPICY",1,standard)
                        holder.firstfood.text = str!!.split(" ").last()
                        val img="img"+foodRecommendation.findIMG(str!!.split(" ").last())+".jpg"
                        reference.child(img).downloadUrl.addOnSuccessListener(object :OnSuccessListener<Uri>{
                            override fun onSuccess(p0: Uri?) {
                                Glide.with(context).load(p0).into(holder.image1)
                            }
                        }
                        )
                        holder.explanation.text ="매운 음식을 좋아하는군요! 오늘은 "+ str +"에 도전해 보는거 어떠세요?"
                    }
                    1->{
                        val str=foodRecommendation.recommendCondition("SPICY",0,standard)
                        holder.firstfood.text = str!!.split(" ").last()
                        val img="img"+foodRecommendation.findIMG(str!!.split(" ").last())+".jpg"
                        reference.child(img).downloadUrl.addOnSuccessListener(object :OnSuccessListener<Uri>{
                            override fun onSuccess(p0: Uri?) {
                                Glide.with(context).load(p0).into(holder.image1)
                            }
                        }
                        )
                        if(standard.contains("VEGAN")){
                            holder.explanation.text="비건인 당신을 위해 "+str+"을 추천해요:)"
                        }else{
                            holder.explanation.text ="오늘은 맵지 않고 "+str+" 어떠세요?"
                        }
                    }
                }
            }
            3 -> {
                when(temp){
                    0->{
                        val str=foodRecommendation.recommendCondition("HOT",1,standard)
                        holder.firstfood.text = str!!.split(" ").last()
                        val img="img"+foodRecommendation.findIMG(str!!.split(" ").last())+".jpg"
                        reference.child(img).downloadUrl.addOnSuccessListener(object :OnSuccessListener<Uri>{
                            override fun onSuccess(p0: Uri?) {
                                Glide.with(context).load(p0).into(holder.image1)
                            }
                        }
                        )
                        holder.explanation.text ="몸녹히기엔 따끈따끈하고 "+str+" 왕강추"
                    }
                    1->{
                        val str=foodRecommendation.recommendCondition("COLD",1,standard)
                        holder.firstfood.text = str!!.split(" ").last()
                        val img="img"+foodRecommendation.findIMG(str!!.split(" ").last())+".jpg"
                        reference.child(img).downloadUrl.addOnSuccessListener(object :OnSuccessListener<Uri>{
                            override fun onSuccess(p0: Uri?) {
                                Glide.with(context).load(p0).into(holder.image1)
                            }
                        }
                        )
                        holder.explanation.text =commentArray[Random().nextInt(4)]
                    }
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return info.size
    }

}
