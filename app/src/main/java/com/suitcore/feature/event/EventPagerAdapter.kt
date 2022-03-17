package com.suitcore.feature.event

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.suitcore.data.model.Event

class EventPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var listData: List<Event>? = null

    override fun getItem(p0: Int): Fragment {
        return listData?.get(p0)?.let { ImageFragment.newInstance(it) }!!
    }

    override fun getCount(): Int {
        return listData?.size ?: 0
    }
}