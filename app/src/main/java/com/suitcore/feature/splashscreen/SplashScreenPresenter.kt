package com.suitcore.feature.splashscreen

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.suitcore.BaseApplication
import com.suitcore.base.presenter.BasePresenter

/**
 * Created by dodydmw19 on 12/19/18.
 */

class SplashScreenPresenter : BasePresenter<SplashScreenView> {

    private var mvpView: SplashScreenView? = null
    private val time: Long = 3000

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    override fun onDestroy() {
        detachView()
    }

    fun initialize() {
        Handler(Looper.getMainLooper()).postDelayed({
            mvpView?.navigateToMainView()
        }, time)
    }

    override fun attachView(view: SplashScreenView) {
        mvpView = view
    }

    override fun detachView() {
        mvpView = null
    }
}