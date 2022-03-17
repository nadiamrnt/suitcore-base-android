package com.suitcore.feature.login

import com.suitcore.BaseApplication
import com.suitcore.base.presenter.BasePresenter

/**
 * Created by dodydmw19 on 7/18/18.
 */

class LoginPresenter : BasePresenter<LoginView>{

    private var mvpView: LoginView? = null

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    fun login(){
        mvpView?.onLoginSuccess("success")
    }

    override fun onDestroy() {
    }

    override fun attachView(view: LoginView) {
        mvpView = view
    }

    override fun detachView() {
        mvpView = null
    }
}