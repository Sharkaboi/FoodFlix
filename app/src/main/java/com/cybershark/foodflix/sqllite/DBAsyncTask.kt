package com.cybershark.foodflix.sqllite

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room

class DBAsyncTask(val context: Context, private val restaurantEntity:RestaurantEntity, private val operationIndex:Int): AsyncTask<Void, Void, Boolean>() {

    val sqlLiteDB= Room.databaseBuilder(context,ResDB::class.java,"res-db").build()

    override fun doInBackground(vararg params: Void?): Boolean {
        when(operationIndex){
            1->{
                val resFromDB: RestaurantEntity? =sqlLiteDB.resDao().getResById(restaurantEntity.id)
                sqlLiteDB.close()
                return resFromDB != null
            }//check
            2->{
                sqlLiteDB.resDao().insertRestaurant(restaurantEntity)
                sqlLiteDB.close()
                return true
            }//add
            3->{
                sqlLiteDB.resDao().deleteRestaurant(restaurantEntity)
                sqlLiteDB.close()
                return true
            }//remove
        }
        return false
    }
}