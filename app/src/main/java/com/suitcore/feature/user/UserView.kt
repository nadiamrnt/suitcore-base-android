package com.suitcore.feature.user

import com.suitcore.base.presenter.MvpView
import com.suitcore.data.model.User
import io.realm.RealmResults

interface UserView : MvpView {

    fun onMemberCacheLoaded(members: RealmResults<User>?)

    fun onMemberLoaded(members: List<User>?)

    fun onMemberEmpty()

    fun onFailed(error: Any?)

}