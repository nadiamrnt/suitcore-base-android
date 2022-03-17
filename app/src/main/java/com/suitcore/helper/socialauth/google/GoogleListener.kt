package com.suitcore.helper.socialauth.google

/**
 * Created by dodydmw19 on 7/16/18.
 */

interface GoogleListener {

    fun onGoogleAuthSignIn(authToken: String?, userId: String?)

    fun onGoogleAuthSignInFailed(errorMessage: String?)

}