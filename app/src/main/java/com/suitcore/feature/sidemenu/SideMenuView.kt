package com.suitcore.feature.sidemenu

import com.suitcore.base.presenter.MvpView
import com.suitcore.data.model.SideMenu


/**
 * Created by dodydmw19 on 1/3/19.
 */

interface SideMenuView : MvpView {

    fun onSideMenuLoaded(sideMenus: List<SideMenu>?)

    fun onFailedLoadSideMenu(message: String?)

}