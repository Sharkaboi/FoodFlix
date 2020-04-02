package com.cybershark.foodflix.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cybershark.foodflix.R
import com.cybershark.foodflix.util.InternetConnectionManager
import kotlinx.android.synthetic.main.activity_no_internet.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //directly loads to Next Activity thus not wasting user's time and taking exactly as much time as the phone take's to load the next activity.
        if (InternetConnectionManager().isNetworkAccessActive(this)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            setContentView(R.layout.activity_no_internet)
            tvRetry.setOnClickListener {
                if (InternetConnectionManager().isNetworkAccessActive(this)) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this,"Still Disconnected :(",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
