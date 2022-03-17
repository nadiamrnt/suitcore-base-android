package com.suitcore.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by DODYDMW19 on 1/30/2018.
 */

open class User : RealmObject(){

    @PrimaryKey
    @SerializedName("id")
    var id: Int? = 0

    @SerializedName("first_name")
    var firstName: String? = ""

    @SerializedName("last_name")
    var lastName: String? = ""

    @SerializedName("avatar")
    var avatar: String? = ""


}