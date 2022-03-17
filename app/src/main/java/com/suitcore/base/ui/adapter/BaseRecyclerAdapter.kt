package com.suitcore.base.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suitcore.base.ui.adapter.viewholder.BaseItemViewHolder
import java.util.*

abstract class BaseRecyclerAdapter<Any, Holder : BaseItemViewHolder<Any>> : RecyclerView.Adapter<Holder>() {

    protected var mData: ArrayList<Any> = ArrayList()
    private var mItemClickListener: OnItemClickListener? = null
    private var mLongItemClickListener: OnLongItemClickListener? = null

    val data: List<Any>
        get() = mData

    init {
        mData = ArrayList()
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setIsRecyclable(false)
        if (mData[position] != null) {
            holder.bind(mData[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return try {
            mData.size
        } catch (e: Exception) {
            0
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.mItemClickListener = itemClickListener
    }

    interface OnLongItemClickListener {
        fun onLongItemClick(view: View, position: Int)
    }

    fun setOnLongItemClickListener(longItemClickListener: OnLongItemClickListener) {
        this.mLongItemClickListener = longItemClickListener
    }

    fun add(item: Any) {
        mData.add(item)
        notifyItemInserted(mData.size - 1)
    }

    fun addAll(items: List<Any>) {
        add(items)
    }

    fun add(item: Any, position: Int) {
        mData.add(position, item)
        notifyItemInserted(position)
    }

    fun add(items: List<Any>) {
        val size = items.size
        for (i in 0 until size) {
            mData.add(items[i])
        }
        notifyDataSetChanged()
    }

    fun addOrUpdate(item: Any) {
        val i = mData.indexOf(item)
        if (i >= 0) {
            mData[i] = item
            notifyItemChanged(i)
        } else {
            add(item)
        }
    }

    fun addOrUpdate(items: List<Any>) {
        val size = items.size
        for (i in 0 until size) {
            val item = items[i]
            val x = mData.indexOf(item)
            if (x >= 0) {
                mData[x] = item
            } else {
                add(item)
            }
        }
        notifyDataSetChanged()
    }

    fun getData(): ArrayList<Any>? {
        return mData
    }

    fun getData(position: Int): Any {
        return mData[position - 1]
    }

    fun remove(position: Int) {
        if (position >= 0 && position < mData.size) {
            mData.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun remove(item: Any) {
        val position = mData.indexOf(item)
        remove(position)
    }

    fun clear() {
        mData.clear()
        notifyDataSetChanged()
    }
}
