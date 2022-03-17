package com.suitcore.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * Created by DODYDMW19 on 6/6/2016.
 */

enum class AppStatus {
    NONE,
    WIFI,
    MOBILE,
    OTHER,
    MAYBE;

    companion object {

        fun checkConnectivity(context: Context): AppStatus {

            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            cm.run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getNetworkCapabilities(activeNetwork)?.run {
                        return when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> WIFI
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> MOBILE
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> OTHER
                            hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> MAYBE
                            else -> NONE
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    activeNetworkInfo?.run {
                        return when (type) {
                            ConnectivityManager.TYPE_WIFI -> WIFI
                            ConnectivityManager.TYPE_MOBILE -> MOBILE
                            ConnectivityManager.TYPE_ETHERNET -> OTHER
                            ConnectivityManager.TYPE_BLUETOOTH -> MAYBE
                            else -> NONE
                        }
                    }
                }
            }
            return NONE
        }

    }

}
