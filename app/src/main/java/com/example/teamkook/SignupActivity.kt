package com.example.teamkook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_signup.*
import kotlin.check

class SignupActivity : AppCompatActivity() {

    val mDatabase= FirebaseDatabase.getInstance()
    var idCheck=true //아이디 중복검사했는지 확인하는 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        //checkCompleted()
    }

    override fun onStart() {
        super.onStart()
        idCheck=true
        checkCompleted()
    }

    fun checkCompleted(){
        redundancycheck.setOnClickListener {
            val inputid=id.text.toString()
            if(inputid!=""){
                idRedundancy(inputid)
            }else{
                Toast.makeText(this,"아이디를 입력해주세요.",Toast.LENGTH_SHORT).show()
                idCheck=false
            }
        }
        complete.setOnClickListener {
            if(inputData()){//회원가입 완료->로그인 창으로 넘어감
                Toast.makeText(this,"회원가입 완료되었습니다.",Toast.LENGTH_SHORT).show()
                val i= Intent(this,LoginActivity::class.java)
                startActivity(i)
                finish()
            }else{
                Toast.makeText(this,"회원가입에 실패했습니다.",Toast.LENGTH_SHORT).show()
            }
        }
        back.setOnClickListener {
            val i=Intent(this,LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    fun idRedundancy(_id:String){

           val database=mDatabase.getReference("Accounts") //account 테이블
           database.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   //check=true
                   for(shot in snapshot.children){
                       if(shot.key.toString() == _id) //child가 id와 일치하면 아이디 중복
                       {
                           Toast.makeText(this@SignupActivity,"사용중인 아이디입니다.",Toast.LENGTH_SHORT).show()
                           id.setText("")
                           idCheck=false
                        }
                   }
               }
               override fun onCancelled(error: DatabaseError) {
                   //TODO("Not yet implemented")
                   Log.e("database error",error.message)
               }
           })
    }

    fun inputData():Boolean{

        //아이디 확인은 중복검사하면서 진행했음
        if(idCheck&&id.text.toString()==""){//아이디 처음부터 입력안하고 버튼 누를 때 방지
            idCheck=false
        }

        //비밀번호 확인
        var accuracy=true
        val _pw=password.text.toString()
        if(_pw!=checkpw.text.toString()){
            accuracy=false
        }

        //나이
        val _age=age.text.toString()

        //성별
        var gender=""
        when(radiogroup.checkedRadioButtonId){
            R.id.female->{gender="여"}
            R.id.male->{gender="남"}
        }

        //비건
        var _vegan=false
        if(vegan.isChecked) _vegan=true

        //알레르기
        var allergy=ArrayList<String>()
        if(peanut.isChecked){ allergy.add("땅콩") }
        if(milk.isChecked){ allergy.add("우유") }
        if(egg.isChecked){ allergy.add("달걀") }
        if(crustacean.isChecked){ allergy.add("갑각류") }
        if(flour.isChecked){ allergy.add("밀가루") }
        if(peach.isChecked){ allergy.add("복숭아") }

        //데이터 검사 확인 후 데이터베이스에 집어넣기
        if(idCheck) {//아이디 중복검사가 완료되어있어야함
            if(accuracy){//비밀번호 일치 확인
                //데이터클래스에 데이터들 집어넣기
                val personal=Account(id.text.toString(),_pw,_age.toInt(),gender,_vegan,allergy)

                //파이어베이스에 항목 만들기
                val rdatabese=mDatabase.getReference("Accounts")
                rdatabese.child(id.text.toString()).setValue(personal)

                return true
            }else{
                Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
                password.setText("")
                checkpw.setText("")
            }
        }else{
            Toast.makeText(this,"아이디 중복검사를 해주세요.",Toast.LENGTH_SHORT).show()
        }

        return false
    }
}
