package com.suitcore

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.facebook.drawee.backends.pipeline.Fresco
import com.mapbox.mapboxsdk.Mapbox
import com.onesignal.OSNotificationOpenedResult
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal
import com.suitcore.data.local.prefs.DataConstant
import com.suitcore.data.local.prefs.SuitPreferences
import com.suitcore.di.component.ApplicationComponent
import com.suitcore.di.component.DaggerApplicationComponent
import com.suitcore.di.module.ApplicationModule
import com.suitcore.firebase.analytics.FireBaseHelper
import com.suitcore.helper.ActivityLifecycleCallbacks
import com.suitcore.helper.CommonConstant
import com.suitcore.helper.CommonUtils
import com.suitcore.helper.localization.LanguageHelper
import com.suitcore.helper.rxbus.RxBus
import com.suitcore.onesignal.OneSignalHelper
import io.realm.Realm
import io.realm.RealmConfiguration


/**
 * Created by DODYDMW19 on 1/30/2018.
 */

class BaseApplication : MultiDexApplication() {

    val mActivityLifecycleCallbacks = ActivityLifecycleCallbacks()

    init {
        instance = this
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    companion object {
        lateinit var applicationComponent: ApplicationComponent
        lateinit var appContext: Context
        lateinit var bus: RxBus
        private var instance: BaseApplication? = null

        fun currentActivity(): Activity? {
            return instance?.mActivityLifecycleCallbacks?.currentActivity
        }
    }

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        //bus = RxBus()

        // Initial Preferences
        SuitPreferences.init(applicationContext)
        FireBaseHelper.instance().initialize(this)

        CommonUtils.setDefaultBaseUrlIfNeeded()

        appContext = applicationContext
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

        Fresco.initialize(this)

        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            .schemaVersion(1)
            .allowWritesOnUiThread(true)
            .deleteRealmIfMigrationNeeded()
            //.encryptionKey(CommonUtils.getKey()) // encrypt realm
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)

        OneSignalHelper.initOneSignal(this)

        Mapbox.getInstance(this, CommonConstant.MAP_BOX_TOKEN)
    }

    override fun attachBaseContext(base: Context) {
        SuitPreferences.init(base)
        super.attachBaseContext(LanguageHelper.setLocale(base))
        MultiDex.install(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LanguageHelper.setLocale(this)
    }
}