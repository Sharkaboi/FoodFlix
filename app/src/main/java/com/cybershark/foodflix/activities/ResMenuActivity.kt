package com.cybershark.foodflix.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.cybershark.foodflix.R
import com.cybershark.foodflix.adapters.ResMenuAdapter
import com.cybershark.foodflix.dataclasses.FoodItemDetails
import com.cybershark.foodflix.dataclasses.MenuItemDetails
import com.cybershark.foodflix.dataclasses.RestaurantDataClass
import com.cybershark.foodflix.fragments.RestaurantsFragment
import com.cybershark.foodflix.sqllite.CartAsyncTask
import com.cybershark.foodflix.sqllite.DBAsyncTask
import com.cybershark.foodflix.sqllite.RestaurantEntity
import com.cybershark.foodflix.sqllite.RetrieveTotalOrCountAsyncTask
import com.cybershark.foodflix.util.InternetConnectionManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_res_menu.*
import kotlinx.android.synthetic.main.activity_res_menu.contentLoading
import kotlinx.android.synthetic.main.fragment_restaurants.*

class ResMenuActivity : AppCompatActivity() {

    private lateinit var menuList: MutableList<MenuItemDetails>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_res_menu)

        val bundle=intent.extras
        val res=Gson().fromJson<RestaurantDataClass>(bundle?.getString("res"),RestaurantDataClass::class.java)
        val res_id=res.id
        var isFav=res.fav

        toolbar.findViewById<TextView>(R.id.tvToolbar).text= res.name
        toolbar.title=""
        setSupportActionBar(toolbar)

        if(isFav)
            Glide.with(this).load(R.drawable.ic_favorite_selected).into(ivFavMenu)
        else
            Glide.with(this).load(R.drawable.ic_favorite_unselected).into(ivFavMenu)

        ivFavMenu.setOnClickListener {
            if(isFav){
                //remove from DB
                val result=DBAsyncTask(this, RestaurantEntity(res_id,res.name,res.rating,res.price,res.image,true),3).execute().get()
                if(result){
                    Glide.with(this).load(R.drawable.ic_favorite_unselected).into(ivFavMenu)
                    isFav=false
                    res.fav=false
                }
            }else{
                //add to DB
                val result=DBAsyncTask(this, RestaurantEntity(res_id,res.name,res.rating,res.price,res.image,true),2).execute().get()
                if(result){
                    Glide.with(this).load(R.drawable.ic_favorite_selected).into(ivFavMenu)
                    isFav=true
                    res.fav=true
                }
            }
        }

        btnCart.setOnClickListener {
            val cartCount=RetrieveTotalOrCountAsyncTask(this,2).execute().get()
            if(cartCount==0){
                Toast.makeText(this,"Add Dishes to proceed!",Toast.LENGTH_SHORT).show()
            }else {
                val intent = Intent(this, CartActivity::class.java)
                intent.putExtra("resID", res_id)
                intent.putExtra("resName", res.name)
                startActivity(intent)
            }
        }

        menuList= mutableListOf()
        try {
            if (InternetConnectionManager().isNetworkAccessActive(this)) {
                rvResMenu.layoutManager=LinearLayoutManager(this)

                val queue=Volley.newRequestQueue(this)
                val jsonObjectRequest=object :JsonObjectRequest(
                    Method.GET,
                    "http://13.235.250.119/v2/restaurants/fetch_result/$res_id",
                    null,
                    Response.Listener {
                        val data=it.getJSONObject("data")
                        if(data.getBoolean("success")) {
                            val jsonArrayFromAPI=data.getJSONArray("data")
                            for (i in 0 until jsonArrayFromAPI.length()){
                                val jsonObject=jsonArrayFromAPI.getJSONObject(i)
                                menuList.add(Gson().fromJson<MenuItemDetails>(jsonObject.toString(),MenuItemDetails::class.java))
                            }
                            rvResMenu.adapter= ResMenuAdapter(this,menuList)
                            rvResMenu.adapter?.notifyDataSetChanged()
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
                    .setPositiveButton("Retry") { dialog, which ->
                            if (InternetConnectionManager().isNetworkAccessActive(this)) {
                                dialog.dismiss()
                                val intent=Intent(this,ResMenuActivity::class.java)
                                intent.putExtra("res",Gson().toJson(res))
                                startActivity(intent)
                                finish()
                            } else {
                               Toast.makeText(this, "Still Disconnected :(", Toast.LENGTH_SHORT).show()
                                onBackPressed()
                            }
                    }
                    .setNegativeButton("Open Settings") { dialog, which ->
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

    override fun onBackPressed() {
        if(RetrieveTotalOrCountAsyncTask(this,2).execute().get()==0){
            val result = CartAsyncTask(this, operationIndex = 4).execute().get()
            if (result) {
                super.onBackPressed()
            }
        }else {
            AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warn)
                .setTitle("Clear Cart?")
                .setMessage("Going back will clear the cart,\nDo you wish to still proceed?")
                .setPositiveButton("Yes") { dialog, which ->
                    val result = CartAsyncTask(this, operationIndex = 4).execute().get()
                    if (result) {
                        dialog.dismiss()
                        super.onBackPressed()
                    }
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }
    fun navUp(view: View) = onBackPressed()
}
