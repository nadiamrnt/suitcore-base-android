package com.suitcore.data.remote.services

import android.util.Log
import com.suitcore.BaseApplication
import com.suitcore.helper.CommonUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by @dodydmw19 on 09, June, 2020
 */

class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 401) {
            Log.d("invalid_auth", response.code.toString())
            CommonUtils.clearLocalStorage()
            CommonUtils.gotoLoginScreen(BaseApplication.currentActivity())
        }

        return response
    }
}