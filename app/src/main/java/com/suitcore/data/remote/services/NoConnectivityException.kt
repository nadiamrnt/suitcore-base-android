package com.suitcore.data.remote.services

import java.io.IOException

/**
 * Created by @dodydmw19 on 07, July, 2020
 */
class NoConnectivityException : IOException() {
    // You can send any message whatever you want from here.
    override val message: String
        get() = "Koneksi Anda terputus"
    // You can send any message whatever you want from here.
}