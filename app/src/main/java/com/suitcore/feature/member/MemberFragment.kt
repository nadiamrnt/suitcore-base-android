package com.suitcore.feature.member

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.suitcore.R
import com.suitcore.base.ui.BaseFragment
import com.suitcore.base.ui.recyclerview.BaseRecyclerView
import com.suitcore.data.model.ErrorCodeHelper
import com.suitcore.data.model.User
import com.suitcore.databinding.FragmentMemberBinding
import com.suitcore.feature.chooseuser.ChooseUserActivity
import io.realm.RealmResults

/**
 * Created by DODYDMW19 on 1/30/2018.
 */

class MemberFragment : BaseFragment<FragmentMemberBinding>(),
    MemberView, SingleMemberItemView.OnActionListener{

    private var memberPresenter: MemberPresenter? = null
    private var currentPage: Int = 1
    private var memberAdapter: MemberAdapter? = null

    companion object {
        fun newInstance(): Fragment {
            return MemberFragment()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMemberBinding = FragmentMemberBinding.inflate(inflater, container, false)

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
        memberPresenter = MemberPresenter()
        memberPresenter?.attachView(this)
        memberPresenter?.getMemberCache()
    }

    private fun setupList() {
        memberAdapter = context?.let { MemberAdapter(it) }
        binding.rvMember.apply {
            setUpAsList()
            setAdapter(memberAdapter)
            /* Disable pull to refresh & pagination */
            enableSwipeRefresh(false)
//            setSwipeRefreshLoadingListener {
//                currentPage = 1
//                loadData(currentPage)
//            }
//            setLoadingListener(object : EndlessScrollCallback {
//                override fun loadMore() {
//                    currentPage++
//                    loadData(currentPage)
//                }
//            })
        }
        memberAdapter?.setOnActionListener(this)
        binding.rvMember.showShimmer()

    }

    private fun loadData(page: Int) {
        memberPresenter?.getMemberWithCoroutines(page)
    }

    private fun setData(data: List<User>?) {
        data?.let {
            if (currentPage == 1) {
                memberAdapter.let {
                    memberAdapter?.clear()
                }
            }
            memberAdapter?.add(it)
        }
        binding.rvMember.stopShimmer()
        binding.rvMember.showRecycler()
    }

    private fun setupProgressView() {
        R.layout.layout_shimmer_member.apply {
            binding.rvMember.baseShimmerBinding.viewStub.layoutResource = this
        }

        binding.rvMember.baseShimmerBinding.viewStub.inflate()
    }

    private fun setupEmptyView() {
        binding.rvMember.setImageEmptyView(R.drawable.empty_state)
        binding.rvMember.setTitleEmptyView(getString(R.string.txt_empty_member))
        binding.rvMember.setContentEmptyView(getString(R.string.txt_empty_member_content))
        binding.rvMember.setEmptyButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
                loadData(1)
            }

        })
    }

    private fun setupErrorView() {
        binding.rvMember.setImageErrorView(R.drawable.empty_state)
        binding.rvMember.setTitleErrorView(getString(R.string.txt_error_no_internet))
        binding.rvMember.setContentErrorView(getString(R.string.txt_error_connection))
        binding.rvMember.setErrorButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
                loadData(1)
            }

        })
    }

    private fun showError() {
        finishLoad(binding.rvMember)
        binding.rvMember.showError()
    }

    private fun showEmpty() {
        finishLoad(binding.rvMember)
        binding.rvMember.showEmpty()
    }

    override fun onMemberCacheLoaded(members: RealmResults<User>?) {
        members.let {
            if (members?.isNotEmpty()!!) {
                setData(members)
            }
        }
        finishLoad(binding.rvMember)
        loadData(currentPage)
    }

    override fun onMemberLoaded(members: List<User>?) {
        members.let {
            if (members?.isNotEmpty()!!) {
                setData(members)
            }
        }
        finishLoad(binding.rvMember)
    }

    override fun onMemberEmpty() {
        showEmpty()
        binding.rvMember.setLastPage()
    }

    override fun onFailed(error: Any?) {
        error?.let { ErrorCodeHelper.getErrorMessage(context, it)?.let { msg -> showToast(msg) } }
        showError()
    }

    override fun onClicked(view: SingleMemberItemView?) {
        view?.getData()?.let {
            showToast(it.firstName.toString() + " selected")
            val mainActivity: ChooseUserActivity? = activity as ChooseUserActivity?
            val selectedUser = it.firstName.toString() + " " + it.lastName.toString()
            mainActivity?.binding?.tvSelectedUser?.text = selectedUser
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

}