package com.example.teamkook


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_a.*
import kotlinx.android.synthetic.main.row_recommend.*

/**
 * A simple [Fragment] subclass.
 */
class Fragment_A : Fragment() {

    lateinit var layoutManager : LinearLayoutManager
    lateinit var recommendAdapter: RecommendAdapter
    lateinit var ID:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(activity!=null){
            val intent=activity!!.intent
            if(intent!=null){
                ID=intent.getStringExtra("id")
            }
        }
        var v : View = inflater.inflate(R.layout.fragment_a, container, false)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    fun init(){
        layoutManager = LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        home_recyclerview.layoutManager = layoutManager
        recommendAdapter = RecommendAdapter(ArrayList<RecommendInfo>())
        recommendAdapter.itemClickListener = object : RecommendAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int, foodPosition: Int) {
                when(foodPosition){
                    1 -> {

                    }
                    2 -> {

                    }
                    3 -> {

                    }
                }
            }
        }
        ///////////// test
        personal_food.text = "얼큰한 김치찌개"
        explanation.text = "김치찌개 레시피 영상을 찾아본 지도 일주일이 넘었네요~! 오늘은 오랜만에 김치찌개 어떠세요?"
        personal_recommendation.setImageResource(R.drawable.img1)
        //recommendinfo 구조 바꾸기
        recommendAdapter.info.add(RecommendInfo("1", "김치전", "수제비", "떡볶이"))
        recommendAdapter.info.add(RecommendInfo("2", "참치김밥", "라면", "카레"))
        recommendAdapter.info.add(RecommendInfo("3", "샐러드", "미역국", "순대"))

        personal_info.text = "  "+ID+"님을 위한 메뉴추천"

        home_recyclerview.adapter = recommendAdapter

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()

    }

}
