package com.cybershark.foodflix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cybershark.foodflix.R
import kotlinx.android.synthetic.main.faq_item.view.*

class FaqAdapter(val context: Context,private val menuList:ArrayList<Pair<String,String>>):RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    inner class FaqViewHolder(menuItem: View):RecyclerView.ViewHolder(menuItem){
        val tvFaqQuestion:TextView=menuItem.tvFaqQuestion
        val tvFaqAnswer:TextView=menuItem.tvFaqAnswer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        return FaqViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.faq_item,parent,false))
    }

    override fun getItemCount(): Int {
       return menuList.size
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
       holder.tvFaqAnswer.text=menuList[position].second
       holder.tvFaqQuestion.text=menuList[position].first
    }
}