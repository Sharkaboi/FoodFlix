package com.cybershark.foodflix.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //directly loads to Next Activity thus not wasting user's time and taking exactly as much time as the phone take's to load the next activity.
        startActivity(Intent(this,
            LoginActivity::class.java))
        finish()
    }
}
