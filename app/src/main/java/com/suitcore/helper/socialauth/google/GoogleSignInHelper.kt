package com.suitcore.helper.socialauth.google

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by dodydmw19 on 12/14/18.
 */

class GoogleSignInHelper(
    private val mContext: FragmentActivity?,
    private val clientId: String,
    private val mListener: GoogleListener?
) {

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mAuth: FirebaseAuth? = null

    init {
        if (mListener == null) {
            throw RuntimeException("GoogleAuthResponse listener cannot be null.")
        }

        buildSignInOptions()

        mAuth = FirebaseAuth.getInstance()
    }

    private fun buildSignInOptions() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestProfile()
            .requestEmail()
            .build()
        ///if (serverClientId != null) gso.requestIdToken(serverClientId)
        if (mContext != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso)
        }
    }

    fun performSignIn(activity: Activity) {
        val intent: Intent? = mGoogleSignInClient?.signInIntent
        activity.startActivityForResult(intent, RC_SIGN_IN)
    }

    fun performSignIn(activity: Fragment) {
        val intent: Intent? = mGoogleSignInClient?.signInIntent
        activity.startActivityForResult(intent, RC_SIGN_IN)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            mListener?.onGoogleAuthSignIn(
                account?.let { parseToGoogleUser(it).idToken },
                account?.let { parseToGoogleUser(it).id }
            )

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            mListener?.onGoogleAuthSignInFailed("signInResult:failed code=" + e.statusCode)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun asyncGoogle(account: GoogleSignInAccount?) {
        if (mContext != null && account != null) {
            Observable.create(ObservableOnSubscribe<String> { emitter ->
                try {
                    val scope = "oauth2:" + Scopes.EMAIL + " " + Scopes.PROFILE
                    val token =
                        account.account?.let {
                            GoogleAuthUtil.getToken(mContext,
                                it, scope, Bundle())
                        }
                    //send token to server
                    mListener?.onGoogleAuthSignIn(token.toString(), account.id)
                } catch (e: Exception) {
                    emitter.onError(e) // In case there are network errors
                }
            })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }
    }

    private fun parseToGoogleUser(account: GoogleSignInAccount): GoogleAuthUser {
        val user = GoogleAuthUser()
        user.name = account.displayName
        user.familyName = account.familyName
        user.idToken = account.idToken
        user.email = account.email
        user.photoUrl = account.photoUrl
        return user
    }

    fun performSignOut() {
        if (mAuth != null && mContext != null) {
            mAuth?.signOut()
            mGoogleSignInClient?.signOut()?.addOnCompleteListener(mContext) { }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 212
    }
}
