package com.suitcore.data.model.deserelializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.suitcore.data.model.User
import java.lang.reflect.Type

/**
 * Created by DODYDMW19 on 1/30/2018.
 */
class UserDeserializer : SuitCoreJsonDeserializer<User>() {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): User {
        val jsonObject = json?.asJsonObject

        val user: User? = User()
        if (jsonObject?.has("id")!!) {
            user?.id = getIntOrZeroFromJson(jsonObject.get("id"))
        }

        if (jsonObject.has("first_name")) {
            user?.firstName = getStringOrNullFromJson(jsonObject.get("first_name"))
        }

        if (jsonObject.has("last_name")) {
            user?.lastName = getStringOrNullFromJson(jsonObject.get("last_name"))
        }

        if (jsonObject.has("avatar")) {
            user?.avatar = getStringOrNullFromJson(jsonObject.get("avatar"))
        }

        return user!!

    }
}