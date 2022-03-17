package com.suitcore.data.remote.services

import com.suitcore.data.model.Place
import com.suitcore.data.model.User
import com.suitcore.data.remote.wrapper.MapBoxResults
import com.suitcore.data.remote.wrapper.Results
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by DODYDMW19 on 8/3/2017.
 */

interface APIService {

    @GET("users")
    fun getMembers(
            @Query("per_page") perPage: Int,
            @Query("page") page: Int): Single<Results<User>>

    @GET("users")
    fun getMembersCoroutinesAsync(
            @Query("per_page") perPage: Int,
            @Query("page") page: Int): Deferred<Results<User>>

    @GET
    fun searchPlaceAsync(@Url url: String?): Deferred<MapBoxResults<Place>>
}
