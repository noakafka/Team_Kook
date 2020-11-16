package com.example.teamkook


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_b.*
import kotlinx.android.synthetic.main.fragment_d.*

/**
 * A simple [Fragment] subclass.
 */
class Fragment_D(var c : Context) : Fragment() {

    val arr = arrayListOf<String>("이대로 끓이니까 너무 맛있었어요~!!!", "제 기준 굴소스를 추가하니까 더 맛있더라구요", "에어프라이기로 하면 더 쉽습니당~!~!","ㅜㅜ 제 입맛에는 아닌가 봐요.. 별로였어요..")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_d, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        add_review_key()
    }

    fun add_review_key(){
        for(str in arr){
            val text = TextView(c)
            text.text = str
            text.textSize = 16f
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            params.bottomMargin = 20
            text.layoutParams = params
            my_reviews.addView(text)
        }
    }

}
