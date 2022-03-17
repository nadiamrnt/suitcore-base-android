package com.suitcore.data.model

import com.google.gson.annotations.SerializedName


/**
 * Created by dodydmw19 on 2/11/19.
 */

class WrapperError : RuntimeException() {

    @SerializedName("status_code")
    private var statusCode: Long? = null

    @SerializedName("message")
    private var messages: String? = null

    fun WrapperError(statusCode: Long?, message: String) {
        this.statusCode = statusCode
        this.messages = message
    }

    fun WrapperError(statusCode: Long?) {
        this.statusCode = statusCode
    }


    fun getStatusCode(): Long? {
        return statusCode
    }

    fun setStatusCode(statusCode: Long?) {
        this.statusCode = statusCode
    }

    fun getMessages() : String? {
        return messages
    }

    fun setMessage(message: String) {
        this.messages = message
    }

}