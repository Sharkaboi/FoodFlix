package com.cybershark.foodflix.sqllite

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room

class RetrieveFavAsyncTask(private val context: Context):AsyncTask<Void,Void,List<RestaurantEntity>>() {

    override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {

        val sqlLiteDB= Room.databaseBuilder(context,ResDB::class.java,"res-db").build()
        return sqlLiteDB.resDao().retrieveRestaurants()
    }
}