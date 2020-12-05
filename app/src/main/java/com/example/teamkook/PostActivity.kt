package com.example.teamkook

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.google.android.youtube.player.internal.i
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.activity_user.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.jsoup.select.Elements
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.URL
import javax.xml.transform.Source

class PostActivity : YouTubeBaseActivity(){

    lateinit var youtubeViewer : YouTubePlayerView
    lateinit var link : String //유튜브 링크
    lateinit var linkID : String
    lateinit var ID :String
    val url1 = "https://www.googleapis.com/youtube/v3/videos?id="
    val url2 = "&key=AIzaSyCLAfLcEQvBA5zrat3nReaT28iI-E3QH5c&part=snippet"
    val APIKEY = "AIzaSyAONAWO0Dta_zwAnMMBmNqkwBjCgSNGVSU"
    
    //현재 동영상 정보
    var title = ""
    var description = ""

    var addFolder : Boolean = false
    lateinit var postAdapter: PostAdapter
    lateinit var rdb :DatabaseReference
    lateinit var layoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        if(intent.hasExtra("id")){
            ID = intent.getStringExtra("id")
            //ID = "ldh1"
        }
        if(intent.hasExtra("link")){
            link = intent.getStringExtra("link")
        }

        youtubeViewer = findViewById<YouTubePlayerView>(R.id.youtubeViewer)

        //유튜브 링크에서 id만 추출 (v이후 값)

        var id1 : String = link.substring(link.lastIndexOf("=")+1)
        var id2 :String= link.substring(link.lastIndexOf("/")+1)
        var id = id1;
        if(id2.length < id1.length)
            id = id2;
        linkID = id
        Log.i("아이디값", linkID)
        //linkID = "qWbHSOplcvY"
        init()
    }

    fun init(){
        var textView : TextView = findViewById(R.id.youtube_info)
        textView.movementMethod = ScrollingMovementMethod()

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        post_recyclerview.layoutManager = layoutManager
        rdb = FirebaseDatabase.getInstance().getReference("Review").child("link")


        var array_post : ArrayList<Review_linkInfo> = ArrayList<Review_linkInfo>()


        val rdatabase = rdb
        rdatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    Log.i("순서", "1")
                    if(snap.key.equals(linkID)){
                        Log.i("순서", "2")
                        for(review in snap.children){
                            Log.i("순서", "3")

                            array_post.add(Review_linkInfo(review.child("id").getValue().toString(),
                            review.child("content").getValue().toString(), review.child("rating").getValue().toString().toFloat()
                            , review.child("spicy").getValue().toString().toFloat()))
//                            val post = review.getValue(Review_linkInfo::class.java)
//                            if(post!=null){
//                            }
                            Log.i("post개수", array_post.size.toString())
                        }
                    }


                }
                postAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })



        postAdapter = PostAdapter(array_post)

        post_recyclerview.adapter = postAdapter


        initYoutubeInfo()

        //리뷰 기능
        add_post.setOnClickListener {
            if(post_content.text.toString() == ""){//리뷰 안 적은 경우
                Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                val newPost = Review_linkInfo(ID, post_content.text.toString(), new_score.rating, new_spicy.rating)
                var rdatabase = FirebaseDatabase.getInstance().getReference("Review")

                //유튜브 링크별 리뷰 추가
                rdatabase.child("link").child(linkID).push().setValue(newPost)

                //review - link - 링크주소 - 리뷰 정보 순으로 데이터베이스
                //시간 순으로 추가되게끔 데베 추가
                val newPost2 = ReviewInfo(ID, link, post_content.text.toString(), new_score.rating, new_spicy.rating, title)
                rdatabase.child("time").push().setValue(newPost2)

                //리뷰 입력창 초기화
                post_content.text = null
                new_score.rating = 0.0f
                new_spicy.rating = 0.0f


                //recyclerview 갱신
                postAdapter.notifyDataSetChanged()
            }
        }

        //상단에 화살표 클릭했을 때
        post_back.setOnClickListener {
//            FirebaseDatabase.getInstance().getReference("Accounts").child(ID).child("Folder").child("한식").push().setValue(
//                Folder("한식", "https://www.youtube.com/watch?v=qWbHSOplcvY")
//            )
//            FirebaseDatabase.getInstance().getReference("Accounts").child(ID).child("Folder").child("한식").push().setValue(
//                Folder("한식", "https://www.youtube.com/watch?v=t4Es8mwdYlE")
//            )
//            FirebaseDatabase.getInstance().getReference("Accounts").child(ID).child("Folder").child("맛집").push().setValue(
//                Folder("맛집", "https://www.youtube.com/watch?v=qWbHSOplcvY")
//            )
//            FirebaseDatabase.getInstance().getReference("Accounts").child(ID).child("Folder").child("맛집").push().setValue(
//                Folder("맛집", "https://www.youtube.com/watch?v=t4Es8mwdYlE")
//            )
            finish()
        }


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



        //폴더에 추가할 때
        add_folder.setOnClickListener {

            addFolder = true
            if(addFolder){
                //폴더에 이미 추가되어 있는지 확인
                val rdatabase = FirebaseDatabase.getInstance().getReference("Accounts").child(ID).child("Folder")
                rdatabase.addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(addFolder){
                            var fname : String = ""
                            var check : Boolean = false
                            for(foldername in snapshot.children){
                                for(folder in foldername.children){
                                    val list=folder.getValue(Folder::class.java)
                                    if (list != null) {
                                        if(link.equals(list.link)){//이미 추가되어 있는 경우
                                            check = true
                                            fname = list.folder_name!!

                                        }
                                    }
                                }
                            }

                            addFolder = false
                            callDialog(check, fname)
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        //
                    }
                })
            }


        }
    }


    fun callDialog(check : Boolean, fileName:String){
        val rdatabase = FirebaseDatabase.getInstance().getReference("Accounts").child(ID).child("Folder")

        Log.i("다이얼로그 함수", check.toString())
        if(!check){
            val dlg = ChooseFolderDialog(this)
            dlg.init(ID, link, title)
        }
        else{
            //var deleted : Boolean = false
            var deletequery = rdatabase.child(fileName).orderByChild("link").equalTo(link)
            deletequery.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){//링크가 존재하는 경우
                        for(snap in snapshot.children){
                            snap.ref.removeValue()
                            //deleted = true

                        }
                        Toast.makeText(this@PostActivity, "폴더에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    //TODO("Not yet implemented")
                }

            })
        }
    }
    fun initYoutubeInfo(){
        var youtubeURL = url1+linkID+url2

        val client = OkHttpClient()
        val request = Request.Builder().url(youtubeURL).build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("유튜브 파싱", "fail")
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonData = response.body()?.string()
                if(jsonData!=null){
                    val jsonobj : JSONObject = JSONObject(jsonData)
                    var json_arr : JSONArray = jsonobj.getJSONArray("items")
                    var items : JSONObject = json_arr.getJSONObject(0)
                    var snippet : JSONObject = items.getJSONObject("snippet")
                    runOnUiThread {
                        youtube_title.text = snippet.getString("title")
                        youtube_info.text = "더보기) ".plus(snippet.getString("description"))
                    }

                    title = snippet.getString("title")
                    Log.i("title11", snippet.getString("title"))
                    Log.i("descrip", snippet.getString("description"))
                    
                }
            }

        })



//        val task = MyAsyncTask(youtube, object : MyAsyncTask.AsyncResponse{
//            override fun readyToSetTitle(title: String, description: String) {
//                youtube_title.text = title
//                youtube_info.text = description
//                Log.i("title11", title)
//                Log.i("descrip", description)
//            }
//        })
//        task.execute()
    }
//    fun startTask(){
//        val task = MyAsyncTask(this)
//        task.execute()
//    }

//    fun dataChanged(){
//        youtube_title.text = title
//        youtube_info.text = description
//    }

}