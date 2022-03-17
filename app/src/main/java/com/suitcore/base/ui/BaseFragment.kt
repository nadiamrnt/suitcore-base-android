package com.suitcore.base.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.suitcore.base.presenter.MvpView
import com.suitcore.base.ui.dialog.BaseDialog
import com.suitcore.base.ui.recyclerview.BaseRecyclerView
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.suitcore.base.ui.dialog.BaseDialogInterface

abstract class BaseFragment<VB : ViewBinding> : Fragment(), MvpView {

    private var baseActivity: BaseActivity<VB>? = null
    private var _binding: VB? = null
    val binding get() = _binding!!
    private var activityIntent: Intent? = null
    private var baseDialog: BaseDialog? = null
    private var dismissDialog: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onViewReady(savedInstanceState)
    }

    protected abstract fun onViewReady(savedInstanceState: Bundle?)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearActivity() {
        activityIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent?.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        activityIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activityIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    fun goToActivity(
        actDestination: Class<*>,
        data: Bundle?,
        clearIntent: Boolean,
        isFinish: Boolean
    ) {

        activityIntent = Intent(activity, actDestination)

        if (clearIntent) {
            clearActivity()
        }

        data?.let { activityIntent?.putExtras(data) }

        startActivity(activityIntent)

        if (isFinish) {
            activity?.finish()
        }
    }

    fun goToActivity(resultCode: Int, actDestination: Class<*>, data: Bundle?) {
        activityIntent = Intent(activity, actDestination)
        data?.let { activityIntent?.putExtras(data) }
        startActivityForResult(activityIntent, resultCode)
    }

    fun finishLoad(recycler: BaseRecyclerView?) {
        recycler?.let {
            //    it.completeRefresh()
            //it.loadMoreComplete()
            it.getSwipeRefreshLayout().isRefreshing = false
            it.releaseBlock()
            it.stopShimmer()
        }
    }

    //Custom Dialog
    override fun showDialogLoading(dismiss: Boolean, message: String?) {
        dismissDialog = dismiss
        baseDialog = BaseDialog.BuildBaseDialog()
            .onBackPressedDismiss(dismiss)
            .setContent(message)
            .build(requireActivity())
        hideSoftKeyboard()
        baseDialog?.show()
    }

    override fun showDialogAlert(
        title: String?,
        message: String?,
        confirmCallback: () -> Unit?,
        drawableImage: Int?
    ) {
        dismissDialog = false
        baseDialog = BaseDialog.BuildAlertDialog()
            .onBackPressedDismiss(false)
            .setTitle(title)
            .setContent(message)
            .setSubmitButtonText("OK")
            .setImageContent(drawableImage)
            .setListener(object : BaseDialogInterface {
                override fun onSubmitClick() {
                    confirmCallback()
                }

                override fun onDismissClick() {

                }
            })
            .build(requireActivity())
        hideSoftKeyboard()
        baseDialog?.show()

    }

    override fun showDialogConfirmation(
        title: String?,
        message: String?,
        confirmCallback: () -> Unit?,
        cancelCallback: () -> Unit?,
        drawableImage: Int?
    ) {
        dismissDialog = false
        baseDialog = BaseDialog.BuildConfirmationDialog()
            .onBackPressedDismiss(dismissDialog)
            .setTitle(title)
            .setContent(message)
            .setImageContent(drawableImage)
            .setSubmitButtonText("OK")
            .setCancelButtonText("Cancel")
            .setSingleButton(false)
            .setListener(object : BaseDialogInterface {
                override fun onSubmitClick() {
                    confirmCallback()
                }

                override fun onDismissClick() {
                    cancelCallback()
                }
            })
            .build(requireActivity())
        hideSoftKeyboard()
        baseDialog?.show()
    }

    override fun showDialogPopImage(drawableImage: Int?) {
        dismissDialog = false
        baseDialog = BaseDialog.BuildAlertDialog()
            .onBackPressedDismiss(dismissDialog)
            .hideAllButton(true)
            .showPanelButton(true)
            .setImageContent(drawableImage)
            .build(requireActivity())
        hideSoftKeyboard()
        baseDialog?.show()
    }

    private fun Activity.hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager =
                ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun hideLoading() {
        if (baseDialog != null) {
            if (baseDialog?.isShowing()!!) {
                baseDialog?.dismissDialog()
            }
        }
    }

    private fun hideSoftKeyboard() {
        activity?.currentFocus?.let {
            val inputMethodManager =
                ContextCompat.getSystemService(requireActivity(), InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

}

