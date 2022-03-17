package com.suitcore.data.remote.wrapper

import com.google.gson.annotations.SerializedName

/**
 * Created by dodydmw19 on 1/14/19.
 */

class MapBoxResults<T> {


    @SerializedName("features")
    var arrayData: List<T>? = null
}