package com.cybershark.foodflix.sqllite

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {

    @Insert
    fun insertRestaurant(resEntity:RestaurantEntity)

    @Delete
    fun deleteRestaurant(resEntity: RestaurantEntity)

    @Query("SELECT * FROM restaurants")
    fun retrieveRestaurants():List<RestaurantEntity>

    @Query("SELECT * FROM restaurants WHERE id=:resId")
    fun getResById(resId:Int):RestaurantEntity
}