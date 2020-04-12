package com.cybershark.foodflix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cybershark.foodflix.R
import com.cybershark.foodflix.dataclasses.FoodItemDetails

class OrderHistoryChildAdapter(private val context: Context,private val items: List<FoodItemDetails>) : RecyclerView.Adapter<OrderHistoryChildAdapter.OrderHistoryChildViewHolder>() {

    inner class OrderHistoryChildViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val tvCartItemName= itemView.findViewById<TextView>(R.id.tvCartItemName)!!
        val tvCartItemPrice= itemView.findViewById<TextView>(R.id.tvCartItemPrice)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryChildViewHolder {
       return OrderHistoryChildViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: OrderHistoryChildViewHolder, position: Int) {
       holder.tvCartItemName.text=items[position].name
       holder.tvCartItemPrice.text=("Rs. ${items[position].cost}")
    }

}
