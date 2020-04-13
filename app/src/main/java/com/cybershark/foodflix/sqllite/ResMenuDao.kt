package com.cybershark.foodflix.sqllite

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResMenuDao {

    @Insert
    fun insertItemToCart(resMenuEntity: ResMenuEntity)

    @Delete
    fun removeItemFromCart(resMenuEntity: ResMenuEntity)

    @Query("SELECT * FROM cart")
    fun getCartContents():List<ResMenuEntity>

    @Query("SELECT SUM(cost) FROM cart")
    fun getCartTotal():Int

    @Query("SELECT COUNT(id) FROM cart")
    fun getCartItemCount():Int

    @Query("DELETE FROM cart")
    fun deleteAllCartItems()

    @Query("SELECT * FROM cart WHERE id=:resId")
    fun checkIfAddedToCart(resId:String):ResMenuEntity
}