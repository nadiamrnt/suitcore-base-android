package com.suitcore.data.model

import android.content.Context
import com.google.gson.JsonSyntaxException
import com.suitcore.R
import com.suitcore.helper.AppStatus
import retrofit2.HttpException
import javax.net.ssl.HttpsURLConnection

/**
 * Created by dodydmw19 on 2/12/19.
 */

class ErrorCodeHelper {

    companion object {
        fun getErrorMessage(context: Context?, e: Any): String? {
            var errorMessage = ""
            context?.let {
                if (AppStatus.checkConnectivity(context) != AppStatus.NONE) {
                    when (e) {
                        is Throwable -> {
                            errorMessage = when (e) {
                                is HttpException -> when (e.code()) {
                                    HttpsURLConnection.HTTP_UNAUTHORIZED -> context.getString(R.string.txt_error_unauthorized)
                                    HttpsURLConnection.HTTP_FORBIDDEN -> context.getString(R.string.txt_error_forbidden)
                                    HttpsURLConnection.HTTP_INTERNAL_ERROR -> context.getString(R.string.txt_error_internal_error)
                                    HttpsURLConnection.HTTP_BAD_REQUEST -> context.getString(R.string.txt_error_bad_request)
                                    else -> e.localizedMessage
                                }
                                is WrapperError -> e.getMessages().toString()
                                is JsonSyntaxException -> context.getString(R.string.txt_error_custom)
                                else -> e.message.toString()
                            }
                        }
                        is Int -> {
                            errorMessage = it.getString(e)
                        }
                    }
                } else {
                    errorMessage = context.getString(R.string.txt_error_no_internet_connection)
                }
            } ?:
            run { errorMessage = "" }
            return errorMessage
        }
    }

}