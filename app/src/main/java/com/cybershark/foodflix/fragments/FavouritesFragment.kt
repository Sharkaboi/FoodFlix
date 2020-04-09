package com.cybershark.foodflix.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybershark.foodflix.R
import com.cybershark.foodflix.adapters.RestaurantsAdapter
import com.cybershark.foodflix.sqllite.DBAsyncTask


class FavouritesFragment : Fragment() {

    private lateinit var inflatedView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        inflatedView= inflater.inflate(R.layout.fragment_favourites, container, false)
        val rvFavourites=inflatedView.findViewById<RecyclerView>(R.id.rvFavourites)
        rvFavourites.layoutManager= LinearLayoutManager(activity!!.applicationContext)
        //val dataList=DBAsyncTask(activity!!.applicationContext,null,4)
        //rvFavourites.adapter= RestaurantsAdapter(activity!!.applicationContext,dataList)
        return inflatedView
    }

}
