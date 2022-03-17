package com.suitcore.firebase.remoteconfig

import android.app.Activity
import com.suitcore.data.local.prefs.DataConstant
import com.suitcore.data.local.prefs.SuitPreferences
import com.suitcore.helper.CommonUtils
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Created by dodydmw19 on 9/27/18.
 */

class RemoteConfigHelper {

    companion object {

        fun changeBaseUrl(activity: Activity?, type: String, endpoint: String) {
            var url = ""
            CommonUtils.clearLocalStorage()

            when (type) {
                "new" -> url = endpoint
                "default" -> url = endpoint
            }
            SuitPreferences.instance()?.saveString(DataConstant.BASE_URL, url)

            Executors.newSingleThreadScheduledExecutor().schedule({
                CommonUtils.restartApp(activity)
            }, 1, TimeUnit.SECONDS)

        }
    }
}