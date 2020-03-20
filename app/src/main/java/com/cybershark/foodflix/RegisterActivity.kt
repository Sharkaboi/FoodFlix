package com.cybershark.foodflix

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception


class RegisterActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val spFileName="foodFlixFile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        sharedPreferences=getSharedPreferences(spFileName, Context.MODE_PRIVATE)
    }

    fun signUpBackIntent(view: View) {
        onBackPressed()
    }

    fun singUpConfirm(view: View) {
        val phone=etPhone.text.toString()
        val pass=etPassword.text.toString()
        val rePass=etRePassword.text.toString()
        val name=etName.text.toString()
        val email=etEmail.text.toString()
        val address=etAddress.text.toString()
        if(phone.isBlank() || pass.isBlank() || rePass.isBlank() || name.isBlank() || email.isBlank() || address.isBlank()) {
            Toast.makeText(this, "Enter Details", Toast.LENGTH_SHORT).show()
            etPhone.setText("")
            etAddress.setText("")
            etPassword.setText("")
            etRePassword.setText("")
            etEmail.setText("")
            etName.setText("")
        }else if (!isEmailValid(email)){
            Toast.makeText(this,"Invalid Email",Toast.LENGTH_SHORT).show()
        }else if (pass.length<8){
          Toast.makeText(this,"Password too Short!",Toast.LENGTH_SHORT).show()
        } else if(pass != rePass){
            Toast.makeText(this,"Passwords Don't Match!",Toast.LENGTH_SHORT).show()
            etPassword.setText("")
            etRePassword.setText("")
        }else if (phone.length<10){
            Toast.makeText(this,"Invalid Phone Number!",Toast.LENGTH_SHORT).show()
        }else if(name.length<=3){
            Toast.makeText(this,"Name too short!",Toast.LENGTH_SHORT).show()
        }else if (sharedPreferences.getString("phone",null)==phone || sharedPreferences.getString("email",null)==email){
            Toast.makeText(this,"Account Already Exists!",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"Logging In...",Toast.LENGTH_SHORT).show()
            setSharedPreferences(phone,pass,email,name,address)
            homeActivityIntent(phone,pass,name,email,address)
            setLoggedIn()
        }
    }

    private fun setSharedPreferences(
        phone: String,
        pass: String,
        email: String,
        name: String,
        address: String
    ) = sharedPreferences.edit()
            .putString("phone",phone)
            .putString("pass",pass)
            .putString("name",name)
            .putString("email",email)
            .putString("address",address)
            .apply()


    private fun isEmailValid(email: CharSequence) =Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun setLoggedIn() =sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()

    private fun homeActivityIntent(phone:String,pass:String,name:String,email:String,address:String) {
        try {
            val intent= Intent(this,HomeActivity::class.java)
            intent.putExtra("phone",phone)
            intent.putExtra("pass",pass)
            intent.putExtra("name",name)
            intent.putExtra("email",email)
            intent.putExtra("address",address)
            startActivity(intent)
            finish()
        }catch (e: Exception){
            Toast.makeText(this,"An Error has occurred! Try Again",Toast.LENGTH_SHORT).show()
            sharedPreferences.edit().clear().apply()
        }
    }
}
