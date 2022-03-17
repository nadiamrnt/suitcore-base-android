package com.suitcore.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by @dodydmw19 on 06, October, 2021
 */
open class Place{

    @SerializedName("id")
    var placeId: String? = ""

    @SerializedName("place_name")
    var placeName: String? = ""

    @SerializedName("center")
    var location: List<Double>? = listOf()

}