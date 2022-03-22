package com.suitcore.feature.chooseuser

import com.suitcore.BaseApplication
import com.suitcore.base.presenter.BasePresenter

class ChooseUserPresenter : BasePresenter<ChooseUserView> {

    private var mvpView: ChooseUserView? = null

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    override fun onDestroy() {    }

    override fun attachView(view: ChooseUserView) {
        mvpView = view
    }

    override fun detachView() {
        mvpView = null
    }
}