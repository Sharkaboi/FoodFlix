package com.cybershark.foodflix.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybershark.foodflix.R
import com.cybershark.foodflix.adapters.RestaurantsAdapter
import com.cybershark.foodflix.dataclasses.RestaurantDataClass
import com.cybershark.foodflix.sqllite.RetrieveFavAsyncTask


class FavouritesFragment : Fragment() {

    private lateinit var inflatedView:View
    private  lateinit var favList:ArrayList<RestaurantDataClass>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        inflatedView= inflater.inflate(R.layout.fragment_favourites, container, false)
        val rvFavourites=inflatedView.findViewById<RecyclerView>(R.id.rvFavourites)
        val listFromAync=RetrieveFavAsyncTask(activity!!.applicationContext).execute().get()
        if(listFromAync!=null && activity!=null){
            favList=ArrayList()
            rvFavourites.layoutManager= LinearLayoutManager(activity!!.applicationContext)
            listFromAync.forEach {
                favList.add(RestaurantDataClass(it.id, it.name, it.rating, it.price, it.image, it.fav))
            }
            rvFavourites.adapter= RestaurantsAdapter(activity!!.applicationContext,favList)
            inflatedView.findViewById<ProgressBar>(R.id.contentLoadingFav).visibility=View.GONE
        }
        return inflatedView
    }

}
