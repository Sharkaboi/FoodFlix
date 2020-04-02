package com.cybershark.foodflix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cybershark.foodflix.R
import com.cybershark.foodflix.dataclasses.RestaurantDataClass
import kotlinx.android.synthetic.main.restaurant_item.view.*

class RestaurantsAdapter(private val context:Context,private val tempItemList:MutableList<RestaurantDataClass>): RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder>() {

    inner class  RestaurantsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val ivResPic: ImageView=itemView.ivResPic
            val tvResName:TextView=itemView.tvResName
            val tvResCost:TextView=itemView.tvResCost
            val tvRating:TextView=itemView.tvRating
            val ivFav: ImageView =itemView.ivFav
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsViewHolder {
        return RestaurantsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item,parent,false))
    }

    override fun getItemCount(): Int {
        return tempItemList.size
    }

    override fun onBindViewHolder(holder: RestaurantsViewHolder, position: Int) {
        if (tempItemList[position].fav)
            holder.ivFav.setImageResource(R.drawable.ic_favorite_selected)
        Glide.with(context).load(tempItemList[position].image).into(holder.ivResPic)
        holder.tvRating.text=tempItemList[position].rating.toString().trim()
        holder.tvResCost.text=("Rs."+tempItemList[position].price+" per person.")
        holder.tvResName.text=tempItemList[position].name.trim()
    }
}