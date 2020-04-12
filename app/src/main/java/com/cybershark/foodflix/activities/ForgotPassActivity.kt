package com.cybershark.foodflix.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cybershark.foodflix.R
import kotlinx.android.synthetic.main.activity_forgot_pass.*
import org.json.JSONObject
import java.lang.Exception

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var email:String
    private lateinit var phone:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        email=""
        phone=""
        btnRetrieve.setOnClickListener {
            email= etEmail.text.toString()
            phone=etPhone.text.toString()
            if(email.isBlank()||phone.isBlank()){
                Toast.makeText(this, "Enter Phone number or PassWord", Toast.LENGTH_SHORT).show()
                etPhone.setText("")
                etEmail.setText("")
            }else if (!isEmailValid(email)){
                Toast.makeText(this,"Invalid Email",Toast.LENGTH_SHORT).show()
            }else if(phone.length<10){
                Toast.makeText(this,"Invalid Phone Number!",Toast.LENGTH_SHORT).show()
            }else{
                contentLoading.visibility=View.VISIBLE
                forgotPassAPIFunc()
            }
        }
    }

    private fun forgotPassAPIFunc() {
        val queue= Volley.newRequestQueue(this)
        val userDetails= JSONObject()
        userDetails.put("mobile_number", phone)
        userDetails.put("email",email)
        val jsonObjectRequest=object : JsonObjectRequest(
            Method.POST,
            "http://13.235.250.119/v2/forgot_password/fetch_result",
            userDetails,
            Response.Listener {
                try {
                    val data=it.getJSONObject("data")
                    if(data.getBoolean("success")){
                        if (!data.getBoolean("first_try"))
                            Toast.makeText(this,"Use same OTP for 24 Hours",Toast.LENGTH_LONG).show()
                        otpActivityIntent()
                    }else{
                        contentLoading.visibility=View.GONE
                        Toast.makeText(this, "Error: ${data.getString("errorMessage")}", Toast.LENGTH_SHORT).show()
                    }
                }catch (e:Exception){
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

    fun forgotPassUp(view: View) =onBackPressed()

    private fun isEmailValid(email: CharSequence) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun otpActivityIntent() {
        val intent=Intent(this, OtpActivity::class.java)
        intent.putExtra("phone",phone)
        startActivity(intent)
        finishAffinity()
    }
}
