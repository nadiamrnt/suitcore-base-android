package com.suitcore.base.presenter

import androidx.annotation.StringRes

interface MvpView {

    fun showDialogLoading(dismiss: Boolean = false, message: String?)
    fun showDialogAlert(title: String?, message: String?, confirmCallback: () -> Unit?={}, drawableImage: Int?=null)
    fun showDialogConfirmation(title: String?, message: String?, confirmCallback: () -> Unit?={}, cancelCallback: ()-> Unit? = {}, drawableImage: Int?=null)
    fun showDialogPopImage(drawableImage: Int?=null)
    fun hideLoading()
}