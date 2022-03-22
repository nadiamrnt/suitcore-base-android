package com.suitcore.feature.user

import androidx.lifecycle.LifecycleOwner
import com.suitcore.BaseApplication
import com.suitcore.base.presenter.BasePresenter
import com.suitcore.data.local.RealmHelper
import com.suitcore.data.model.User
import com.suitcore.data.remote.services.APIService
import io.realm.RealmResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class UserPresenter : BasePresenter<UserView>, CoroutineScope {

    @Inject
    lateinit var apiService: APIService
    private var mvpView: UserView? = null
    override val coroutineContext: CoroutineContext get() =  Dispatchers.IO + job
    private var job: Job = Job()
    private var mRealm: RealmHelper<User>? = RealmHelper()

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    override fun onDestroy() {
        detachView()
    }

    override fun attachView(view: UserView) {
        mvpView = view

        if (mvpView is LifecycleOwner) {
            (mvpView as LifecycleOwner).lifecycle.addObserver(this)
        }
    }

    override fun detachView() {
        mvpView = null
    }

    fun getMemberWithCoroutines(currentPage: Int?) = launch(Dispatchers.Main) {
        runCatching {
            apiService.getMembersCoroutinesAsync(10, currentPage!!).await()
        }. onSuccess { data ->
            if (data.arrayData?.isNotEmpty()!!) {
                val rest = data.arrayData
                runCatching {
                    saveToCache(data.arrayData)
                }. onSuccess {
                    mvpView?.onMemberLoaded(rest)
                }. onFailure { mvpView?.onFailed(it) }
            } else {
                if (currentPage == 1) {
                    mvpView?.onMemberEmpty()
                } else {
                    mvpView?.onMemberLoaded(emptyList())
                }
            }
        }.onFailure { mvpView?.onFailed(it) }
    }

    private fun saveToCache(data: List<User>?) {
        if (data != null && data.isNotEmpty()) {
            mRealm?.saveList(data)
        }
    }

    fun getMemberCache() {
        val data: RealmResults<User>? = mRealm?.getData(User::class.java, "id")

        mvpView?.onMemberCacheLoaded(data)
    }
}