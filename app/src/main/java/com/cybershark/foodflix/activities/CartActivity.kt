package com.cybershark.foodflix.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cybershark.foodflix.R
import com.cybershark.foodflix.adapters.OrderHistoryChildAdapter
import com.cybershark.foodflix.dataclasses.FoodItemDetails
import com.cybershark.foodflix.sqllite.CartAsyncTask
import com.cybershark.foodflix.sqllite.RetrieveCartItemsAsyncTask
import com.cybershark.foodflix.sqllite.RetrieveTotalOrCountAsyncTask
import com.cybershark.foodflix.util.InternetConnectionManager
import kotlinx.android.synthetic.main.activity_cart.*
import org.json.JSONArray
import org.json.JSONObject

class CartActivity : AppCompatActivity() {

    //class variables
    private lateinit var sharedPreferences: SharedPreferences
    private var spFileName="foodFlixFile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inflate layout
        setContentView(R.layout.activity_cart)

        //set custom toolbar with title in separate text view so it doesn't overlap nav up button
        toolbar.findViewById<TextView>(R.id.tvToolbar).text= ("My Cart")
        toolbar.title=""
        setSupportActionBar(toolbar)

        //getting shared preferences and storing user id
        sharedPreferences=getSharedPreferences(spFileName,Context.MODE_PRIVATE)
        val userID=sharedPreferences.getString("id",null)

        //reads restaurant name and id for text view and API
        val bundle=intent.extras
        val name=bundle?.getString("resName")
        tvCartResName.text=name
        val resId=bundle?.getInt("resID")

        //get total from sql command through room
        val total= RetrieveTotalOrCountAsyncTask(this,1).execute().get()
        btnCartOrder.text=("Place Order ( Total : Rs.$total )")

        //initializing cart item json array for json params
        val cartItemsListJSONArray=JSONArray()

        rvCart.layoutManager=LinearLayoutManager(this)
        val tempCartItemsList=RetrieveCartItemsAsyncTask(this).execute().get()
        val cartItemsList= mutableListOf<FoodItemDetails>()
        for (i in tempCartItemsList.indices){
            cartItemsList.add(FoodItemDetails(tempCartItemsList[i].id,tempCartItemsList[i].name,tempCartItemsList[i].cost.toString()))
            val tempJSONObject=JSONObject()
            tempJSONObject.put("food_item_id",tempCartItemsList[i].id)
            cartItemsListJSONArray.put(tempJSONObject)
        }
        rvCart.adapter=OrderHistoryChildAdapter(this,cartItemsList)
        rvCart.adapter?.notifyDataSetChanged()

        contentLoading.visibility=View.GONE

        btnCartOrder.setOnClickListener {
            try{
                if(InternetConnectionManager().isNetworkAccessActive(this)){
                    contentLoading.visibility=View.VISIBLE
                    val queue=Volley.newRequestQueue(this)
                    val jsonParams=JSONObject()
                    jsonParams.put("user_id",userID)
                    jsonParams.put("restaurant_id",resId)
                    jsonParams.put("total_cost",total)
                    jsonParams.put("food",cartItemsListJSONArray)
                    val jsonObjectRequest=object :JsonObjectRequest(
                        Method.POST,
                        "http://13.235.250.119/v2/place_order/fetch_result/",
                        jsonParams,
                        Response.Listener {
                            val data=it.getJSONObject("data")
                            if(data.getBoolean("success")){
                                orderSuccessful()
                            }else{
                                Toast.makeText(this, "Error retrieving Data from the server!", Toast.LENGTH_SHORT).show()
                            }
                            contentLoading.visibility = View.GONE
                        },
                        Response.ErrorListener {
                            Toast.makeText(this, "Error retrieving Data from the internet!", Toast.LENGTH_SHORT).show()
                            contentLoading.visibility = View.GONE
                        }
                    ){
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = getString(R.string.token)
                            return headers
                        }
                    }
                    queue.add(jsonObjectRequest)
                }else{
                     AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_no_wifi)
                        .setTitle("No Internet")
                        .setMessage("Internet Access has been Restricted.")
                        .setPositiveButton("Retry") { dialog, _ ->
                            if (InternetConnectionManager().isNetworkAccessActive(this)) {
                                dialog.dismiss()
                                val intent=Intent(this,CartActivity::class.java)
                                intent.putExtra("resName", name)
                                intent.putExtra("resID",resId)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Still Disconnected :(", Toast.LENGTH_SHORT).show()
                                onBackPressed()
                            }
                        }
                        .setNegativeButton("Open Settings") { _, _ ->
                            startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                        }
                        .setCancelable(false)
                        .show()
                }
            }catch (e:Exception){
                Toast.makeText(this, "An Unexpected Error has occurred!", Toast.LENGTH_SHORT).show()
                Log.e("foodflix",e.message.toString())
            }
        }
    }

    private fun orderSuccessful(){
        CartAsyncTask(this,operationIndex = 4).execute().get()
        startActivity(Intent(this,OrderSuccessActivity::class.java))
        finishAffinity()
    }

    fun navUp(view: View) =onBackPressed()
}
