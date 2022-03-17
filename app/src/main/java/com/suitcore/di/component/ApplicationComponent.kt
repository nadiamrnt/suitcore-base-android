package com.suitcore.di.component

import com.suitcore.di.module.ApplicationModule
import com.suitcore.di.scope.SuitCoreApplicationScope
import com.suitcore.feature.event.EventPresenter
import com.suitcore.feature.event.search.SearchPlacePresenter
import com.suitcore.feature.login.LoginPresenter
import com.suitcore.feature.member.MemberPresenter
import com.suitcore.feature.sidemenu.SideMenuPresenter
import com.suitcore.feature.splashscreen.SplashScreenPresenter
import com.suitcore.firebase.remoteconfig.RemoteConfigPresenter
import com.suitcore.onesignal.OneSignalPresenter
import dagger.Component

@SuitCoreApplicationScope
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(memberPresenter: MemberPresenter)

    fun inject(loginPresenter: LoginPresenter)

    fun inject(splashScreenPresenter: SplashScreenPresenter)

    fun inject(oneSignalPresenter: OneSignalPresenter)

    fun inject(remoteConfigPresenter: RemoteConfigPresenter)

    fun inject(searchPlacePresenter: SearchPlacePresenter)

    fun inject(eventPresenter: EventPresenter)

    fun inject(sideMenuPresenter: SideMenuPresenter)
}