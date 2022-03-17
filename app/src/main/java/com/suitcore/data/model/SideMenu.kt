package com.suitcore.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by dodydmw19 on 1/3/19.
 */


open class SideMenu : RealmObject() {

    @PrimaryKey
    @SerializedName("id")
    open var id: Int = 0
    @SerializedName("position")
    open var position: String? = null
    @SerializedName("position_order")
    open var positionOrder: Int = 0
    @SerializedName("label")
    open var label: String? = null
    @SerializedName("url")
    open var url: String? = null
    @SerializedName("status")
    open var status: Int = 0
    @SerializedName("created_at")
    open var createdAt: Date? = null
    @SerializedName("updated_at")
    open var updatedAt: Date? = null

}