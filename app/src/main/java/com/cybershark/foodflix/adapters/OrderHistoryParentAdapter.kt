package com.cybershark.foodflix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybershark.foodflix.R
import com.cybershark.foodflix.dataclasses.OrderDetails

class OrderHistoryParentAdapter(private val context: Context,private val parentListOfOrders:List<OrderDetails>):RecyclerView.Adapter<OrderHistoryParentAdapter.OrderHistoryViewHolder>(){
    inner class OrderHistoryViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val rvOrderHisRes= itemView.findViewById<RecyclerView>(R.id.rvOrderHisRes)!!
        val tvOrderHisResName= itemView.findViewById<TextView>(R.id.tvOrderHisResName)!!
        val tvOrderDate= itemView.findViewById<TextView>(R.id.tvOrderDate)!!
        val tvOrderTotal= itemView.findViewById<TextView>(R.id.tvOrderTotal)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
       return OrderHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.order_history_item,parent,false))
    }

    override fun getItemCount(): Int {
        return parentListOfOrders.size
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        holder.tvOrderHisResName.text=parentListOfOrders[position].resName
        holder.tvOrderDate.text=parentListOfOrders[position].orderDate.subSequence(0,8)
        holder.tvOrderTotal.text=("Total : Rs.${parentListOfOrders[position].total_cost}")
        holder.rvOrderHisRes.layoutManager=LinearLayoutManager(context)
        holder.rvOrderHisRes.adapter=OrderHistoryChildAdapter(context,parentListOfOrders[position].items)
        holder.rvOrderHisRes.adapter!!.notifyDataSetChanged()
    }

}