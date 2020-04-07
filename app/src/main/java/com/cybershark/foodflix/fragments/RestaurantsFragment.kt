package com.cybershark.foodflix.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cybershark.foodflix.R
import com.cybershark.foodflix.adapters.RestaurantsAdapter
import com.cybershark.foodflix.dataclasses.RestaurantDataClass
import com.cybershark.foodflix.sqllite.DBAsyncTask
import com.cybershark.foodflix.sqllite.ResDB
import com.cybershark.foodflix.sqllite.RestaurantEntity
import com.cybershark.foodflix.util.InternetConnectionManager
import java.lang.Exception

class RestaurantsFragment : Fragment() {

    private lateinit var inflatedView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_restaurants, container, false)
        try {
            if (InternetConnectionManager().isNetworkAccessActive(activity as Context)) {

                val restaurantList = mutableListOf<RestaurantDataClass>()
                val rvRestaurants = inflatedView.findViewById<RecyclerView>(R.id.rvRestaurants)

                rvRestaurants.layoutManager = LinearLayoutManager(activity as Context)
                rvRestaurants.adapter = RestaurantsAdapter(activity as Context, restaurantList)

                val queue = Volley.newRequestQueue(activity as Context)
                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.GET,
                    "http://13.235.250.119/v2/restaurants/fetch_result/",
                    null,
                    Response.Listener {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val resultJSONArray = data.getJSONArray("data")
                            for (i in 0 until resultJSONArray.length()) {
                                val resultJSONObject = resultJSONArray.getJSONObject(i)
                                val id= resultJSONObject.getInt("id")
                                val name=resultJSONObject.getString("name")
                                val rating=resultJSONObject.getDouble("rating").toFloat()
                                val cost=resultJSONObject.getInt("cost_for_one")
                                val image=resultJSONObject.getString("image_url")
                                var fav=false
                                val resEntity=RestaurantEntity(id, name, rating, cost, image, false)
                                val checkFav= DBAsyncTask(activity!!.applicationContext,resEntity,1).execute().get()
                                if(checkFav)
                                   fav=true
                                restaurantList.add(
                                    RestaurantDataClass(id, name, rating, cost, image, fav)
                                )
                            }
                        }
                        //TODO:check or swap adapter
                        rvRestaurants.adapter!!.notifyDataSetChanged()
                        inflatedView.findViewById<ProgressBar>(R.id.contentLoading).visibility=View.GONE
                    },
                    Response.ErrorListener {
                        Toast.makeText(activity, "Error retrieving Data from the internet", Toast.LENGTH_SHORT).show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = getString(R.string.token)
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)

            } else {
                AlertDialog.Builder(activity as Context)
                    .setIcon(R.drawable.ic_no_wifi)
                    .setTitle("No Internet")
                    .setMessage("Internet Access has been Restricted.")
                    .setPositiveButton("Retry") { dialog, which ->
                        if (InternetConnectionManager().isNetworkAccessActive(activity as Context)) {
                            dialog.dismiss()
                        } else {
                            Toast.makeText(activity as Context, "Still Disconnected :(", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Open Settings") { dialog, which ->
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                        activity!!.finish()
                    }
                    .setCancelable(false)
                    .show()
            }
        }catch (e:Exception){
            Toast.makeText(activity as Context, "An Unexpected Error has occured!", Toast.LENGTH_SHORT).show()
            Log.e("foodflix",e.message.toString())
        }
        return inflatedView
    }
}
