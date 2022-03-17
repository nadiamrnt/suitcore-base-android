package com.suitcore.feature.member

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.suitcore.data.model.User
import com.suitcore.databinding.ItemMemberBinding

/**
 * Created by DODYDMW19 on 1/30/2018.
 */
class MultiTypeMemberItemViewFirst(var binding: ItemMemberBinding) : RecyclerView.ViewHolder(binding.root) {

    private var mActionListener: OnActionListener? = null
    private var user: User? = null

    @SuppressLint("SetTextI18n")
    fun bind(data: User?) {
        data.let {
            // for get context = itemView.context

            this.user = data
            binding.imgMember.setImageURI(data?.avatar)
            binding.txtMemberName.text = data?.firstName + " " + data?.lastName
            binding.button.setOnClickListener {
                mActionListener?.onClicked(this)
            }
        }
    }

    fun getData(): User? {
        return user
    }

    fun setOnActionListener(listener: OnActionListener) {
        mActionListener = listener
    }

    interface OnActionListener {
        fun onClicked(view: MultiTypeMemberItemViewFirst?)
    }
}