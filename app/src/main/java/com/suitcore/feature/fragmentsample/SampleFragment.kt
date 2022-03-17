package com.suitcore.feature.fragmentsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.suitcore.R
import com.suitcore.base.ui.BaseFragment
import com.suitcore.data.local.prefs.DataConstant
import com.suitcore.data.local.prefs.SuitPreferences
import com.suitcore.databinding.FragmentTestBinding
import com.suitcore.feature.login.LoginActivity
import com.suitcore.helper.localization.LanguageHelper

/**
 * Created by dodydmw19 on 7/30/18.
 */

class SampleFragment : BaseFragment<FragmentTestBinding>() {

    companion object {
        fun newInstance(): Fragment {
            return SampleFragment()
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTestBinding = FragmentTestBinding.inflate(inflater, container, false)

    override fun onViewReady(savedInstanceState: Bundle?) {
        actionClicked()
    }

    private fun actionClicked() {
        binding.relNewAlertDialog.setOnClickListener {
            showDialogAlert(title = null, message = "New Alert")
        }

        binding.relNewImageAlertDialog.setOnClickListener {
            showDialogAlert(
                title = null,
                message = "New Alert Image",
                drawableImage = R.drawable.ic_marker_normal
            )
        }

        binding.relNewConfirmDialog.setOnClickListener {
            showDialogConfirmation(
                title = null,
                message = "New Confirmation Without Image",
                confirmCallback = {
                    showDialogLoading(true, null)
                })

        }

        binding.relNewConfirmImageDialog.setOnClickListener {
            showDialogConfirmation(
                title = null,
                message = "New Confirmation With Image",
                drawableImage = R.drawable.ic_marker_normal,
                confirmCallback = {
                    showDialogLoading(true, getString(R.string.txt_loading_with_info))
                })
        }

        binding.relNewContentDialog.setOnClickListener {
            showDialogPopImage(R.drawable.ic_marker_normal)
        }

        binding.relChangeLanguage.setOnClickListener {
            showDialogConfirmation(
                title = "Change Language",
                message = "Do you want to change current language ?",
                confirmCallback = {
                    if(SuitPreferences.instance()?.getString(DataConstant.CURRENT_LANG) == "en"){
                        LanguageHelper.setNewLocale(requireContext(), LanguageHelper.mIndonesianFlag)
                        goToActivity(LoginActivity::class.java, null, clearIntent = true, isFinish = true)
                    }else{
                        LanguageHelper.setNewLocale(requireContext(), LanguageHelper.mEnglishFlag)
                        goToActivity(LoginActivity::class.java, null, clearIntent = true, isFinish = true)
                    }
                })
        }
    }

}