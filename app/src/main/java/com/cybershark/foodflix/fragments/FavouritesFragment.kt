package com.cybershark.foodflix.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybershark.foodflix.R
import com.cybershark.foodflix.adapters.FavResAdapter
import com.cybershark.foodflix.sqllite.RestaurantEntity
import com.cybershark.foodflix.sqllite.RetrieveFavAsyncTask


class FavouritesFragment : Fragment() {

    private lateinit var inflatedView:View
    private  lateinit var favList:MutableList<RestaurantEntity>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        inflatedView= inflater.inflate(R.layout.fragment_favourites, container, false)
        val rvFavourites=inflatedView.findViewById<RecyclerView>(R.id.rvFavourites)
        val listFromAsync=RetrieveFavAsyncTask(activity!!.applicationContext).execute().get()
        if(listFromAsync!=null && activity!=null){
            favList= mutableListOf()
            rvFavourites.layoutManager= LinearLayoutManager(activity as Context)
            listFromAsync.forEach {
                favList.add(RestaurantEntity(it.id, it.name, it.rating, it.price, it.image, it.fav))
            }
            if (listFromAsync.isEmpty()){
                inflatedView.findViewById<TextView>(R.id.tvNoFav).visibility=View.VISIBLE
                inflatedView.findViewById<ProgressBar>(R.id.contentLoadingFav).visibility = View.GONE
            }else {
                rvFavourites.adapter = FavResAdapter(activity as Context, favList)
                inflatedView.findViewById<ProgressBar>(R.id.contentLoadingFav).visibility = View.GONE
            }
        }
        return inflatedView
    }
}
