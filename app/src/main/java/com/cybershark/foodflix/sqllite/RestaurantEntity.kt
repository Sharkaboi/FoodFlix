package com.cybershark.foodflix.sqllite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey val id:Int,
    @ColumnInfo(name = "res_name") val name:String,
    @ColumnInfo(name = "res_rating") val rating:Float,
    @ColumnInfo(name = "res_price") val price:Int,
    @ColumnInfo(name = "res_image") val image:String,
    @ColumnInfo(name = "res_fav") val fav:Boolean
)