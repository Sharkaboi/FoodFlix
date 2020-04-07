package com.cybershark.foodflix.sqllite

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RestaurantEntity::class],version = 1)
abstract class ResDB:RoomDatabase() {

    abstract fun resDao():RestaurantDao
}