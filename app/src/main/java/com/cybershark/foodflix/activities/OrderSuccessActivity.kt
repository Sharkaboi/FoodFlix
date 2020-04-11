package com.cybershark.foodflix.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cybershark.foodflix.R
import kotlinx.android.synthetic.main.activity_order_success.*

class OrderSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_success)
        btnOrderOk.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
            finishAffinity()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this,HomeActivity::class.java))
        finishAffinity()
    }
}
