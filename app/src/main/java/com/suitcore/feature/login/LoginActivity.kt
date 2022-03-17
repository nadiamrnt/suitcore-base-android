package com.suitcore.feature.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.install.model.ActivityResult
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.data.model.UpdateType
import com.suitcore.databinding.ActivityLoginBinding
import com.suitcore.feature.sidemenu.SideMenuActivity
import com.suitcore.feature.tabmenu.TabMenuActivity
import com.suitcore.firebase.analytics.FireBaseConstant
import com.suitcore.firebase.analytics.FireBaseHelper
import com.suitcore.firebase.remoteconfig.RemoteConfigHelper
import com.suitcore.firebase.remoteconfig.RemoteConfigPresenter
import com.suitcore.firebase.remoteconfig.RemoteConfigView
import com.suitcore.helper.CommonConstant
import com.suitcore.helper.CommonUtils
import com.suitcore.helper.inappupdates.InAppUpdateManager
import com.suitcore.helper.inappupdates.InAppUpdateStatus
import com.suitcore.helper.permission.SuitPermissions
import com.suitcore.helper.socialauth.facebook.FacebookHelper
import com.suitcore.helper.socialauth.facebook.FacebookListener
import com.suitcore.helper.socialauth.google.GoogleListener
import com.suitcore.helper.socialauth.google.GoogleSignInHelper
import timber.log.Timber

/**
 * Created by dodydmw19 on 7/18/18.
 */

class LoginActivity : BaseActivity<ActivityLoginBinding>(), LoginView, RemoteConfigView,
        GoogleListener, FacebookListener, InAppUpdateManager.InAppUpdateHandler {

    private var loginPresenter: LoginPresenter? = null
    private var remoteConfigPresenter: RemoteConfigPresenter? = null
    private var inAppUpdateManager: InAppUpdateManager? = null

    private var mGoogleHelper: GoogleSignInHelper? = null
    private var mFbHelper: FacebookHelper? = null

    override fun getViewBinding(): ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupPresenter()
        setupSocialLogin()
        actionClicked()
        needPermissions()
        CommonUtils.getIMEIDeviceId(this)
    }

    override fun onResume() {
        super.onResume()
        sendAnalytics()
        //remoteConfigPresenter?.checkBaseUrl()
        remoteConfigPresenter?.getUpdateType(this)
    }

    private fun sendAnalytics() {
        FireBaseHelper.instance().getFireBaseAnalytics()?.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, FireBaseConstant.SCREEN_LOGIN)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, LoginActivity::class.java.simpleName)
        }
    }

    private fun setupPresenter() {
        loginPresenter = LoginPresenter()
        loginPresenter?.attachView(this)

        remoteConfigPresenter = RemoteConfigPresenter()
        remoteConfigPresenter?.attachView(this)
    }

    @SuppressLint("TimberArgCount")
    private fun needPermissions() {
        SuitPermissions.with(this)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE)
                .onAccepted {
                    for (s in it) {
                        Timber.d("granted_permission", s)
                    }
                    showToast("Granted")
                }
                .onDenied {
                    showToast("Denied")
                }
                .onForeverDenied {
                    showToast("Forever denied")
                }
                .ask()
    }

    private fun setupInAppUpdate(mode: CommonConstant.UpdateMode) {
        inAppUpdateManager = InAppUpdateManager.builder(this, 100)
                ?.resumeUpdates(true) // Resume the update, if the update was stalled. Default is true
                ?.mode(mode)
                ?.snackBarMessage(getString(R.string.txt_update_completed))
                ?.snackBarAction(getString(R.string.txt_button_restart))
                ?.handler(this)

        inAppUpdateManager?.checkForAppUpdate()
    }

    private fun setupSocialLogin() {
        // Google  initialization
        mGoogleHelper = GoogleSignInHelper(this, getString(R.string.google_default_web_client_id), this)

        // fb initialization
        mFbHelper = FacebookHelper(this, getString(R.string.facebook_request_field))

        signOut()
    }

    private fun signOut() {
        mGoogleHelper?.performSignOut()
        mFbHelper?.performSignOut()
    }

    override fun onLoginSuccess(message: String?) {
        //goToActivity(MemberActivity::class.java, null, clearIntent = true, isFinish = true)
        showToast("Login Success")
    }

    override fun onLoginFailed(message: String?) {
        message?.let {
            showToast(message.toString())
        }
    }

    override fun onGoogleAuthSignIn(authToken: String?, userId: String?) {
        // send token & user_id to server
        loginPresenter?.login()
    }

    override fun onGoogleAuthSignInFailed(errorMessage: String?) {
        showToast(errorMessage.toString())
    }

    override fun onFbSignInFail(errorMessage: String?) {
        showToast(errorMessage.toString())
    }

    override fun onFbSignInSuccess(authToken: String?, userId: String?) {
        // send token & user_id to server
        loginPresenter?.login()
    }

    override fun onUpdateBaseUrlNeeded(type: String?, url: String?) {
        RemoteConfigHelper.changeBaseUrl(this, type.toString(), url.toString())
    }

    @SuppressLint("TimberArgCount")
    override fun onUpdateTypeReceive(update: UpdateType?) {
        update?.let {
            when (it.updateType) {
                CommonConstant.INAPPUPDATE -> {
                    setupInAppUpdate(CommonUtils.convertData(update.category))
                }
                CommonConstant.REMOTECONFIG -> {
                    if (CommonUtils.isUpdateAvailable(update.latestVersionCode)) {
                        val message: String = update.messages ?: "Update Available"
                        if (update.category == CommonConstant.IMMEDIATE) {
                            showDialogAlert(title = null, message = message, confirmCallback = {
                                CommonUtils.openAppInStore(this)
                            })
                        } else {
                            showDialogConfirmation(title = null, message, confirmCallback = {
                                CommonUtils.openAppInStore(this)
                            })
                        }
                    }
                }
            }
        }
    }

    override fun onInAppUpdateError(code: Int, error: Throwable?) {
        Timber.d(error, error?.message.toString())
    }

    override fun onInAppUpdateStatus(status: InAppUpdateStatus?) {
        if (status?.isDownloaded == true) {
            val rootView: View = window.decorView.findViewById(R.id.content)
            val snackBar = Snackbar.make(rootView,
                    "An update has just been downloaded.",
                    Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction("RESTART") {

                // Triggers the completion of the update of the app for the flexible flow.
                inAppUpdateManager?.completeUpdate()
            }
            snackBar.show()
        }
    }

    private fun actionClicked() {
        binding.relGoogle.setOnClickListener {
            mGoogleHelper?.performSignIn(this)
        }

        binding.relFacebook.setOnClickListener {
            mFbHelper?.performSignIn(this)
        }

//        binding.relTwitter.setOnClickListener {
//            if (CommonUtils.checkTwitterApp(this)) {
//                mTwitterHelper?.performSignIn()
//            } else {
//                showToast(getString(R.string.txt_twitter_not_installed))
//            }
//        }

        binding.tvSkipToTabMenu.setOnClickListener {
            goToActivity(TabMenuActivity::class.java, null, clearIntent = true, isFinish = true)
        }

        binding.tvSkipToSideMenu.setOnClickListener {
            goToActivity(SideMenuActivity::class.java, null, clearIntent = true, isFinish = true)
        }
    }

    @SuppressLint("TimberArgCount")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Timber.d("appupdates", "Result Ok")
                }
                Activity.RESULT_CANCELED -> {
                    Timber.d("appupdates", "Result Cancelled")
                    inAppUpdateManager?.checkForAppUpdate()
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    Timber.d("Update Failure")
                }
            }
        } else {
            if (data != null) {
                mGoogleHelper?.onActivityResult(requestCode, resultCode, data)
               // mTwitterHelper?.onActivityResult(requestCode, resultCode, data)
                mFbHelper?.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

}