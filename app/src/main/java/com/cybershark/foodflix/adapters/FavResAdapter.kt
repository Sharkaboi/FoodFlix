package com.cybershark.foodflix.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cybershark.foodflix.R
import com.cybershark.foodflix.sqllite.DBAsyncTask
import com.cybershark.foodflix.sqllite.RestaurantEntity
import kotlinx.android.synthetic.main.restaurant_item.view.*

class FavResAdapter(private val context:Context,private val tempItemList:MutableList<RestaurantEntity>): RecyclerView.Adapter<FavResAdapter.FavResViewHolder>() {

    inner class  FavResViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivResPic: ImageView=itemView.ivResPic
        val tvResName:TextView=itemView.tvResName
        val tvResCost:TextView=itemView.tvResCost
        val tvRating:TextView=itemView.tvRating
        val ivFav: ImageView =itemView.ivFav
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavResViewHolder {
        return FavResViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item,parent,false))
    }

    override fun getItemCount(): Int {
        return tempItemList.size
    }

    override fun onBindViewHolder(holder: FavResViewHolder, position: Int) {
        //if (tempItemList[position].fav)
            holder.ivFav.setImageResource(R.drawable.ic_favorite_selected)
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
            Log.e("foodflix","item onclick")
            Toast.makeText(context,"${tempItemList[position].name} clicked!",Toast.LENGTH_SHORT).show()
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
                notifyDataSetChanged()
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
}