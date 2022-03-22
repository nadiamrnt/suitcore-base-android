package com.suitcore.feature.chooseuser

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.databinding.ActivityChooseUserBinding
import com.suitcore.feature.member.MemberFragment
import com.suitcore.feature.user.UserActivity


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
//            supportFragmentManager.beginTransaction()
//                .add(R.id.choose_user, MemberFragment.newInstance()).commit()
            goToActivity(1, UserActivity::class.java, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            binding.tvSelectedUser.text = data?.getStringExtra("username")
        }
    }

}