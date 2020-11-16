package com.example.teamkook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val hand=Handler();
        hand.postDelayed({
            val i=Intent(this,LoginActivity::class.java)
            startActivity(i)
            finish()
        },2500)
    }
}
