package com.suitcore.feature.sidemenu

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.suitcore.R
import com.suitcore.base.ui.adapter.BaseRecyclerAdapter
import com.suitcore.data.model.SideMenu
import com.suitcore.databinding.ItemMemberBinding
import com.suitcore.databinding.ItemSideMenuBinding

/**
 * Created by dodydmw19 on 1/3/19.
 */

class SideMenuAdapter(var context: Context?) : BaseRecyclerAdapter<SideMenu, SideMenuItemView>() {

    private lateinit var itemSideMenuBinding: ItemSideMenuBinding
    private var mOnActionListener: SideMenuItemView.OnActionListener? = null

    fun setOnActionListener(onActionListener: SideMenuItemView.OnActionListener) {
        mOnActionListener = onActionListener
    }

    var selectedItem = 0
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SideMenuItemView {
        itemSideMenuBinding =
            ItemSideMenuBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = SideMenuItemView(itemSideMenuBinding)
        mOnActionListener?.let { view.setOnActionListener(it) }
        return view
    }

    override fun onBindViewHolder(holder: SideMenuItemView, position: Int) {
        context?.let { ctx ->
            if (position == selectedItem) {
                holder.getTitleView()
                    .setTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary))
            } else {
                holder.getTitleView().setTextColor(ContextCompat.getColor(ctx, R.color.black))
            }
        }
        super.onBindViewHolder(holder, position)
    }
}