package com.cybershark.foodflix.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cybershark.foodflix.R
import kotlinx.android.synthetic.main.activity_otp.*
import org.json.JSONObject
import java.lang.Exception

class OtpActivity : AppCompatActivity() {

    private val spFileName="foodFlixFile"
    private lateinit var otp:String
    private lateinit var pass:String
    private lateinit var phone:String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        sharedPreferences=getSharedPreferences(spFileName, Context.MODE_PRIVATE)
        otp=""
        pass=""
        phone=intent.extras?.getString("phone")!!
        btnRetrieve.setOnClickListener {
            otp=etOtp.text.toString()
            pass=etPassword.text.toString()
            val rePass=etRePassword.text.toString()
            when {
                otp.length<4 -> {
                    Toast.makeText(this,"Invalid OTP.",Toast.LENGTH_SHORT).show()
                }
                pass!=rePass -> {
                    Toast.makeText(this,"Passwords Don't Match!",Toast.LENGTH_SHORT).show()
                    etPassword.setText("")
                    etRePassword.setText("")
                }
                pass.length<6 -> {
                    Toast.makeText(this,"Password should be at least 6 characters.",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    contentLoading.visibility=View.VISIBLE
                    checkOtpWithAPI()
                }
            }
        }
    }

    private fun checkOtpWithAPI() {
        val queue= Volley.newRequestQueue(this)
        val userDetails= JSONObject()
        userDetails.put("mobile_number", phone)
        userDetails.put("password",pass)
        userDetails.put("otp",otp)
        val jsonObjectRequest=object : JsonObjectRequest(
            Method.POST,
            "http://13.235.250.119/v2/reset_password/fetch_result",
            userDetails,
            Response.Listener {
                try {
                    val data=it.getJSONObject("data")
                    if(data.getBoolean("success")){
                        sharedPreferences.edit().clear().apply()
                        signInActivityIntent()
                    }else{
                        contentLoading.visibility=View.GONE
                        Toast.makeText(this, "An Unexpected Server Error has occurred!", Toast.LENGTH_SHORT).show()
                    }
                }catch (e: Exception){
                    contentLoading.visibility=View.GONE
                    Toast.makeText(this, "An Unexpected Error has occurred!", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
                contentLoading.visibility=View.GONE
                Toast.makeText(this, "Error sending Data to the server!", Toast.LENGTH_SHORT).show()
                Log.e("foodflix",it.message.toString())
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = getString(R.string.token)
                return headers
            }
        }
        queue.add(jsonObjectRequest)
    }

    private fun signInActivityIntent() {
        startActivity(Intent(this,LoginActivity::class.java))
        finishAffinity()
    }
}
