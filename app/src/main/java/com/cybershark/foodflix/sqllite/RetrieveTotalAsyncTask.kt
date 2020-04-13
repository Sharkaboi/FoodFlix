package com.cybershark.foodflix.sqllite

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room

class RetrieveTotalOrCountAsyncTask(val context: Context,private val operationIndex:Int):AsyncTask<Void,Void,Int>() {

    private val sqlLiteCartDb=Room.databaseBuilder(context,CartDB::class.java,"cart-db").build()

    override fun doInBackground(vararg params: Void?): Int {
        when(operationIndex) {
            //return total
            1-> {
                val total = sqlLiteCartDb.resMenuDao().getCartTotal()
                sqlLiteCartDb.close()
                return total
            }
            //return count of cart
            2->{
                val count=sqlLiteCartDb.resMenuDao().getCartItemCount()
                sqlLiteCartDb.close()
                return count
            }
            else->{
                return 0
            }
        }
    }
}