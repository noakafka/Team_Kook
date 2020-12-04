package com.example.teamkook


import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_a.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class Fragment_A : Fragment() {

    lateinit var layoutManager : LinearLayoutManager
    lateinit var recommendAdapter: RecommendAdapter
    lateinit var ID:String
    val mDatabase= FirebaseDatabase.getInstance()
    lateinit var foodRecommendation:FoodClassification
    val commentArray= listOf<String>(
        "만약 오늘 힘든 일이 있으셨다면 제가 추천해주는 맛있는 음식들로 힘내보아요!",
        "오늘도 푸드서치와 함께 맛있는 요리를 해보세요:)",
        "뭐니뭐니해도 건강이 최고! 오늘은 간편조리 음식 대신 직접 요리해보는거 어떠세요?",
        "메뉴 고민은 이제 그만! 당신의 취향을 바탕으로 한 맞춤 메뉴를 추천드려요! 오늘은 이거 어떠세요?"
    )

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

        //사용자 알러지 읽어들여서 arraylist 생성 후 전달
        var array= arrayListOf<String>()
        val database=mDatabase.getReference("Accounts").child(ID)
        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val child=snapshot.getValue(Account::class.java)
                if (child != null) {
                    if(!child.vegan){
                        array.add("VEGAN")
                    }
                    val i=child.allergy?.iterator()
                    if (i != null) {
                        while(i.hasNext()){
                            when(i.next()){
                                "땅콩"->{ array.add("PEANUT") }
                                "복숭아"->{ array.add("PEACH") }
                                "우유"->{ array.add("MILK") }
                                "달걀"->{ array.add("EGG") }
                                "갑각류"->{ array.add("CRUSTACEAN") }
                                "밀가루"->{ array.add("FLOUR") }
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("database error",error.message)
            }
        })

        layoutManager = LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        home_recyclerview.layoutManager = layoutManager
        recommendAdapter = RecommendAdapter(ArrayList<RecommendInfo>(),array,activity!!,resources.openRawResource(R.raw.classification1))
        recommendAdapter.itemClickListener = object : RecommendAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int, foodPosition: Int) {
                when(foodPosition){
                    1 -> {

                    }
                    2 -> {

                    }
                    3 -> {

                    }
                    4->{

                    }
                }
            }
        }

        val hand=Handler()
        hand.postDelayed({
            foodRecommendation= FoodClassification(resources.openRawResource(R.raw.classification1),activity!!)
            var line=foodRecommendation.recommendRandom(array) ///////////문자열 반환 받은거 split해서 text 넣으면 될 듯
            if(line!=null){
                var keyword=line.split(" ")//array1이 음식, array2가 음식 키워드
                personal_food.text=line
                explanation.text=commentArray[Random().nextInt(4)] //comment 만들고 붙이기

                //////////////////////이미지는...............?
                personal_recommendation.setImageResource(R.drawable.img1)
            }
        },2500)


        //recommendinfo 구조 바꾸기 --> ......? 지워도 되나..?
        recommendAdapter.info.add(RecommendInfo("0", "샐러드", "미역국", "순대"))
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
