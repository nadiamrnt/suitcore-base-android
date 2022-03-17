package com.suitcore.firebase.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Created by DODYDMW19 on 12/03/2020.
 */

class FireBaseHelper {

    private var firebaseAnalytics: FirebaseAnalytics? = null

    fun initialize(context: Context?) {
        // Obtain the FirebaseAnalytics instance.
        if (context != null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        }
    }

    /* firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
       param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
       param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
   }*/

    /*  firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
        param(FirebaseAnalytics.Param.ITEM_ID, id)
        param(FirebaseAnalytics.Param.ITEM_NAME, name)
        param(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
    }*/

    /* firebaseAnalytics.logEvent("share_image") {
        param("image_name", name)
        param("full_text", text)
    }*/

    fun getFireBaseAnalytics(): FirebaseAnalytics?{
        return firebaseAnalytics
    }

    companion object {

        private var sHelper: FireBaseHelper? = null

        fun instance(): FireBaseHelper {
            if (sHelper == null) {
                sHelper = FireBaseHelper()
            }
            return sHelper as FireBaseHelper
        }
    }
}
