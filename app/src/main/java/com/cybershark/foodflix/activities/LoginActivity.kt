package com.cybershark.foodflix.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cybershark.foodflix.R
import com.cybershark.foodflix.util.InternetConnectionManager
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private val spFileName="foodFlixFile"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var phone:String
    private lateinit var pass:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences=getSharedPreferences(spFileName, Context.MODE_PRIVATE)

        phone=""
        pass=""

        if(sharedPreferences.getBoolean("isLoggedIn",false))
            homeActivityIntent()

        setContentView(R.layout.activity_login)

        btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        phone=etPhone.text.toString()
        pass=etPassword.text.toString()
        if(phone.isBlank() || pass.isBlank()) {
            Toast.makeText(this, "Enter Phone number or PassWord", Toast.LENGTH_SHORT).show()
            etPhone.setText("")
            etPassword.setText("")
        }
        else if(phone.length!=10) {
            Toast.makeText(this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show()
        }else if(pass.length<4) {
            Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show()
        }else{
            if(!InternetConnectionManager().isNetworkAccessActive(this)){
                AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_no_wifi)
                    .setTitle("No Internet")
                    .setMessage("Internet Access has been Restricted.")
                    .setPositiveButton("Retry") { dialog, _ ->
                        if (InternetConnectionManager().isNetworkAccessActive(this)) {
                            dialog.dismiss()
                            loginWithAPICheck()
                        } else {
                            Toast.makeText(this, "Still Disconnected :(", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Open Settings") { _, _ ->
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }.show()
            }else{
                loginWithAPICheck()
            }
        }
    }

    private fun loginWithAPICheck() {
        val queue= Volley.newRequestQueue(this)
        val userDetails= JSONObject()
        userDetails.put("mobile_number", phone)
        userDetails.put("password",pass)
        val jsonObjectRequest=object : JsonObjectRequest(
            Method.POST,
            "http://13.235.250.119/v2/login/fetch_result",
            userDetails,
            Response.Listener {
                try {
                    val data=it.getJSONObject("data")
                    if(data.getBoolean("success")){
                        setSharedPreferences(data.getJSONObject("data").getString("user_id"),phone, pass, data.getJSONObject("data").getString("email"), data.getJSONObject("data").getString("name"), data.getJSONObject("data").getString("address"))
                        Toast.makeText(this,"Logging In...",Toast.LENGTH_SHORT).show()
                        homeActivityIntent()
                        setLoggedIn()
                        finishAffinity()
                    }else{
                        Toast.makeText(this, "Error: ${data.getString("errorMessage")}", Toast.LENGTH_SHORT).show()
                    }
                }catch (e:Exception){
                    Toast.makeText(this, "An Unexpected Error has occurred!", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
                Toast.makeText(this, "Error saving Data to the server!", Toast.LENGTH_SHORT).show()
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

    private fun setLoggedIn() =sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()

    private fun homeActivityIntent() {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
    }

    fun signUpIntent(view: View) =startActivity(Intent(this, RegisterActivity::class.java))

    fun forgotPasswordIntent(view: View) =startActivity(Intent(this, ForgotPassActivity::class.java))

    private fun setSharedPreferences(
        id: String,
        phone: String,
        pass: String,
        email: String,
        name: String,
        address: String
    ) = sharedPreferences.edit()
        .putString("id",id)
        .putString("phone",phone)
        .putString("pass",pass)
        .putString("name",name)
        .putString("email",email)
        .putString("address",address)
        .apply()

}
