package com.suitcore.feature.splashscreen

import android.os.Bundle
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.databinding.ActivitySplashscreenBinding
import com.suitcore.feature.login.LoginActivity

/**
 * Created by dodydmw19 on 12/19/18.
 */

class SplashScreenActivity : BaseActivity<ActivitySplashscreenBinding>(), SplashScreenView {

    private var splashScreenPresenter: SplashScreenPresenter? = null

    override fun getViewBinding(): ActivitySplashscreenBinding = ActivitySplashscreenBinding.inflate(layoutInflater)

    override fun onViewReady(savedInstanceState: Bundle?) {
        changeProgressBarColor(R.color.white, binding.progressBar)
        setupPresenter()
    }

    private fun setupPresenter() {
        splashScreenPresenter = SplashScreenPresenter()
        splashScreenPresenter?.attachView(this)
        splashScreenPresenter?.initialize()
    }

    override fun navigateToMainView() {
        goToActivity(LoginActivity::class.java,  null, clearIntent = true, isFinish = true)
    }

}