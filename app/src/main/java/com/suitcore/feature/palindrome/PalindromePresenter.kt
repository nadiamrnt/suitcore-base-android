package com.suitcore.feature.palindrome

import com.suitcore.base.presenter.BasePresenter

class PalindromePresenter : BasePresenter<PalindromeView> {
    private var mvpView: PalindromeView? = null

    fun isPalindrome(text: String): Boolean {
        val reverseString = text.reversed()
        return text.equals(reverseString, ignoreCase = true)
    }

    override fun onDestroy() {}

    override fun attachView(view: PalindromeView) {
        mvpView = view
    }

    override fun detachView() {
        mvpView = null
    }
}