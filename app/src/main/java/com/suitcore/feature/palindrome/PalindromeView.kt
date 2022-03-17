package com.suitcore.feature.palindrome

import com.suitcore.base.presenter.MvpView

interface PalindromeView : MvpView {

    fun onPalindromeChecked(message: String)

    fun onEmptyText(message: String)

}