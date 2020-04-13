package com.cybershark.foodflix.sqllite

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room

class RetrieveCartItemsAsyncTask(val context: Context):AsyncTask<Void,Void,List<ResMenuEntity>>() {

    private val sqlLiteCartDB=Room.databaseBuilder(context,CartDB::class.java,"cart-db").build()

    override fun doInBackground(vararg params: Void?): List<ResMenuEntity> {
        val cartList=sqlLiteCartDB.resMenuDao().getCartContents()
        sqlLiteCartDB.close()
        return cartList
    }
}