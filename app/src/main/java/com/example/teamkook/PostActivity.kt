package com.example.teamkook

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.activity_user.*
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.jsoup.select.Elements
import java.lang.ref.WeakReference
import java.net.URL

class PostActivity : YouTubeBaseActivity() {

    lateinit var youtubeViewer : YouTubePlayerView
    lateinit var link : String //유튜브 링크
    lateinit var linkID : String
    lateinit var ID :String
    val url1 = "https://www.googleapis.com/youtube/v3/videos?id="
    val url2 = "&key=AIzaSyAONAWO0Dta_zwAnMMBmNqkwBjCgSNGVSU&part=snippet"
    val APIKEY = "AIzaSyAONAWO0Dta_zwAnMMBmNqkwBjCgSNGVSU"
    
    //현재 동영상 정보
    var title = ""
    var description = ""

    lateinit var postAdapter: PostAdapter
    lateinit var rdb :DatabaseReference
    lateinit var layoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        if(intent.hasExtra("id")){
            //ID = intent.getStringExtra("id")
            ID = "ldh1"
        }
        if(intent.hasExtra("link")){
            link = intent.getStringExtra("link")
        }

        youtubeViewer = findViewById<YouTubePlayerView>(R.id.youtubeViewer)

        //유튜브 링크에서 id만 추출 (v이후 값)
        linkID = "qWbHSOplcvY"
        init()
    }

    fun init(){
        youtubeViewer.initialize("temp", object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                wasRestored: Boolean
            ) {
                if(!wasRestored){
                    player?.cueVideo(linkID)
                }
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {

            }
        })


        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        post_recyclerview.layoutManager = layoutManager

        rdb = FirebaseDatabase.getInstance().getReference("Review").child("link")
        val query = FirebaseDatabase.getInstance().reference.child("Review").child("link")
            .limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<ReviewInfo>()
            .setQuery(query, ReviewInfo::class.java)
            .build()
        postAdapter = PostAdapter(option)
        post_recyclerview.adapter = postAdapter


        //startTask()

        //리뷰 기능
        add_post.setOnClickListener {
            if(post_content.text.toString() == null || new_score.rating == null ||
                    new_spicy.rating == null){//리뷰 안 적은 경우

            }
            else{
                val newPost = ReviewInfo(ID, link, post_content.text.toString(), new_score.rating, new_spicy.rating)
                var rdatabase = FirebaseDatabase.getInstance().getReference("Review").child("link")

                //review - link - 링크주소 - 리뷰 정보 순으로 데이터베이스
                //시간 순으로 추가되게끔 데베 추가
                rdatabase.setValue(newPost)

                //리뷰 입력창 초기화
                post_content.text = null
                new_score.rating = 0.0f
                new_spicy.rating = 0.0f


            }
        }
    }


    fun startTask(){
        val task = MyAsyncTask(this)
        task.execute()
    }

    fun dataChanged(){
        youtube_title.text = title
        youtube_info.text = description
    }
    class MyAsyncTask(context:PostActivity) : AsyncTask<Unit, Unit, Unit>(){
        val activityReference = WeakReference(context)
        override fun doInBackground(vararg params: Unit?) {
            val activity = activityReference.get()
            var argUrl = activity?.url1 + activity?.linkID + activity?.url2
            val infoURL = URL(argUrl)
            val doc = Jsoup.connect(infoURL.toString()).parser(Parser.xmlParser()).get()
            var search : Elements
            search = doc.select("items")
            if(search.size<=0){
                Log.i("youtubeInfo", "실패")
            }
            else{
                for (info in search){
                    val title = info.select("title")
                    val description = info.select("description")
                    activity?.title = title.toString()
                    activity?.description = description.toString()

                }
            }
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            val activity = activityReference.get()
            activity?.dataChanged()
        }

    }

}