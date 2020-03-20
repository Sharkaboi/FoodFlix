package com.cybershark.foodflix

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
            homeActivityIntent(
                sharedPreferences.getString("phone",null),
                sharedPreferences.getString("pass",null),
                sharedPreferences.getString("name",null),
                sharedPreferences.getString("email",null),
                sharedPreferences.getString("address",null)
            )

        setContentView(R.layout.activity_login)

        btnSignIn.setOnClickListener {
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
        if(phone.isBlank() || pass.isBlank()) {
            Toast.makeText(this, "Enter Phone number or PassWord", Toast.LENGTH_SHORT).show()
            etPhone.setText("")
            etPassword.setText("")
        }
        else if(phone == sharedPreferences.getString("phone",null)&& pass == sharedPreferences.getString("pass",null)){
            Toast.makeText(this,"Logging In...",Toast.LENGTH_SHORT).show()
            homeActivityIntent(
                phone,
                pass,
                sharedPreferences.getString("name",null),
                sharedPreferences.getString("email",null),
                sharedPreferences.getString("address",null)
            )
            setLoggedIn()
        }else{
            Toast.makeText(this,"Wrong Password or Phone Number",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setLoggedIn() =sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()

    private fun homeActivityIntent(phone:String?,pass:String?,name:String?,email:String?,address:String?) {
        try {
            val intent=Intent(this,HomeActivity::class.java)
            intent.putExtra("phone",phone)
            intent.putExtra("pass",pass)
            intent.putExtra("name",name)
            intent.putExtra("email",email)
            intent.putExtra("address",address)
            startActivity(intent)
            finish()
        }catch (e:Exception){
            Toast.makeText(this,"An Error has occurred! Try Again",Toast.LENGTH_SHORT).show()
            sharedPreferences.edit().clear().apply()
        }
    }

    fun signUpIntent(view: View) =startActivity(Intent(this,RegisterActivity::class.java))

    fun forgotPasswordIntent(view: View) =startActivity(Intent(this,ForgotPassActivity::class.java))

}
