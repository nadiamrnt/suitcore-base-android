package com.suitcore.feature.member

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suitcore.base.ui.adapter.BaseRecyclerMultiTypeAdapter
import com.suitcore.data.model.User
import com.suitcore.databinding.ItemMember2Binding
import com.suitcore.databinding.ItemMemberBinding

/**
 * Created by @dodydmw19 on 28, January, 2021
 */

class MemberMultiTypeAdapter(var context: Context?) : BaseRecyclerMultiTypeAdapter<Any, RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_1 = 0
        private const val TYPE_2 = 1
    }

    private lateinit var itemMemberBinding: ItemMemberBinding
    private lateinit var itemMember2rBinding: ItemMember2Binding

    private var mOnActionListener: MultiTypeMemberItemViewFirst.OnActionListener? = null
    private var mOnActionListener2: MultiTypeMemberItemViewSecond.OnActionListener? = null

    fun setOnActionListener(onActionListener: MultiTypeMemberItemViewFirst.OnActionListener) {
        mOnActionListener = onActionListener
    }

    fun setOnActionListener2(onActionListener: MultiTypeMemberItemViewSecond.OnActionListener) {
        mOnActionListener2 = onActionListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_1 -> {
                itemMemberBinding = ItemMemberBinding.inflate(LayoutInflater.from(context) , parent,false)
                val view = MultiTypeMemberItemViewFirst(itemMemberBinding)
                mOnActionListener?.let { view.setOnActionListener(it) }
                view
            }
            TYPE_2 -> {
                itemMember2rBinding = ItemMember2Binding.inflate(LayoutInflater.from(context) , parent,false)
                val view = MultiTypeMemberItemViewSecond(itemMember2rBinding)
                mOnActionListener2?.let { view.setOnActionListener(it) }
                view
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val element = data[position]
        when (holder) {
            is MultiTypeMemberItemViewFirst -> holder.bind(element as User)
            is MultiTypeMemberItemViewSecond -> holder.bind(element as User)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position % 2 == 0){
            TYPE_1
        }else{
            TYPE_2
        }
    }

}