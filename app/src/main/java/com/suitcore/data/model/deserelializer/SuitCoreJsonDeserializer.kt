package com.suitcore.data.model.deserelializer

import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement

/**
 * Created by DODYDMW19 on 9/7/2017.
 */

abstract class SuitCoreJsonDeserializer<T> : JsonDeserializer<T> {

    fun getStringOrNullFromJson(json: JsonElement): String {
        try {
            return json.asString
        } catch (ignored: Exception) {
        }

        return ""
    }

    fun getIntOrZeroFromJson(json: JsonElement): Int {
        try {
            return json.asInt
        } catch (ignored: Exception) {
        }

        return 0
    }

    fun getLongOrZeroFromJson(json: JsonElement): Long {
        try {
            return json.asLong
        } catch (ignored: Exception) {
        }

        return 0
    }

    fun getDoubleOrZeroFromJson(json: JsonElement): Double {
        try {
            return json.asDouble
        } catch (ignored: Exception) {
        }

        return 0.0
    }

    fun getBooleanOrFalseFromJson(json: JsonElement): Boolean {
        try {
            return json.asBoolean
        } catch (ignored: Exception) {
        }

        return false
    }
}