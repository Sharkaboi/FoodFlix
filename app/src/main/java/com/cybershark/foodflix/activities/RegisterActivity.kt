package com.cybershark.foodflix.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cybershark.foodflix.R
import com.cybershark.foodflix.util.InternetConnectionManager
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.lang.Exception


class RegisterActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val spFileName="foodFlixFile"
    private lateinit var phone:String
    private lateinit var pass:String
    private lateinit var rePass:String
    private lateinit var name:String
    private lateinit var email:String
    private lateinit var address:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        sharedPreferences=getSharedPreferences(spFileName, Context.MODE_PRIVATE)
    }

    fun signUpBackIntent(view: View) {
        onBackPressed()
    }

    fun singUpConfirm(view: View) {
        phone=etPhone.text.toString()
        pass=etPassword.text.toString()
        rePass=etRePassword.text.toString()
        name=etName.text.toString()
        email=etEmail.text.toString()
        address=etAddress.text.toString()
        if(phone.isBlank() || pass.isBlank() || rePass.isBlank() || name.isBlank() || email.isBlank() || address.isBlank()) {
            Toast.makeText(this, "Values cannot be blank", Toast.LENGTH_SHORT).show()
        }else if (!isEmailValid(email)){
            Toast.makeText(this,"Invalid Email",Toast.LENGTH_SHORT).show()
        }else if (pass.length<6){
          Toast.makeText(this,"Password too Short!",Toast.LENGTH_SHORT).show()
        }else if(name.length<3){
            Toast.makeText(this,"Username too Short!",Toast.LENGTH_SHORT).show()
        } else if(pass != rePass){
            Toast.makeText(this,"Passwords Don't Match!",Toast.LENGTH_SHORT).show()
            etPassword.setText("")
            etRePassword.setText("")
        }else if (phone.length<10){
            Toast.makeText(this,"Invalid Phone Number!",Toast.LENGTH_SHORT).show()
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
                                registerInServer()
                            } else {
                                Toast.makeText(this, "Still Disconnected :(", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .setNegativeButton("Open Settings") { _, _ ->
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }.show()
            }else{
                registerInServer()
            }
        }
    }

    private fun registerInServer() {
        val queue=Volley.newRequestQueue(this)
        val userDetails=makeHashMap().toMap()
        val jsonObjectRequest=object : JsonObjectRequest(
            Method.POST,
            "http://13.235.250.119/v2/register/fetch_result",
            JSONObject(userDetails),
            Response.Listener {
                try {
                    val data=it.getJSONObject("data")
                    if(data.getBoolean("success")){
                        setSharedPreferences(data.getJSONObject("data").getString("user_id"),phone, pass, email, name, address)
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

    private fun makeHashMap():Map<String,String> {
        val userDetails=HashMap<String,String>()
        userDetails["name"] = name
        userDetails["mobile_number"] = phone
        userDetails["password"] = pass
        userDetails["address"] = address
        userDetails["email"] = email
        return userDetails
    }

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


    private fun isEmailValid(email: CharSequence) =Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun setLoggedIn() =sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()

    private fun homeActivityIntent() = startActivity(Intent(this, HomeActivity::class.java))
}
