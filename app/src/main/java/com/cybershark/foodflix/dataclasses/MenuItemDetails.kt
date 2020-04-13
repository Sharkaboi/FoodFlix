package com.cybershark.foodflix.dataclasses

import com.google.gson.annotations.SerializedName

data class MenuItemDetails(
    @SerializedName("id")
    val id:String,
    @SerializedName("name")
    val name:String,
    @SerializedName("cost_for_one")
    val cost:String,
    @SerializedName("restaurant_id")
    val resID:String
)