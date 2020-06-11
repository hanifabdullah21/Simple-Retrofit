package org.sandec.hanippund.easyretrofit.util

import android.content.Context
import android.net.ConnectivityManager

val TAG_RETROFIT = "SIMPLE-RETROFIT"

/**
 * This function is to check the connection
 *
 * @return Boolean, if true so device has internet connection
 * */
fun Context.checkConnextion(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = connectivityManager.activeNetworkInfo
    return (netInfo != null && netInfo.isConnected)
}