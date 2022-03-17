package com.suitcore.helper.rxbus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by DODYDMW19 on 3/22/2017.
 */

class RxBus {

    private val bus = PublishSubject.create<Any>()

    fun send(o: Any) {
        bus.onNext(o)
    }

    fun toObservable(): Observable<Any> {
        return bus
    }

    fun hasObservers(): Boolean {
        return bus.hasObservers()
    }
}
