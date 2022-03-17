package com.suitcore.feature.palindrome

import android.content.Intent
import android.os.Bundle
import com.suitcore.base.ui.BaseActivity
import com.suitcore.databinding.ActivityPalindromeBinding
import com.suitcore.feature.chooseuser.ChooseUserActivity

class PalindromeActivity : BaseActivity<ActivityPalindromeBinding>(), PalindromeView {

    private var palindromePresenter: PalindromePresenter? = null

    override fun getViewBinding(): ActivityPalindromeBinding =
        ActivityPalindromeBinding.inflate(layoutInflater)

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupPresenter()
        onClick()
    }

    private fun onClick() {
        binding.btnCheck.setOnClickListener {
            if (binding.etPalindrome.text.isEmpty()) {
                onEmptyText("Input Palindrome Text First!")
            } else {
                val text = binding.etPalindrome.text.toString()
                if (palindromePresenter!!.isPalindrome(text)) {
                    onPalindromeChecked("It\'s a Palindrome")
                } else {
                    onPalindromeChecked("It\'s not a Palindrome")
                }
            }
        }

        binding.btnNext.setOnClickListener {
            if (binding.etName.text.isEmpty()) {
                onEmptyText("Input Your Name First!")
            } else {
                val intent = Intent(this@PalindromeActivity, ChooseUserActivity::class.java)
                val name = binding.etName.text.toString()
                intent.putExtra("name", name)
                startActivity(intent)
            }
        }
    }

    private fun setupPresenter() {
        palindromePresenter = PalindromePresenter()
        palindromePresenter?.attachView(this)
    }

    override fun onPalindromeChecked(message: String) {
        showToast(message)
    }

    override fun onEmptyText(message: String) {
        showToast(message)
    }

}