package com.cybershark.foodflix

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val spFileName="foodFlixFile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        sharedPreferences=getSharedPreferences(spFileName, Context.MODE_PRIVATE)
        val bundle=intent.extras

        tvLoginInDetails.append(
            "Name:"+bundle!!.getString("name")+
            "\nEmail:"+bundle.getString("email")+
            "\nPhone:"+bundle.getString("phone")+
            "\nAddress:"+bundle.getString("address")+
            "\nPassword:"+bundle.getString("pass")
        )

        btnLogOut.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
        }

    }
}
