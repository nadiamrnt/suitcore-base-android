package com.suitcore.helper.socialauth.facebook

/**
 * Created by dodydmw19 on 7/16/18.
 */

interface FacebookListener {

    fun onFbSignInFail(errorMessage: String?)

    fun onFbSignInSuccess(authToken: String?, userId: String?)

}