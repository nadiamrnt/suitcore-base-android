package com.suitcore.helper

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import com.jakewharton.processphoenix.ProcessPhoenix
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.suitcore.BuildConfig
import com.suitcore.data.local.RealmHelper
import com.suitcore.data.local.prefs.DataConstant
import com.suitcore.data.local.prefs.SuitPreferences
import com.suitcore.data.model.User
import com.suitcore.feature.login.LoginActivity
import com.suitcore.feature.splashscreen.SplashScreenActivity
import java.io.IOException


/**
 * Created by dodydmw19 on 7/18/18.
 */

class CommonUtils {

    companion object {

        fun openAppInStore(context: Context) {
            // you can also use BuildConfig.APPLICATION_ID
            try {
                val appId = context.packageName
                val rateIntent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appId"))
                var marketFound = false
                // find all applications able to handle our rateIntent
                val otherApps = context.packageManager
                        .queryIntentActivities(rateIntent, 0)
                for (otherApp in otherApps) {
                    // look for Google Play application
                    if (otherApp.activityInfo.applicationInfo.packageName == "com.android.vending") {

                        val otherAppActivity = otherApp.activityInfo
                        val componentName = ComponentName(
                                otherAppActivity.applicationInfo.packageName,
                                otherAppActivity.name
                        )
                        // make sure it does NOT open in the stack of your activity
                        rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        // task reparenting if needed
                        rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                        // if the Google Play was already open in a search result
                        //  this make sure it still go to the app page you requested
                        rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        // this make sure only the Google Play app is allowed to
                        // intercept the intent
                        rateIntent.component = componentName
                        context.startActivity(rateIntent)
                        marketFound = true
                        break

                    }
                }

                // if GP not present on device, open web browser
                if (!marketFound) {
                    val webIntent = Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appId"))
                    context.startActivity(webIntent)

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun restartApp(activity: Activity?) {
            activity?.let {
                activity.run {
                    val intent = Intent(activity, SplashScreenActivity::class.java)
                    ProcessPhoenix.triggerRebirth(activity, intent)
                }
            }
        }

        fun restartApp(context: Context?) {
            context?.let {
                context.run {
                    val intent = Intent(context, SplashScreenActivity::class.java)
                    ProcessPhoenix.triggerRebirth(context, intent)
                }
            }
        }

        fun setCamera(lat: Double, lng: Double, mapBox: MapboxMap?) {
            val position = CameraPosition.Builder()
                    .target(LatLng(lat, lng))
                    .zoom(15.0)
                    .build()

            mapBox?.animateCamera(CameraUpdateFactory
                    .newCameraPosition(position), 3000)
        }

        fun clearLocalStorage() {
            val suitPreferences = SuitPreferences.instance()
            val currentUrl: String? = SuitPreferences.instance()?.getString(DataConstant.BASE_URL)
            suitPreferences?.clearSession()
            currentUrl?.let{
                SuitPreferences.instance()?.saveString(DataConstant.BASE_URL, currentUrl.toString())
            }
            val realm: RealmHelper<User> = RealmHelper()
            realm.removeAllData()
        }

        fun setDefaultBaseUrlIfNeeded() {
            val currentUrl: String? = SuitPreferences.instance()?.getString(DataConstant.BASE_URL)
            if (currentUrl == null) {
                SuitPreferences.instance()?.saveString(DataConstant.BASE_URL, BuildConfig.BASE_URL)
            }
        }

        fun createIntent(context: Context, actDestination: Class<out Activity>): Intent {
            return Intent(context, actDestination)
        }

        fun gotoLoginScreen(activity: Activity?) {
            activity?.let{
                val intent = Intent(it, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                it.startActivity(intent)
                it.finish()
            }
        }

        fun loadJSONFromAsset(json_name: String, context: Context): String? {
            val json: String
            try {
                val `is` = context.assets.open(json_name)
                val size = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                json = String(buffer, charset("UTF-8"))
            } catch (ex: IOException) {
                ex.printStackTrace()
                return null
            }

            return json
        }

         fun convertData(type: String?): CommonConstant.UpdateMode{
            return when(type){
                "flexible" -> CommonConstant.UpdateMode.FLEXIBLE
                "immediate" -> CommonConstant.UpdateMode.IMMEDIATE
                else -> CommonConstant.UpdateMode.FLEXIBLE
            }
        }

        fun isUpdateAvailable(version: Int?): Boolean{
            version?.let{
                val currentVersion = BuildConfig.VERSION_CODE
                return version != 0 && currentVersion < it
            }?:run{
                return false
            }
        }

        @SuppressLint("HardwareIds")
        fun getIMEIDeviceId(context: Context): String {
            val deviceId: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            } else {
                val mTelephony =
                    context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return ""
                    }
                }
                if (mTelephony.deviceId != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        mTelephony.imei
                    } else {
                        mTelephony.deviceId
                    }
                } else {
                    Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                }
            }
            return deviceId
        }

    }

}