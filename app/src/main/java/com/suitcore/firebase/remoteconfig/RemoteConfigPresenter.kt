package com.suitcore.firebase.remoteconfig

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.suitcore.BaseApplication
import com.suitcore.BuildConfig
import com.suitcore.R
import com.suitcore.base.presenter.BasePresenter
import com.suitcore.data.local.prefs.DataConstant
import com.suitcore.data.local.prefs.SuitPreferences
import com.suitcore.data.model.UpdateType
import com.suitcore.helper.CommonConstant
import com.suitcore.helper.CommonUtils

class RemoteConfigPresenter : BasePresenter<RemoteConfigView> {

    private var mFireBaseRemoteConfig: FirebaseRemoteConfig? = null
    private var mvpView: RemoteConfigView? = null

    init {
        BaseApplication.applicationComponent.inject(this)
        setupFireBaseRemoteConfig()
    }

    private fun setupFireBaseRemoteConfig() {
        mFireBaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFireBaseRemoteConfig?.setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0)
                        .build())

        mFireBaseRemoteConfig?.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    fun checkBaseUrl() {
        if (mFireBaseRemoteConfig == null) {
            setupFireBaseRemoteConfig()
        }

        mFireBaseRemoteConfig?.fetchAndActivate()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Log.d("***", "Config params updated: $updated")
                    }

                    val newBaseUrl: String? = mFireBaseRemoteConfig?.getString(CommonConstant.NEW_BASE_URL)
                    val currentUrl: String? = SuitPreferences.instance()?.getString(DataConstant.BASE_URL)
                    if (newBaseUrl != null) {
                        if (newBaseUrl.toString().isNotEmpty()) {
                            if (currentUrl != newBaseUrl) mvpView?.onUpdateBaseUrlNeeded("new", newBaseUrl.toString())
                        } else {
                            if (currentUrl != null && currentUrl.isNotEmpty() && currentUrl != BuildConfig.BASE_URL) {
                                mvpView?.onUpdateBaseUrlNeeded("default", BuildConfig.BASE_URL)
                            } else {
                                CommonUtils.setDefaultBaseUrlIfNeeded()
                            }
                        }
                    }
                }
    }

    fun getUpdateType(context: Context) {
        var updateFromConsole: String? = ""
        val updateDefaultJSON = CommonUtils.loadJSONFromAsset("update.json", context)
        val gSon = Gson()
        var updateType: UpdateType? = gSon.fromJson(updateDefaultJSON, UpdateType::class.java)

        if (mFireBaseRemoteConfig == null) {
            setupFireBaseRemoteConfig()
        }

        mFireBaseRemoteConfig?.fetchAndActivate()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Log.d("***", "Config params updated: $updated")
                    }

                    if (mFireBaseRemoteConfig?.getString(CommonConstant.NOTIFY_UPDATE_TYPE) != null && mFireBaseRemoteConfig?.getString(CommonConstant.NOTIFY_UPDATE_TYPE)!!.isNotEmpty()) {
                        updateFromConsole = mFireBaseRemoteConfig?.getString(CommonConstant.NOTIFY_UPDATE_TYPE)
                        updateType = UpdateType()
                        updateType = gSon.fromJson(updateFromConsole.toString(), UpdateType::class.java)
                        mvpView?.onUpdateTypeReceive(updateType)
                    }else{
                        mvpView?.onUpdateTypeReceive(updateType)
                    }
                }
    }

    override fun onDestroy() {
        detachView()
    }

    override fun attachView(view: RemoteConfigView) {
        mvpView = view
        // Initialize this presenter as a lifecycle-aware when a view is a lifecycle owner.
        if (mvpView is LifecycleOwner) {
            (mvpView as LifecycleOwner).lifecycle.addObserver(this)
        }
    }

    override fun detachView() {
        mvpView = null
    }

}