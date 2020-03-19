package com.cybershark.foodflix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bundle=intent.extras
        tvLoginInDetails.append("Phone:"+bundle!!.getString("phone")+"\nPassword:"+bundle.getString("pass"))
    }
}
