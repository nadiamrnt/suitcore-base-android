package com.suitcore.helper.localization

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.suitcore.data.local.prefs.DataConstant
import com.suitcore.data.local.prefs.SuitPreferences
import java.util.*


/**
 * Created by DODYDMW19 on 02/08/2022.
 */

object LanguageHelper {

    var mEnglishFlag = "en"
    var mIndonesianFlag = "in"

    fun setLocale(context: Context?): Context {
        return updateResources(context!!, getCurrentLanguage(context)!!)
    }

    fun setNewLocale(context: Context, language: String) {
        persistLanguagePreference(language)
        updateResources(context, language)
    }

    private fun getCurrentLanguage(context: Context?): String {
        val mCurrentLanguage = SuitPreferences.instance()?.getString(DataConstant.CURRENT_LANG).toString()
        return mCurrentLanguage.ifEmpty { mEnglishFlag }
    }

    private fun persistLanguagePreference(language: String) {
        SuitPreferences.instance()?.saveString(DataConstant.CURRENT_LANG, language)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("ObsoleteSdkInt")
    fun updateResources(context: Context, language: String): Context {

        var contextFun = context
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = Configuration(resources.configuration)

        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(locale)
            contextFun = context.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
        return contextFun
    }

}
