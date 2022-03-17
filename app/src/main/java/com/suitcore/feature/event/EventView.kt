package com.suitcore.feature.event

import com.suitcore.base.presenter.MvpView
import com.suitcore.data.model.Event


/**
 * Created by dodydmw19 on 1/16/19.
 */

interface EventView : MvpView {

    fun onEventsLoaded(events: List<Event>?)

}