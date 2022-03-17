package com.suitcore.data.remote.services

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.suitcore.BuildConfig
import com.suitcore.data.local.prefs.DataConstant
import com.suitcore.data.local.prefs.SuitPreferences
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext


object BaseServiceFactory {

    fun getAPIService(): APIService {
        return provideRetrofit(makeGSON())
    }

    private fun provideRetrofit(gSon: Gson): APIService {

        var url: String
        val currentUrl: String? = SuitPreferences.instance()?.getString(DataConstant.BASE_URL)

        if (currentUrl != null && currentUrl.isNotEmpty()) {
            currentUrl.let { url = currentUrl }
        } else {
            url = BuildConfig.BASE_URL
        }

        val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gSon))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofit.create(APIService::class.java)
    }

    private fun provideOkHttpClient(): OkHttpClient {

        val httpClient = OkHttpClient().newBuilder()
            .apply {
            readTimeout(60, TimeUnit.SECONDS)
            connectTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
        }

        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(LoggingInterceptor.getLoggingInterceptor())
        }

        httpClient.addNetworkInterceptor(NetworkInterceptor())
        httpClient.addInterceptor(ErrorInterceptor())
        //httpClient.addInterceptor(NetworkConnectionInterceptor(BaseApplication.appContext))

        return httpClient.build()
    }

    private fun makeGSON(): Gson {
        return GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                //.registerTypeAdapter(Home::class.java, HomeDeserializer())
                .create()
    }

}