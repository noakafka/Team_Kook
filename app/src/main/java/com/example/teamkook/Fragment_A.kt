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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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
        explanation.text = """김치찌개
            |한국인의 소울푸드 김치찌개!
            |김치찌개를 먹은지도 일주일이 넘었네요~
            |오늘은 김치찌개 어떠세요??
        """.trimMargin()
        personal_recommendation.setImageResource(R.drawable.img1)
        recommendAdapter.info.add(RecommendInfo("1", "김치전", "수제비", "떡볶이"))
        recommendAdapter.info.add(RecommendInfo("2", "참치김밥", "라면", "카레"))
        recommendAdapter.info.add(RecommendInfo("3", "샐러드", "미역국", "순대"))

        //아이디 받아와서 추가하는 부분 수정해야해ㅐㅐㅐㅐㅐㅐㅐ
        personal_info.text = "ldh1님을 위한 메뉴추천"

        home_recyclerview.adapter = recommendAdapter

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()

    }

}
