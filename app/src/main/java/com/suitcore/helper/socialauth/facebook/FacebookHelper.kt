package com.suitcore.helper.socialauth.facebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.CheckResult
import androidx.fragment.app.Fragment

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

import org.json.JSONException
import org.json.JSONObject

import java.util.Arrays

/**
 * Created by dodydmw19 on 12/14/18.
 */

class FacebookHelper(private val mListener: FacebookListener?,
                     private val mFieldString: String?) {
    /**
     * Get the [CallbackManager] for managing callbacks.
     *
     * @return [CallbackManager]
     */
    @get:CheckResult
    val callbackManager: CallbackManager

    init {
        //FacebookSdk.sdkInitialize(context.getApplicationContext());

        if (mListener == null)
            throw IllegalArgumentException("FacebookResponse listener cannot be null.")


        if (mFieldString == null) throw IllegalArgumentException("field string cannot be null.")
        callbackManager = CallbackManager.Factory.create()

        //get access token
        val mCallBack = object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {

                //get the user profile
                getUserProfile(loginResult)
            }

            override fun onCancel() {
                mListener.onFbSignInFail("Failed Login Facebook")
            }

            override fun onError(e: FacebookException) {
                mListener.onFbSignInFail("Failed Login Facebook")
            }
        }
        LoginManager.getInstance().registerCallback(callbackManager, mCallBack)
    }

    /**
     * Get user facebook profile.
     *
     * @param loginResult login result with user credentials.
     */
    private fun getUserProfile(loginResult: LoginResult) {
        // App code
        val request = GraphRequest.newMeRequest(
                loginResult.accessToken
        ) { _, response ->

            Log.e("response: ", response.toString() + "")
            try {
                mListener?.onFbSignInSuccess(loginResult.accessToken.toString(), parseResponse(response.jsonObject).facebookID)
            } catch (e: Exception) {
                e.printStackTrace()

                mListener?.onFbSignInFail("Failed Login Facebook")
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", mFieldString)
        request.parameters = parameters
        request.executeAsync()
    }

    /**
     * Parse the response received into [FacebookUser] object.
     *
     * @param object response received.
     * @return [FacebookUser] with required fields.
     * @throws JSONException
     */
    @Throws(JSONException::class)
    private fun parseResponse(`object`: JSONObject): FacebookUser {
        val user = FacebookUser()
        user.response = `object`

        if (`object`.has("id")) user.facebookID = `object`.getString("id")
        if (`object`.has("email")) user.email = `object`.getString("email")
        if (`object`.has("name")) user.name = `object`.getString("name")
        if (`object`.has("gender"))
            user.gender = `object`.getString("gender")
        if (`object`.has("about")) user.about = `object`.getString("about")
        if (`object`.has("bio")) user.bio = `object`.getString("bio")
        if (`object`.has("cover"))
            user.coverPicUrl = `object`.getJSONObject("cover").getString("source")
        if (`object`.has("picture"))
            user.profilePic = `object`.getJSONObject("picture").getJSONObject("data").getString("url")
        return user
    }

    /**
     * Perform facebook sign in.
     *
     *
     * NOTE: If you are signing from the fragment than you should call [.performSignIn].
     *
     *
     * This method should generally call when user clicks on "Sign in with Facebook" button.
     *
     * @param activity instance of the caller activity.
     */
    fun performSignIn(activity: Activity) {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "user_friends", "email"))
    }

    /**
     * Perform facebook login. This method should be called when you are signing in from
     * fragment.
     *
     *
     * This method should generally call when user clicks on "Sign in with Facebook" button.
     *
     * @param fragment caller fragment.
     */
    fun performSignIn(fragment: Fragment) {
        LoginManager.getInstance().logInWithReadPermissions(fragment, Arrays.asList("public_profile", "user_friends", "email"))
    }

    /**
     * This method handles onActivityResult callbacks from fragment or activity.
     *
     * @param requestCode request code received.
     * @param resultCode  result code received.
     * @param data        Data intent.
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun performSignOut() {
        LoginManager.getInstance().logOut()
    }
}
