package com.suitcore.firebase.remoteconfig

import com.suitcore.base.presenter.MvpView
import com.suitcore.data.model.UpdateType
import com.suitcore.helper.CommonConstant

interface RemoteConfigView : MvpView {

    fun onUpdateBaseUrlNeeded(type: String?, url: String?)

    fun onUpdateTypeReceive(update: UpdateType?)

}