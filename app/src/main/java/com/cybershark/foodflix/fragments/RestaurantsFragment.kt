package com.cybershark.foodflix.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cybershark.foodflix.R
import com.cybershark.foodflix.adapters.RestaurantsAdapter
import com.cybershark.foodflix.dataclasses.RestaurantDataClass

class RestaurantsFragment : Fragment() {

    private lateinit var inflatedView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val restaurantList = mutableListOf<RestaurantDataClass>()
        inflatedView=inflater.inflate(R.layout.fragment_restaurants, container, false)
        val rvRestaurants=inflatedView.findViewById<RecyclerView>(R.id.rvRestaurants)
        rvRestaurants.layoutManager= LinearLayoutManager(activity as Context)
        val queue=Volley.newRequestQueue(activity)
        val jsonObjectRequest= object: JsonObjectRequest(
            Method.GET,
            "http://13.235.250.119/v2/restaurants/fetch_result/",
            null,
            Response.Listener {
                val data=it.getJSONObject("data")
                val success=data.getBoolean("success")
                if(success){
                    val resultJSONArray=data.getJSONArray("data")
                    for (i in 0 until resultJSONArray.length()){
                        val resultJSONObject=resultJSONArray.getJSONObject(i)
                        restaurantList.add(
                            RestaurantDataClass(
                                resultJSONObject.getInt("id"),
                                resultJSONObject.getString("name"),
                                resultJSONObject.getDouble("rating").toFloat(),
                                resultJSONObject.getInt("cost_for_one"),
                                resultJSONObject.getString("image_url"),
                                false
                            )
                        )
                    }
                }
            },
            Response.ErrorListener {
                Toast.makeText(activity,"Error retrieving Data from the internet",Toast.LENGTH_SHORT).show()
            }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers=HashMap<String,String>()
                headers["Content-type"]="application/json"
                headers["token"]=getString(R.string.token)
                return headers
            }
        }
        queue.add(jsonObjectRequest)
        rvRestaurants.adapter=RestaurantsAdapter(activity as Context,restaurantList)
        rvRestaurants.adapter!!.notifyDataSetChanged()
        return inflatedView
    }

}
