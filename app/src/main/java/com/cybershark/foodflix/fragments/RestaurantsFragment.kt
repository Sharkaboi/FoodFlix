package com.cybershark.foodflix.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cybershark.foodflix.R
import com.cybershark.foodflix.adapters.RestaurantsAdapter
import com.cybershark.foodflix.dataclasses.RestaurantDataClass
import com.cybershark.foodflix.sqllite.DBAsyncTask
import com.cybershark.foodflix.sqllite.RestaurantEntity
import com.cybershark.foodflix.util.InternetConnectionManager
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class RestaurantsFragment : Fragment() {

    private lateinit var inflatedView: View
    private var ratingComparator= Comparator<RestaurantDataClass>{ res1, res2 ->
            if((res1.rating.compareTo(res2.rating))==0){
                res1.name.compareTo(res2.name)*-1
            }else{
                res1.rating.compareTo(res2.rating)
            }
    }
    private var priceHighLowComparator= Comparator<RestaurantDataClass>{ res1,res2->
        if((res1.price.compareTo(res2.price))==0){
            res1.name.compareTo(res2.name)*-1
        }else{
            res1.price.compareTo(res2.price)
        }
    }
    private var priceLowHighComparator= Comparator<RestaurantDataClass>{ res1,res2->
        if((res1.price.compareTo(res2.price))==0){
            res1.name.compareTo(res2.name)
        }else{
            res1.price.compareTo(res2.price)
        }
    }
    private lateinit var restaurantList: MutableList<RestaurantDataClass>
    private lateinit var rvRestaurants:RecyclerView
    private lateinit var resSearchView:SearchView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //has menu only for this fragment
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_restaurants, container, false)
        try {
            if (InternetConnectionManager().isNetworkAccessActive(activity as Context)) {

                restaurantList = mutableListOf()
                resSearchView=inflatedView.findViewById(R.id.resSearchView)
                rvRestaurants = inflatedView.findViewById(R.id.rvRestaurants)

                rvRestaurants.layoutManager = LinearLayoutManager(activity as Context)
                val adapter=RestaurantsAdapter(activity as Context, restaurantList)
                rvRestaurants.adapter = adapter

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
                                val resEntity=RestaurantEntity(id, name, rating, cost, image, true)
                                val checkFav= DBAsyncTask(activity!!.applicationContext,resEntity,1).execute().get()
                                if(checkFav)
                                   fav=true
                                restaurantList.add(
                                    RestaurantDataClass(id, name, rating, cost, image, fav)
                                )
                            }
                            /*resSearchView.setOnQueryTextListener(object :
                                SearchView.OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String): Boolean {
                                    return false
                                }
                                override fun onQueryTextChange(newText: String): Boolean {
                                    adapter.getFilter()?.filter(newText)
                                    return false
                                }
                            })*/
                        }
                        rvRestaurants.adapter!!.notifyDataSetChanged()
                        inflatedView.findViewById<ProgressBar>(R.id.contentLoading).visibility=View.GONE
                    },
                    Response.ErrorListener {
                        if (activity!=null) {
                            Toast.makeText(activity, "Error retrieving Data from the internet", Toast.LENGTH_SHORT).show()
                            inflatedView.findViewById<ProgressBar>(R.id.contentLoading).visibility = View.GONE
                        }
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
                if(activity!=null)
                AlertDialog.Builder(activity as Context)
                    .setIcon(R.drawable.ic_no_wifi)
                    .setTitle("No Internet")
                    .setMessage("Internet Access has been Restricted.")
                    .setPositiveButton("Retry") { dialog, which ->
                        if (activity!=null)
                        if (InternetConnectionManager().isNetworkAccessActive(activity as Context)) {
                            dialog.dismiss()
                            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,RestaurantsFragment()).commit()
                        } else {
                            if(activity!=null)
                            Toast.makeText(activity as Context, "Still Disconnected :(", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Open Settings") { dialog, which ->
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }
                    .setCancelable(false)
                    .show()
            }
        }catch (e:Exception){
            if(activity!=null)
            Toast.makeText(activity as Context, "An Unexpected Error has occurred!", Toast.LENGTH_SHORT).show()
            Log.e("foodflix",e.message.toString())
        }

        return inflatedView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater.inflate(R.menu.toolbar_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter_rating -> {
                Collections.sort(restaurantList,ratingComparator)
                restaurantList.reverse()
                rvRestaurants.adapter?.notifyDataSetChanged()
            }
            R.id.filter_cost_high_low -> {
                Collections.sort(restaurantList,priceHighLowComparator)
                restaurantList.reverse()
                rvRestaurants.adapter?.notifyDataSetChanged()
            }
            R.id.filter_cost_low_high -> {
                Collections.sort(restaurantList,priceLowHighComparator)
                rvRestaurants.adapter?.notifyDataSetChanged()
            }
            else -> {
                item.isChecked=false
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
