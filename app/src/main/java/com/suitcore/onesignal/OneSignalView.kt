package com.suitcore.onesignal

import com.suitcore.base.presenter.MvpView

/**
 * Created by dodydmw19 on 6/12/19.
 */

interface OneSignalView : MvpView {

    fun onRegisterIdSuccess(message: String?)

    fun onRegisterIdFailed(error: Any?)

}