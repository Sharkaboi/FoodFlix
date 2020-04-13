package com.cybershark.foodflix.sqllite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class ResMenuEntity (
    @PrimaryKey
    val id:String,
    @ColumnInfo(name="name")
    val name:String,
    @ColumnInfo(name="cost")
    val cost :Int
)