package com.suitcore.feature.user

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.base.ui.recyclerview.BaseRecyclerView
import com.suitcore.base.ui.recyclerview.EndlessScrollCallback
import com.suitcore.data.model.ErrorCodeHelper
import com.suitcore.data.model.User
import com.suitcore.databinding.ActivityUserBinding
import com.suitcore.feature.member.SingleMemberItemView
import io.realm.RealmResults

class UserActivity : BaseActivity<ActivityUserBinding>(), UserView, SingleMemberItemView.OnActionListener {

    private var userPresenter: UserPresenter? = null
    private var userAdapter: UserAdapter? = UserAdapter(this)
    private var currentPage: Int = 1

    override fun getViewBinding(): ActivityUserBinding = ActivityUserBinding.inflate(layoutInflater)

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupProgressView()
        setupEmptyView()
        setupErrorView()
        setupList()
        Handler(Looper.getMainLooper()).postDelayed({
            setupPresenter()
        }, 100)
    }

    private fun setupPresenter() {
        userPresenter = UserPresenter()
        userPresenter?.attachView(this)
        userPresenter?.getMemberCache()
    }

    private fun loadData(page: Int) {
        userPresenter?.getMemberWithCoroutines(page)
    }

    private fun setupProgressView() {
        R.layout.layout_shimmer_member.apply {
            binding.rvUser.baseShimmerBinding.viewStub.layoutResource = this
        }

        binding.rvUser.baseShimmerBinding.viewStub.inflate()
    }

    private fun setupEmptyView() {
        binding.rvUser.setImageEmptyView(R.drawable.empty_state)
        binding.rvUser.setTitleEmptyView(getString(R.string.txt_empty_member))
        binding.rvUser.setContentEmptyView(getString(R.string.txt_empty_member_content))
        binding.rvUser.setEmptyButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
                loadData(1)
            }

        })
    }

    private fun setupErrorView() {
        binding.rvUser.setImageErrorView(R.drawable.empty_state)
        binding.rvUser.setTitleErrorView(getString(R.string.txt_error_no_internet))
        binding.rvUser.setContentErrorView(getString(R.string.txt_error_connection))
        binding.rvUser.setErrorButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
                loadData(1)
            }

        })
    }

    private fun setupList() {
        binding.rvUser.apply {
            setUpAsList()
            setAdapter(userAdapter)
            enableSwipeRefresh(false)
            setLoadingListener(object : EndlessScrollCallback {
                override fun loadMore() {
                    currentPage++
                    loadData(currentPage)
                }
            })
        }
        userAdapter?.setOnActionListener(this)
        binding.rvUser.showShimmer()

    }

    override fun onClicked(view: SingleMemberItemView?) {
        view?.getData()?.let {
            val result = Intent()
            result.putExtra("username", it.firstName.toString() + " " + it.lastName.toString())
            setResult(RESULT_OK, result)
            finish()
            showToast(it.firstName.toString() + " selected")
        }
    }

    override fun onMemberCacheLoaded(members: RealmResults<User>?) {
        members.let {
            if (members?.isNotEmpty()!!) {
                setData(members)
            }
        }
        finishLoad(binding.rvUser)
        loadData(currentPage)

    }

    override fun onMemberLoaded(members: List<User>?) {
        members.let {
            if (members?.isNotEmpty()!!) {
                setData(members)
            }
        }
        finishLoad(binding.rvUser)
    }

    override fun onMemberEmpty() {
        showEmpty()
        binding.rvUser.setLastPage()
    }

    override fun onFailed(error: Any?) {
        error?.let { ErrorCodeHelper.getErrorMessage(this, it)?.let { msg -> showToast(msg) } }
        showError()
    }

    private fun setData(data: List<User>?) {
        data?.let {
            if (currentPage == 1) {
                userAdapter.let {
                    userAdapter?.clear()
                }
            }
            userAdapter?.add(it)
        }
        binding.rvUser.stopShimmer()
        binding.rvUser.showRecycler()
    }

    private fun showEmpty() {
        finishLoad(binding.rvUser)
        binding.rvUser.showEmpty()
    }

    private fun showError() {
        finishLoad(binding.rvUser)
        binding.rvUser.showError()
    }
}