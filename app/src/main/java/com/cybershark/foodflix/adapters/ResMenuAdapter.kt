package com.cybershark.foodflix.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cybershark.foodflix.R
import com.cybershark.foodflix.dataclasses.MenuItemDetails
import com.cybershark.foodflix.sqllite.CartAsyncTask
import com.cybershark.foodflix.sqllite.ResMenuEntity

class ResMenuAdapter(private val context: Context,private val menuList: MutableList<MenuItemDetails>) : RecyclerView.Adapter<ResMenuAdapter.ResMenuViewHolder>() {

    inner class ResMenuViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvMenuItemId=itemView.findViewById<TextView>(R.id.tvMenuItemId)!!
        val tvMenuItemName=itemView.findViewById<TextView>(R.id.tvMenuItemName)!!
        val tvMenuItemPrice=itemView.findViewById<TextView>(R.id.tvMenuItemPrice)!!
        val btnAddRemove=itemView.findViewById<Button>(R.id.btnAddRemove)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResMenuViewHolder {
        return ResMenuViewHolder(LayoutInflater.from(context).inflate(R.layout.menu_item,parent,false))
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: ResMenuViewHolder, position: Int) {
        holder.tvMenuItemId.text=("${position+1}.")
        holder.tvMenuItemName.text=menuList[position].name
        holder.tvMenuItemPrice.text=("Rs.${menuList[position].cost}")
        holder.btnAddRemove.text=context.getText(R.string.add)
        holder.btnAddRemove.setOnClickListener{
            if(CartAsyncTask(context,id=menuList[position].id,operationIndex = 3).execute().get()){
                //remove from cart
                val result=CartAsyncTask(context, ResMenuEntity(menuList[position].id,menuList[position].name,menuList[position].cost.toInt()),operationIndex = 2).execute().get()
                if(result){
                    holder.btnAddRemove.setBackgroundColor(Color.parseColor("#d32f2f"))
                    holder.btnAddRemove.setTextColor(Color.parseColor("#FFFFFF"))
                    holder.btnAddRemove.text = context.getString(R.string.add)
                }else{
                    Toast.makeText(context,"An Error Has Occurred!",Toast.LENGTH_SHORT).show()
                }
            }else{
                //add to cart
                val result=CartAsyncTask(context, ResMenuEntity(menuList[position].id,menuList[position].name,menuList[position].cost.toInt()),operationIndex = 1).execute().get()
                if(result){
                    holder.btnAddRemove.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    holder.btnAddRemove.setTextColor(Color.parseColor("#d32f2f"))
                    holder.btnAddRemove.text=context.getString(R.string.remove)
                }else{
                    Toast.makeText(context,"An Error Has Occurred!",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
