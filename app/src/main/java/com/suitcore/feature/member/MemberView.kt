package com.suitcore.feature.member

import com.suitcore.base.presenter.MvpView
import com.suitcore.data.model.User
import io.realm.RealmList
import io.realm.RealmResults

/**
 * Created by DODYDMW19 on 1/30/2018.
 */

interface MemberView : MvpView {

    fun onMemberCacheLoaded(members: RealmResults<User>?)

    fun onMemberLoaded(members: List<User>?)

    fun onMemberEmpty()

    fun onFailed(error: Any?)

}