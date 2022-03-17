package com.suitcore.data.model

import com.google.gson.annotations.SerializedName


/**
 * Created by @dodydmw19 on 05, May, 2021
 * Update Type :
 *  InAppUpdate - (flexible/immediate)(default)
    RemoteConfig - (flexible/immediate)
 */

open class UpdateType {

    @SerializedName("update_type")
    var updateType: String? = ""
    @SerializedName("category")
    var category: String? = ""
    @SerializedName("latest_vcode")
    var latestVersionCode: Int? = 0
    @SerializedName("messages")
    var messages: String? = ""

}