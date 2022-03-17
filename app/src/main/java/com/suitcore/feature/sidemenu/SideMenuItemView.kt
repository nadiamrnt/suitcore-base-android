package com.suitcore.feature.sidemenu

import android.widget.TextView
import com.suitcore.base.ui.adapter.viewholder.BaseItemViewHolder
import com.suitcore.data.model.SideMenu
import com.suitcore.databinding.ItemSideMenuBinding

/**
 * Created by dodydmw19 on 1/3/19.
 */

class SideMenuItemView(var binding: ItemSideMenuBinding) : BaseItemViewHolder<SideMenu>(binding) {

    private var mActionListener: OnActionListener? = null
    private var sideMenu: SideMenu? = null

    override fun bind(data: SideMenu?) {
        data?.let {
            sideMenu = data
            binding.textViewMenuTitle.text = data.label

            binding.linearLayoutBackground.setOnClickListener {
                if (mActionListener != null) {
                    mActionListener?.onClicked(this, adapterPosition - 1)
                }
            }
        }
    }

    fun getTitleView(): TextView {
        return binding.textViewMenuTitle
    }

    fun getData() : SideMenu?{
        return sideMenu
    }

    fun setOnActionListener(listener: OnActionListener) {
        mActionListener = listener
    }

    interface OnActionListener {
        fun onClicked(view: SideMenuItemView, position: Int)
    }

}