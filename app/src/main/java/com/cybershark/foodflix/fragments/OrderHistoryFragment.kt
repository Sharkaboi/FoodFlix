package com.cybershark.foodflix.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.cybershark.foodflix.R
import com.cybershark.foodflix.adapters.OrderHistoryParentAdapter
import com.cybershark.foodflix.dataclasses.OrderDetails
import com.cybershark.foodflix.util.InternetConnectionManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_order_history.*

class OrderHistoryFragment : Fragment() {

    private lateinit var inflatedView: View
    private var spFileName="foodFlixFile"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        inflatedView= inflater.inflate(R.layout.fragment_order_history, container, false)

        val sharedPreferences=activity?.getSharedPreferences(spFileName,Context.MODE_PRIVATE)
        val userID=sharedPreferences!!.getString("id",null)
        val rvPreviousOrders=inflatedView.findViewById<RecyclerView>(R.id.rvPreviousOrders)
        val ordersListFromAPI= mutableListOf<OrderDetails>()
        val tvNoOrders=inflatedView.findViewById<TextView>(R.id.tvNoOrders)
        val tvPreviousOrders=inflatedView.findViewById<TextView>(R.id.tvPreviousOrders)
        val divider5=inflatedView.findViewById<View>(R.id.divider5)
        val contentLoading=inflatedView.findViewById<ProgressBar>(R.id.contentLoading)

        if (activity!=null) {
            if (InternetConnectionManager().isNetworkAccessActive(activity as Context)) {
                rvPreviousOrders.layoutManager = LinearLayoutManager(activity as Context)
                val queue = Volley.newRequestQueue(activity as Context)
                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.GET,
                    "http://13.235.250.119/v2/orders/fetch_result/$userID",
                    null,
                    Response.Listener {
                        val data = it.getJSONObject("data")
                        if (data.getBoolean("success")) {
                            val resultJSONArray = data.getJSONArray("data")
                            if (resultJSONArray.length() == 0) {
                                tvNoOrders.visibility = View.VISIBLE
                            } else {
                                tvPreviousOrders.visibility = View.VISIBLE
                                divider5.visibility = View.VISIBLE
                                for (i in 0 until resultJSONArray.length()) {
                                    val resultJSONObject = resultJSONArray.getJSONObject(i)!!
                                    ordersListFromAPI.add(
                                        Gson().fromJson<OrderDetails>(
                                            resultJSONObject.toString(),
                                            OrderDetails::class.java
                                        )
                                    )
                                }
                                rvPreviousOrders.adapter = OrderHistoryParentAdapter(
                                    activity as Context,
                                    ordersListFromAPI
                                )
                                rvPreviousOrders.adapter!!.notifyDataSetChanged()
                            }
                            contentLoading.visibility = View.GONE
                        }
                    },
                    Response.ErrorListener {
                        contentLoading.visibility = View.GONE
                        Toast.makeText(activity as Context, "Error receiving Data to the server!", Toast.LENGTH_SHORT).show()
                        Log.e("foodflix", it.message.toString())
                    }
                ) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = getString(R.string.token)
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)
            } else {
                if (activity != null)
                    AlertDialog.Builder(activity as Context)
                        .setIcon(R.drawable.ic_no_wifi)
                        .setTitle("No Internet")
                        .setMessage("Internet Access has been Restricted.")
                        .setPositiveButton("Retry") { dialog, which ->
                            if (activity != null)
                                if (InternetConnectionManager().isNetworkAccessActive(activity as Context)) {
                                    dialog.dismiss()
                                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,OrderHistoryFragment()).commit()
                                } else {
                                    if (activity != null)
                                        Toast.makeText(
                                            activity as Context,
                                            "Still Disconnected :(",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                }
                        }
                        .setNegativeButton("Open Settings") { dialog, which ->
                            startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                        }
                        .setCancelable(false)
                        .show()
            }
        }
        return inflatedView
    }

}