package com.suitcore.feature.event.search

import com.suitcore.base.presenter.MvpView
import com.suitcore.data.model.Place

/**
 * Created by dodydmw19 on 1/14/19.
 */

interface SearchPlaceView : MvpView {

    fun onPlaceReceive(places: List<Place>?)

    fun onAddressReceive(address: String?)

    fun onPlaceNotFound()

    fun onFailedLoadPlaces(message: String?)

}