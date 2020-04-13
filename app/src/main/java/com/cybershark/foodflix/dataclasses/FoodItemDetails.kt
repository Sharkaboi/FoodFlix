package com.cybershark.foodflix.dataclasses

import com.google.gson.annotations.SerializedName

data class FoodItemDetails(
    @SerializedName("food_item_id")
    val id:String,
    @SerializedName("name")
    val name:String,
    @SerializedName("cost")
    val cost:String
)