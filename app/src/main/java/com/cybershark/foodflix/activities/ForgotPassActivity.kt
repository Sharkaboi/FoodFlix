package com.cybershark.foodflix.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.cybershark.foodflix.R
import kotlinx.android.synthetic.main.activity_forgot_pass.*
import java.lang.Exception

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val spFileName="foodFlixFile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        sharedPreferences=getSharedPreferences(spFileName, Context.MODE_PRIVATE)
        btnRetrieve.setOnClickListener {
            val email= etEmail.text.toString()
            val phone=etPhone.text.toString()
            if(email.isBlank()||phone.isBlank()){
                Toast.makeText(this, "Enter Phone number or PassWord", Toast.LENGTH_SHORT).show()
                etPhone.setText("")
                etEmail.setText("")
            }else if (!isEmailValid(email)){
                Toast.makeText(this,"Invalid Email",Toast.LENGTH_SHORT).show()
            }else if(phone.length<10){
                Toast.makeText(this,"Invalid Phone Number!",Toast.LENGTH_SHORT).show()
            }else if(sharedPreferences.getString("email",null)!=email || sharedPreferences.getString("phone",null)!=phone){
                Toast.makeText(this,"Matching Account for Email and Phone Number not found!",Toast.LENGTH_LONG).show()
            }else{
                homeActivityIntent(
                    sharedPreferences.getString("phone",null),
                    sharedPreferences.getString("pass",null),
                    sharedPreferences.getString("name",null),
                    sharedPreferences.getString("email",null),
                    sharedPreferences.getString("address",null)
                )
            }
        }
    }

    fun forgotPassUp(view: View) =onBackPressed()

    private fun isEmailValid(email: CharSequence) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun homeActivityIntent(phone:String?,pass:String?,name:String?,email:String?,address:String?) {
        try {
            val intent= Intent(this, HomeActivity::class.java)
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
