package com.suitcore.feature.event

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.gson.JsonPrimitive
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.suitcore.R
import com.suitcore.base.ui.BaseFragment
import com.suitcore.data.model.Event
import com.suitcore.databinding.FragmentEventBinding
import com.suitcore.feature.event.search.SearchPlaceActivity
import com.suitcore.helper.CommonUtils


class EventFragment : BaseFragment<FragmentEventBinding>(), EventView {

    private var eventPresenter: EventPresenter? = null
    private var adapter: EventPagerAdapter? = null
    private var mapBox: MapboxMap? = null
    private var symbolManager: SymbolManager? = null
    private var arrayEvent = emptyList<Event>()
    private var hashMapEvents: HashMap<Int, Int> = hashMapOf()
    private var markerNormal = "marker_normal"
    private var markerSelected = "marker_selected"

    companion object {
        fun newInstance(): Fragment {
            return EventFragment()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEventBinding = FragmentEventBinding.inflate(inflater, container, false)

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupPresenter()
        actionClick()
    }

    private fun setupPresenter() {
        eventPresenter = EventPresenter()
        eventPresenter?.attachView(this)
        eventPresenter?.getEvents()
    }

    private fun setupMap(events: List<Event>) {
        setupPager(events)
        binding.mapEvent.getMapAsync { mapBoxMap ->
            this.mapBox = mapBoxMap

            mapBoxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                style.addImage(markerNormal, BitmapFactory.decodeResource(this.resources, R.drawable.ic_marker_normal))
                style.addImage(markerSelected, BitmapFactory.decodeResource(this.resources, R.drawable.ic_marker_selected))

                // create symbol manager
                val geoJsonOptions = GeoJsonOptions().withTolerance(0.4f)
                symbolManager = SymbolManager(binding.mapEvent, mapBoxMap, style, null, geoJsonOptions)
                symbolManager?.addClickListener { symbol ->
                    showToast("value : " + symbol.data?.asString.toString())
                    val pos = hashMapEvents.filterKeys { it == symbol.data?.asInt }.getValue(symbol.data?.asInt!!)
                    setSelectedMarker(pos, symbolManager)
                }
                events.forEachIndexed { index, event ->
                    symbolManager?.create(createCustomMarker(event.lat ?: 0.0, event.lng
                            ?: 0.0, event, index))
                }

                events.first().let { event ->
                    CommonUtils.setCamera(event.lat ?: 0.0, event.lng ?: 0.0, mapBox)
                }
            }
        }
    }

    private fun createCustomMarker(latitude: Double, longitude: Double, event: Event, index: Int): SymbolOptions {
        return SymbolOptions()
                .withData(JsonPrimitive(event.id.toString()))
                .withLatLng(LatLng(latitude, longitude))
                .withIconImage(if (index == 0) markerSelected else markerNormal)
                .withIconSize(1.0f)
                .withSymbolSortKey(10.0f)
                .withDraggable(false)
    }

    private fun setSelectedMarker(position: Int, symbolManager: SymbolManager?) {

        val symbol = symbolManager?.annotations?.valueAt(position)
        symbol?.iconImage = markerSelected
        symbolManager?.symbolPlacement = markerSelected
        symbolManager?.update(symbol)

        binding.vpEvent.currentItem = position
        for (item in 0 until symbolManager?.annotations?.size()!!) {
            val currentSymbol = symbolManager.annotations!![item.toLong()]
            if (currentSymbol?.id != symbol?.id) {
                currentSymbol?.iconImage = markerNormal
                symbolManager.symbolPlacement = markerNormal
                symbolManager.update(currentSymbol)
            }
        }

        symbol.let { it?.latLng?.longitude?.let { it1 -> CommonUtils.setCamera(it.latLng.latitude, it1, mapBox) } }
    }

    private fun setupPager(events: List<Event>) {
        adapter = EventPagerAdapter(childFragmentManager)

        events.forEachIndexed { index, event ->
            hashMapEvents[event.id!!] = index
        }

        adapter?.listData = events
        binding.vpEvent.adapter = adapter

        binding.vpEvent.clipToPadding = false
        binding.vpEvent.setPadding(50, 0, 50, 0)
        binding.vpEvent.pageMargin = 20

        binding.vpEvent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                setSelectedMarker(p0, symbolManager)
            }
        })
    }

    override fun onEventsLoaded(events: List<Event>?) {
        arrayEvent = events!!
        setupMap(events)
    }

    private fun actionClick() {
        binding.relSearch.setOnClickListener {
            goToActivity(SearchPlaceActivity::class.java, null, false, isFinish = false)
        }
    }

}