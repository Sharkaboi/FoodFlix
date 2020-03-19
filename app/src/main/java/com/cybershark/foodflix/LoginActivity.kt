package com.cybershark.foodflix

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private val spFileName="foodFlixFile"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences=getSharedPreferences(spFileName, Context.MODE_PRIVATE)

        if(sharedPreferences.getBoolean("isLoggedIn",false))
            homeActivityIntent(sharedPreferences.getString("phone",null),sharedPreferences.getString("pass",null))

        setContentView(R.layout.activity_login)

        btnSignUp.setOnClickListener {
            signIn()
        }
        tvForgotPass.setOnClickListener {
            forgotPass()
        }
        tvSignUp.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() = startActivity(Intent(this,RegisterActivity::class.java))

    private fun forgotPass() = startActivity(Intent(this,ForgotPassActivity::class.java))

    private fun signIn() {
        val phone=etPhone.text.toString()
        val pass=etPassword.text.toString()
        if(phone.isBlank() || pass.isBlank())
            Toast.makeText(this,"Invalid Phone number or PassWord",Toast.LENGTH_SHORT).show()
        else{
            homeActivityIntent(phone,pass)
            setLoggedIn(phone,pass)
        }
    }

    private fun setLoggedIn(phone:String,pass:String) =sharedPreferences.edit().putBoolean("isLoggedIn",true).putString("phone",phone).putString("pass",pass).apply()

    private fun homeActivityIntent(phone:String?,pass:String?) {
        try {
            val intent=Intent(this,HomeActivity::class.java)
            intent.putExtra("phone",phone)
            intent.putExtra("pass",pass)
            startActivity(intent)
            finish()
        }catch (e:Exception){
            Toast.makeText(this,"An Error has occurred! Try Again",Toast.LENGTH_SHORT).show()
            sharedPreferences.edit().clear().apply()
        }
    }
}
