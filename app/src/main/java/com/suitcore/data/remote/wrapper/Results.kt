package com.suitcore.data.remote.wrapper

import com.google.gson.annotations.SerializedName

/**
 * Created by DODYDMW19 on 1/30/2018.
 */

class Results<T> {

    @SerializedName("page")
    var page: Int? = 0

    @SerializedName("per_page")
    var perPage: Int? = 0

    @SerializedName("total")
    var total: Int? = 0

    @SerializedName("total_pages")
    var totalPages: Int? = 0

    @SerializedName("data")
    var arrayData: List<T>? = null
}
