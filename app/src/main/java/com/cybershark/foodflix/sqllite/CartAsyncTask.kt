package com.cybershark.foodflix.sqllite

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room

class CartAsyncTask(val context: Context,private val resMenuEntity: ResMenuEntity?=null,val id:String="",private val operationIndex:Int):AsyncTask<Void,Void,Boolean>() {

    private val sqlLiteCartDB=Room.databaseBuilder(context,CartDB::class.java,"cart-db").build()

    override fun doInBackground(vararg params: Void?): Boolean {
       when(operationIndex){
           //insert
           1->{
               sqlLiteCartDB.resMenuDao().insertItemToCart(resMenuEntity!!)
               sqlLiteCartDB.close()
               return true
           }
           //remove
           2->{
               sqlLiteCartDB.resMenuDao().removeItemFromCart(resMenuEntity!!)
               sqlLiteCartDB.close()
               return true
           }
           //check if in cart
           3->{
               val resMenuFromDB: ResMenuEntity? =sqlLiteCartDB.resMenuDao().checkIfAddedToCart(id)
               sqlLiteCartDB.close()
               return resMenuFromDB!=null
           }
           //delete all items
           4->{
               sqlLiteCartDB.resMenuDao().deleteAllCartItems()
               return true
           }
       }
     return false
    }
}