package com.example.teamkook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val mDatabase=FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnClick()
    }

    fun btnClick(){
        signup.setOnClickListener {
            //회원가입 창으로 넘기기
            val i=Intent(this,SignupActivity::class.java)
            startActivity(i)
            finish()
        }
        login.setOnClickListener {
            //로그인 항목 확인 후 화면 넘기기
            val _id=id.text.toString()
            val _pw=password.text.toString()

            checkAccount(_id,_pw)
            val hand= Handler();
            hand.postDelayed({
                //Toast.makeText(this@LoginActivity,"아이디와 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
                id.setText("")
                password.setText("")
            },2000) //데이터 늘어나면 시간 늘려야되나?

        }

    }

    fun checkAccount(id:String,pw: String){ //firebase에 id가 존재하는지 확인

        val database=mDatabase.getReference("Accounts") //account 테이블
        database.addValueEventListener(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                //check=true
                for(shot in snapshot.children){
                    if(shot.key.toString() == id) //child가 id와 일치하면
                    {
                        //Toast.makeText(this@LoginActivity,"야 떴다 "+shot.key.toString(),Toast.LENGTH_SHORT).show()
                        //check=true //checkID가 반환되는 시간이 데이터 비교하는 시간보다 훨씬 빠르기 때문에 check가 true가 되기 전에 반환되어 안되었던 것
                        val account=shot.getValue(Account::class.java)
                        if(account!=null){
                            if(account.pw==pw){
                                Toast.makeText(this@LoginActivity,id+"님 환영합니다.",Toast.LENGTH_SHORT).show()
                                val i=Intent(this@LoginActivity, MainActivity::class.java)
                                i.putExtra("id",id)
                                startActivity(i)
                                finish()
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
                Log.e("database error",error.message)
            }
        })

    }

}
