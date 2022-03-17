package com.suitcore.feature.event

import com.suitcore.BaseApplication
import com.suitcore.base.presenter.BasePresenter
import com.suitcore.data.model.Event

/**
 * Created by dodydmw19 on 1/16/19.
 */

class EventPresenter : BasePresenter<EventView> {

    private var mvpView: EventView? = null

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    fun getEvents() {
        val listData = mutableListOf<Event>()
        listData.add(Event(1, "Bandung Jazz Night", "https://picsum.photos/id/1033/200/300", -6.8848417, 107.6119303))
        listData.add(Event(2, "Dago Culinary Day", "https://picsum.photos/id/1063/200/300", -6.884847, 107.614119))
        listData.add(Event(3, "Suitparty 2019", "https://picsum.photos/id/1068/200/300", -6.8838404, 107.6126062))
        listData.add(Event(4, "Malam Amal Anak Jalanan", "https://picsum.photos/id/1082/200/300", -6.8863473, 107.6142107))
        mvpView?.onEventsLoaded(listData)
    }

    override fun onDestroy() {}

    override fun attachView(view: EventView) {
        mvpView = view
    }

    override fun detachView() {
        mvpView = null
    }
}