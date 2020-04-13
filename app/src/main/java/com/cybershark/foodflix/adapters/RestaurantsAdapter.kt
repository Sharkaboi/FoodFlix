package com.cybershark.foodflix.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cybershark.foodflix.R
import com.cybershark.foodflix.activities.ResMenuActivity
import com.cybershark.foodflix.dataclasses.RestaurantDataClass
import com.cybershark.foodflix.sqllite.DBAsyncTask
import com.cybershark.foodflix.sqllite.RestaurantEntity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.restaurant_item.view.*
import java.util.*
import kotlin.collections.ArrayList


class RestaurantsAdapter(private val context:Context,private val tempItemList:MutableList<RestaurantDataClass>): RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder>() {

    private val tempItemListCopy=tempItemList

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
            Glide.with(context).load(R.drawable.ic_favorite_selected).into(holder.ivFav)
        else
            Glide.with(context).load(R.drawable.ic_favorite_unselected).into(holder.ivFav)
        Glide.with(context)
            .load(tempItemList[position].image)
            .error(R.drawable.ic_no_wifi)
            .transform(CenterCrop(),RoundedCorners(12))
            .into(holder.ivResPic)
        holder.tvRating.text=tempItemList[position].rating.toString().trim()
        holder.tvResCost.text=("₹ "+tempItemList[position].price+" per person.")
        holder.tvResName.text=tempItemList[position].name.trim()
        ///TODO:add listener
        holder.itemView.setOnClickListener{
            val intent=Intent(context,ResMenuActivity::class.java)
            intent.putExtra("res",Gson().toJson(tempItemList[position]))
            context.startActivity(intent)
        }
        holder.ivFav.setOnClickListener {
            Log.e("foodflix","fav onclick")
            if (tempItemList[position].fav){
                holder.ivFav.setImageResource(R.drawable.ic_favorite_unselected)
                //TODO:remove from db
                val result=DBAsyncTask(
                    context.applicationContext,
                    RestaurantEntity(
                        tempItemList[position].id,
                        tempItemList[position].name,
                        tempItemList[position].rating,
                        tempItemList[position].price,
                        tempItemList[position].image,
                        true
                        ),
                    3
                ).execute().get()
                tempItemList[position].fav=false
                if(result)
                    Toast.makeText(context,"Removed ${tempItemList[position].name} to Favourites!",Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(context, "An Error has occurred!\nTry again later", Toast.LENGTH_SHORT).show()
                    holder.ivFav.setImageResource(R.drawable.ic_favorite_selected)
                }
            }else{
                holder.ivFav.setImageResource(R.drawable.ic_favorite_selected)
                //TODO:add to db
                val result=DBAsyncTask(
                    context.applicationContext,
                    RestaurantEntity(
                        tempItemList[position].id,
                        tempItemList[position].name,
                        tempItemList[position].rating,
                        tempItemList[position].price,
                        tempItemList[position].image,
                        true
                    ),
                    2
                ).execute().get()
                tempItemList[position].fav=true
                if(result)
                    Toast.makeText(context,"Added ${tempItemList[position].name} to Favourites!",Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(context, "An Error has occurred!\nTry again later", Toast.LENGTH_SHORT).show()
                    holder.ivFav.setImageResource(R.drawable.ic_favorite_unselected)
                }
            }
        }
    }
/*
    fun getFilter(): Filter? {
        return searchFilter
    }

    private val searchFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<RestaurantDataClass> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(tempItemListCopy)
            } else {
                val filterPattern = constraint.toString().trim().toLowerCase(Locale.ROOT)
                for (item in tempItemListCopy) {
                    if (item.name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            tempItemList.clear()
            tempItemList.addAll(results.values as List<RestaurantDataClass>)
            notifyDataSetChanged()
        }
    }

 */
}