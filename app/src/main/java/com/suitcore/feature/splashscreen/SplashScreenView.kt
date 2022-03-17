package com.suitcore.feature.splashscreen

import android.annotation.SuppressLint
import com.suitcore.base.presenter.MvpView

/**
 * Created by dodydmw19 on 12/19/18.
 */

@SuppressLint("CustomSplashScreen")
interface SplashScreenView : MvpView {

    fun navigateToMainView()

}