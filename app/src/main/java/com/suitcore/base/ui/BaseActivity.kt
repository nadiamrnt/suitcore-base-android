package com.suitcore.base.ui

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.suitcore.base.presenter.MvpView
import com.suitcore.base.ui.dialog.BaseDialog
import com.suitcore.base.ui.dialog.BaseDialogInterface
import com.suitcore.base.ui.recyclerview.BaseRecyclerView
import com.suitcore.helper.localization.LanguageHelper


abstract class BaseActivity<VB : ViewBinding>: AppCompatActivity(), MvpView {
    private var _binding: VB? = null
    val binding get() = _binding!!

    private var toolBar: Toolbar? = null
    private var mActionBar: ActionBar? = null
    private var activityIntent: Intent? = null
    private var baseDialog: BaseDialog? = null
    private var dismissDialog:Boolean = false

    private val baseFragmentManager: FragmentManager
        get() = super.getSupportFragmentManager()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LanguageHelper.setLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(binding.root)
        onViewReady(savedInstanceState)
    }

    abstract fun getViewBinding(): VB

    protected fun setupToolbar(baseToolbar: Toolbar, needHomeButton: Boolean) {
        baseToolbar.title = ""
        setupToolbar(baseToolbar, needHomeButton, null)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupToolbar(baseToolbar: Toolbar, needHomeButton: Boolean, onClickListener: View.OnClickListener?) {

        toolBar = baseToolbar
        setSupportActionBar(baseToolbar)
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(needHomeButton)
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }

        if (onClickListener != null)
            baseToolbar.setNavigationOnClickListener(onClickListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setTitle(title: Int) {
        super.setTitle(title)
        if (mActionBar != null)
            mActionBar?.title = getString(title)
    }

    fun changeProgressBarColor(color: Int, progressBar: ProgressBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            progressBar.indeterminateDrawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            progressBar.indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    fun getActivity() : Activity{
        return this
    }

    private fun clearActivity() {
        activityIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent?.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        activityIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activityIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    fun goToActivity(actDestination: Class<*>, data: Bundle?, clearIntent: Boolean, isFinish: Boolean) {

        activityIntent = Intent(this, actDestination)

        if (clearIntent) {
            clearActivity()
        }

        data?.let { activityIntent?.putExtras(data) }

        startActivity(activityIntent)

        if (isFinish) {
            finish()
        }
    }

    fun goToActivity(resultCode: Int, actDestination:  Class<*>, data: Bundle?) {
        activityIntent = Intent(this, actDestination)

        data?.let { activityIntent?.putExtras(data) }

        startActivityForResult(activityIntent, resultCode)

    }

    override fun onBackPressed() {
        if (baseFragmentManager.backStackEntryCount > 0) {
            baseFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected abstract fun onViewReady(savedInstanceState: Bundle?)

    override fun showDialogLoading(dismiss: Boolean, message: String?) {
        dismissDialog = dismiss
        baseDialog = BaseDialog.BuildBaseDialog()
                .onBackPressedDismiss(dismiss)
                .setContent(message)
                .build(this)
        hideSoftKeyboard()
        baseDialog?.show()
    }

    override fun showDialogAlert(title: String?, message: String?, confirmCallback: () -> Unit?, drawableImage: Int?){
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
                .build(this)
        hideSoftKeyboard()
        baseDialog?.show()

    }

    override fun showDialogConfirmation(title: String?, message: String?, confirmCallback: () -> Unit?, cancelCallback: ()-> Unit?, drawableImage: Int?){
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
                .build(this)
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
                .build(this)
        hideSoftKeyboard()
        baseDialog?.show()
    }

    private fun Activity.hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun hideLoading() {
        if(baseDialog!=null){
            if(baseDialog?.isShowing()!!){
                baseDialog?.dismissDialog()
            }
        }
    }

    fun finishLoad(recycler: BaseRecyclerView?) {
        recycler?.let {
            it.stopShimmer()
        }
    }


}