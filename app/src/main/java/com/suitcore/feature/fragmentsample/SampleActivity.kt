package com.suitcore.feature.fragmentsample

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.databinding.ActivityTestBinding

/**
 * Created by dodydmw19 on 7/30/18.
 */

class SampleActivity : BaseActivity<ActivityTestBinding>(){

    private lateinit var mCurrentFragment: Fragment

    override fun getViewBinding(): ActivityTestBinding = ActivityTestBinding.inflate(layoutInflater)

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupToolbar(binding.mToolbar, true)
        setContentFragment(SampleFragment.newInstance())
    }

    private fun setContentFragment(fragment: Fragment) {
        mCurrentFragment = fragment
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, mCurrentFragment)
                .commitAllowingStateLoss()
    }

}