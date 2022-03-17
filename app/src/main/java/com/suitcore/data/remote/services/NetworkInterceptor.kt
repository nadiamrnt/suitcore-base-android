package com.suitcore.data.remote.services

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


/**
 * Created by @dodydmw19 on 15, June, 2020
 */

class NetworkInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + "SuitPreferences.instance()?.getString(DataConstant.KEY_USER_TOKEN).toString()").build()
        return chain.proceed(request)
    }
}