package com.suitcore.feature.tabmenu

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.suitcore.feature.event.EventFragment
import com.suitcore.feature.fragmentsample.SampleFragment
import com.suitcore.feature.member.MemberFragment

class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var memberFragment: Fragment? = null
    private var dialogFragment: Fragment? = null
    private var mapFragment: Fragment? = null

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> generateMemberFragment()
        1 -> generateDialogFragment()
        2 -> generateMapFragment()
        else -> generateMemberFragment()
    }
    override fun getCount(): Int = 3

    private fun generateMemberFragment(): Fragment = if (memberFragment == null) {
        MemberFragment.newInstance()
    }else{
        memberFragment!!
    }

    private fun generateDialogFragment(): Fragment = if (dialogFragment == null) {
        SampleFragment.newInstance()
    }else{
        dialogFragment!!
    }

    private fun generateMapFragment(): Fragment = if (mapFragment == null) {
        EventFragment.newInstance()
    }else{
        mapFragment!!
    }

}