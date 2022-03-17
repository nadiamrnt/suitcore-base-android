package com.suitcore.feature.chooseuser

import android.os.Bundle
import android.widget.Toast
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.databinding.ActivityChooseUserBinding
import com.suitcore.feature.member.MemberFragment


class ChooseUserActivity : BaseActivity<ActivityChooseUserBinding>() {

    private var chooseUserPresenter: ChooseUserPresenter? = null

    override fun getViewBinding(): ActivityChooseUserBinding =
        ActivityChooseUserBinding.inflate(layoutInflater)

    override fun onViewReady(savedInstanceState: Bundle?) {
        getName()
        onClick()
    }

    private fun getName() {
        val name: String? = intent.getStringExtra("name")
        binding.tvName.text = name.toString()
    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnChoose.setOnClickListener{
            supportFragmentManager.beginTransaction()
                .add(R.id.choose_user, MemberFragment.newInstance()).commit()
        }
    }

}