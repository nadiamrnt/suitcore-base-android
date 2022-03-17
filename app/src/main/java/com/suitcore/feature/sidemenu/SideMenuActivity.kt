package com.suitcore.feature.sidemenu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.base.ui.recyclerview.BaseRecyclerView
import com.suitcore.data.model.SideMenu
import com.suitcore.databinding.ActivitySideMenuBinding
import com.suitcore.databinding.LayoutSideMenuBinding
import com.suitcore.feature.event.EventFragment
import com.suitcore.feature.fragmentsample.SampleFragment
import com.suitcore.feature.login.LoginActivity
import com.suitcore.feature.member.MemberFragment
import com.suitcore.helper.CommonConstant


/**
 * Created by @dodydmw19 on 10, September, 2020
 */

class SideMenuActivity : BaseActivity<ActivitySideMenuBinding>(), SideMenuView, SideMenuItemView.OnActionListener{

    private var sideMenuPresenter: SideMenuPresenter? = null
    private var sideMenuAdapter: SideMenuAdapter? = SideMenuAdapter(this)
    private var isDrawerOpen = false
    private var finalFragment: Fragment? = null
    private var arraySideMenu: List<SideMenu>? = emptyList()
    private lateinit var viewSideMenuBinding: LayoutSideMenuBinding

    companion object {
        fun createIntent(context: Context?): Intent {
            return Intent(context, SideMenuActivity::class.java)
        }
    }

    override fun getViewBinding(): ActivitySideMenuBinding = ActivitySideMenuBinding.inflate(layoutInflater)

    override fun onViewReady(savedInstanceState: Bundle?) {
        initIncludeViewBinding()
        setupProgressView()
        setupEmptyView()
        setupErrorView()
        setupPresenter()
        setUpSideBar()
        actionClick()
    }

    private fun initIncludeViewBinding(){
        viewSideMenuBinding = binding.sideMenu
    }

    private fun setupPresenter() {
        sideMenuPresenter = SideMenuPresenter(this)
        sideMenuPresenter?.attachView(this)
        sideMenuPresenter?.getMenuFromFile()
    }

    private fun setUpSideBar() {
        val drawerToggle = object : ActionBarDrawerToggle(this, binding.drawerLayout, binding.mToolbar, 0, 0) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                isDrawerOpen = true
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                isDrawerOpen = false
                if (finalFragment != null) {
                    setContentFragment(finalFragment)
                }
            }
        }

        drawerToggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.black)
        drawerToggle.syncState()
        binding.drawerLayout.addDrawerListener(drawerToggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        sideMenuAdapter?.selectedItem = 0
        finalFragment = MemberFragment()
        //Navigation.findNavController(this).navigate(R.id.action)
        binding.tvTitle.text = getString(R.string.txt_toolbar_home)
        setContentFragment(finalFragment)
    }

    private fun setupList(sideMenus: List<SideMenu>) {
        viewSideMenuBinding.rvSideMenu.apply {
            setUpAsList()
            setAdapter(sideMenuAdapter)
        }
        sideMenuAdapter?.setOnActionListener(this)
        sideMenuAdapter?.add(sideMenus)
        viewSideMenuBinding.rvSideMenu.stopShimmer()
        viewSideMenuBinding.rvSideMenu.showRecycler()
        finishLoad(viewSideMenuBinding.rvSideMenu)
    }

    private fun setupProgressView() {
        R.layout.layout_shimmer_member.apply {
            viewSideMenuBinding.rvSideMenu.baseShimmerBinding.viewStub.layoutResource = this
        }

        viewSideMenuBinding.rvSideMenu.baseShimmerBinding.viewStub.inflate()
    }

    private fun setupEmptyView() {
        viewSideMenuBinding.rvSideMenu.setImageEmptyView(R.drawable.empty_state)
        viewSideMenuBinding.rvSideMenu.setTitleEmptyView(getString(R.string.txt_empty_member))
        viewSideMenuBinding.rvSideMenu.setContentEmptyView(getString(R.string.txt_empty_member_content))
        viewSideMenuBinding.rvSideMenu.setEmptyButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
            }

        })
    }

    private fun setupErrorView() {
        viewSideMenuBinding.rvSideMenu.setImageErrorView(R.drawable.empty_state)
        viewSideMenuBinding.rvSideMenu.setTitleErrorView(getString(R.string.txt_error_no_internet_connection))
        viewSideMenuBinding.rvSideMenu.setContentErrorView(getString(R.string.txt_error_connection))
        viewSideMenuBinding.rvSideMenu.setErrorButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
            }

        })
    }

    private fun setContentFragment(fragment: Fragment?) {
        finalFragment = fragment
        finalFragment?.let {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, it)
                    .commitAllowingStateLoss()
        }
    }

    private fun closeDrawers() {
        binding.drawerLayout.closeDrawers()
    }

    override fun onSideMenuLoaded(sideMenus: List<SideMenu>?) {
        sideMenus?.let {
            arraySideMenu = sideMenus
            setupList(sideMenus)
            sideMenuAdapter?.selectedItem = 0
        }
    }

    override fun onFailedLoadSideMenu(message: String?) {
        message?.let {
            showToast(it)
        }
    }

    override fun onClicked(view: SideMenuItemView, position: Int) {
        view.getData().let { data ->

            binding.tvTitle.text = data?.label
            finalFragment = null
            sideMenuAdapter?.selectedItem = position + 1
            finalFragment = when (view.getData()?.url) {
                CommonConstant.MENU_HOME -> {
                    MemberFragment.newInstance()
                }
                CommonConstant.MENU_FRAGMENT_1 -> {
                    EventFragment.newInstance()
                }
                CommonConstant.MENU_FRAGMENT_2 -> {
                    SampleFragment.newInstance()
                }
                CommonConstant.MENU_FRAGMENT_3 -> {
                    SampleFragment.newInstance()
                }
                else -> {
                    MemberFragment.newInstance()
                }
            }

            closeDrawers()

        }
    }

    private fun actionClick() {
        viewSideMenuBinding.relLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            closeDrawers()
        }
    }

    override fun onBackPressed() {
        if (isDrawerOpen) {
            closeDrawers()
        } else {
            finish()
        }
    }

}