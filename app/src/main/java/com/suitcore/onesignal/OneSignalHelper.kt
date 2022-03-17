package com.suitcore.onesignal

import android.content.Context
import com.onesignal.OSNotificationOpenedResult
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal
import com.suitcore.BuildConfig
import com.suitcore.data.local.prefs.DataConstant
import com.suitcore.data.local.prefs.SuitPreferences
import com.suitcore.helper.CommonConstant


/**
 * Created by @dodydmw19 on 05, October, 2021
 */

class OneSignalHelper {

    companion object{
        fun initOneSignal(context: Context){
            // OneSignal Initialization
            OneSignal.initWithContext(context)
            OneSignal.setAppId(CommonConstant.ONE_SIGNAL_APP_ID)

            initNotificationOpenedHandler()
            notificationForeGroundHandler()

            OneSignal.unsubscribeWhenNotificationsAreDisabled(true)
            OneSignal.pauseInAppMessages(true)
            OneSignal.setLocationShared(false)

            val device = OneSignal.getDeviceState()
            val userId = device?.userId //push player_id

            userId?.let {
                SuitPreferences.instance()?.saveString(DataConstant.PLAYER_ID, it)
            }

            if (BuildConfig.DEBUG) {
                OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
            }
        }

        private fun initNotificationOpenedHandler(){
            OneSignal.setNotificationOpenedHandler { result: OSNotificationOpenedResult ->
                OneSignal.onesignalLog(
                    OneSignal.LOG_LEVEL.VERBOSE,
                    "OSNotificationOpenedResult result: $result"
                )
            }
        }

        private fun notificationForeGroundHandler(){
            OneSignal.setNotificationWillShowInForegroundHandler { notificationReceivedEvent: OSNotificationReceivedEvent ->
                OneSignal.onesignalLog(
                    OneSignal.LOG_LEVEL.VERBOSE, "NotificationWillShowInForegroundHandler fired!" +
                            " with notification event: " + notificationReceivedEvent.toString()
                )
                val notification = notificationReceivedEvent.notification
                //val data = notification.additionalData
                notificationReceivedEvent.complete(notification)
            }
        }
    }

}