package com.example.teamkook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.activity_user.checkpw
import kotlinx.android.synthetic.main.activity_user.crustacean
import kotlinx.android.synthetic.main.activity_user.egg
import kotlinx.android.synthetic.main.activity_user.flour
import kotlinx.android.synthetic.main.activity_user.id
import kotlinx.android.synthetic.main.activity_user.milk
import kotlinx.android.synthetic.main.activity_user.password
import kotlinx.android.synthetic.main.activity_user.peach
import kotlinx.android.synthetic.main.activity_user.peanut
import kotlinx.android.synthetic.main.activity_user.vegan

class UserActivity : AppCompatActivity() {

    val mDatabase= FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val i=intent
        val _id=i.getStringExtra("id") //로그인에서 전달한 아이디 전달받기
        id.setText(_id)
        modify.setOnClickListener {
            modifyInfo(_id)
        }
//        fback.setOnClickListener {
//            val i=Intent(this,MainActivity::class.java)
//            startActivity(i)
//            finish()
//        }
    }

    fun modifyInfo(_id:String){

        if(_id==null){
            Toast.makeText(this,"아이디가 존재하지 않습니다.",Toast.LENGTH_SHORT).show()
        }
        var allcheck=true

        var changepw=true
        if(password.text.toString()==""&&checkpw.text.toString()==""){ //비밀번호 칸 모두 비워져있으면 안바꾸는 것으로 간주
            changepw=false
        }

        if (password.text.toString()!=checkpw.text.toString()){
            Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            password.setText("")
            checkpw.setText("")
            allcheck=false
        }

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

        if(allcheck){
            val database=mDatabase.getReference("Accounts")
            if(changepw){
                database.child(_id).child("pw").setValue(password.text.toString())
            }
            database.child(_id).child("vegan").setValue(_vegan)
            database.child(_id).child("allergy").setValue(allergy)

            Toast.makeText(this,"정보가 변경되었습니다.",Toast.LENGTH_SHORT).show()
            val i=Intent(this,MainActivity::class.java)
            i.putExtra("id",_id)
            startActivity(i)
            finish()
        }


    }
}