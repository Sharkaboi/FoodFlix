package com.cybershark.foodflix.util

import android.content.Context
import android.net.ConnectivityManager

class InternetConnectionManager {
    fun isNetworkAccessActive(context: Context):Boolean {
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
}