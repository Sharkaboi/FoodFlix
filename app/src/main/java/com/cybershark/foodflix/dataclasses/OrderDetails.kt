package com.cybershark.foodflix.dataclasses

import com.google.gson.annotations.SerializedName

data class OrderDetails(
    @SerializedName("order_id")
    val id:String,
    @SerializedName("restaurant_name")
    val resName:String,
    @SerializedName("total_cost")
    val total_cost:String,
    @SerializedName("order_placed_at")
    val orderDate:String,
    @SerializedName("food_items")
    val items:List<FoodItemDetails>
)