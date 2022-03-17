package com.suitcore.base.ui.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.suitcore.R
import com.suitcore.databinding.*

class BaseRecyclerView : FrameLayout {

    private lateinit var mRecyclerView: FrameLayout
    private lateinit var mEmptyView: FrameLayout
    private lateinit var mErrorView: FrameLayout
    private lateinit var mShimmerContainer: ShimmerFrameLayout

    private var mClipToPadding: Boolean = false
    private var mPadding: Int = 0
    private var mPaddingTop: Int = 0
    private var mPaddingBottom: Int = 0
    private var mPaddingLeft: Int = 0
    private var mPaddingRight: Int = 0
    private var mScrollbarStyle: Int = 0
    private var mScrollbar: Int = 0
    lateinit var baseEmptyBinding: LayoutBaseEmptyViewBinding
    lateinit var baseErrorBinding: LayoutBaseErrorBinding
    lateinit var baseShimmerBinding: LayoutBaseShimmerBinding
    lateinit var baseRecyclerBinding: LayoutBaseRecyclerviewBinding

    interface ReloadListener : OnClickListener

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(attrs)
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initAttrs(attrs)
        initView()
    }

    private fun initAttrs(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BaseRecyclerView)
        try {
            mClipToPadding = a.getBoolean(R.styleable.BaseRecyclerView_recyclerClipToPadding, false)
            mPadding = a.getDimension(R.styleable.BaseRecyclerView_recyclerPadding, -1.0f).toInt()
            mPaddingLeft =
                a.getDimension(R.styleable.BaseRecyclerView_recyclerPaddingLeft, 0.0f).toInt()
            mPaddingTop =
                a.getDimension(R.styleable.BaseRecyclerView_recyclerPaddingTop, 0.0f).toInt()
            mPaddingRight =
                a.getDimension(R.styleable.BaseRecyclerView_recyclerPaddingRight, 0.0f).toInt()
            mPaddingBottom =
                a.getDimension(R.styleable.BaseRecyclerView_recyclerPaddingBottom, 0.0f).toInt()
            mScrollbar = a.getInteger(R.styleable.BaseRecyclerView_scrollbars, -1)
            mScrollbarStyle = a.getInteger(R.styleable.BaseRecyclerView_scrollbarStyle, -1)
        } finally {
            a.recycle()
        }
    }

    private fun initView() {
        buildViews()

        // Configure the recycler view
        mRecyclerView.apply {
            clipToPadding = mClipToPadding
            if (mPadding != (-1.0f).toInt())
                setPadding(mPadding, mPadding, mPadding, mPadding)
            else
                setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
            when (mScrollbar) {
                0 -> isVerticalScrollBarEnabled = false
                1 -> isHorizontalScrollBarEnabled = false
                2 -> {
                    isVerticalScrollBarEnabled = false
                    isHorizontalScrollBarEnabled = false
                }
            }
            if (mScrollbarStyle != -1) scrollBarStyle = mScrollbarStyle
        }
    }

    private fun buildViews() {
        mRecyclerView = FrameLayout(context)
        mEmptyView = FrameLayout(context)
        mErrorView = FrameLayout(context)
        mShimmerContainer = ShimmerFrameLayout(context)

        baseEmptyBinding =
            LayoutBaseEmptyViewBinding.inflate(LayoutInflater.from(context), mEmptyView, false)
        baseErrorBinding =
            LayoutBaseErrorBinding.inflate(LayoutInflater.from(context), mErrorView, false)
        baseShimmerBinding =
            LayoutBaseShimmerBinding.inflate(LayoutInflater.from(context), mShimmerContainer, false)
        baseRecyclerBinding = LayoutBaseRecyclerviewBinding.inflate(
            LayoutInflater.from(context),
            mRecyclerView,
            false
        )

        addView(baseEmptyBinding.root)
        addView(baseErrorBinding.root)
        addView(baseShimmerBinding.root)
        addView(baseRecyclerBinding.root)
    }

    fun setUpAsList() {
        baseRecyclerBinding.recyclerView.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun setUpAsListInScroll() {
        baseRecyclerBinding.recyclerView.apply {
            setHasFixedSize(false)
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
    }

    fun setUpAsGrid(spanCount: Int) {
        baseRecyclerBinding.recyclerView.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = GridLayoutManager(context, spanCount)
        }
    }

    fun setUpAsGridInScroll(spanCount: Int) {
        baseRecyclerBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = object : GridLayoutManager(context, spanCount) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
    }

    /**
     * Below are some methods for setting the RecyclerView attributes
     */

    fun enableSwipeRefresh(value: Boolean){
        baseRecyclerBinding.swipeRefresh.isRefreshing = value
        baseRecyclerBinding.swipeRefresh.isEnabled = value
    }

    fun getSwipeRefreshLayout(): SwipeRefreshLayout {
        return baseRecyclerBinding.swipeRefresh
    }

    fun setSwipeRefreshLoadingListener(listener: SwipeRefreshLayout.OnRefreshListener) {
        baseRecyclerBinding.swipeRefresh.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.white
        )
        baseRecyclerBinding.swipeRefresh.setOnRefreshListener(listener)
    }

    fun releaseBlock() {
        baseRecyclerBinding.recyclerView.releaseBlock()
    }

    fun setLastPage() {
        baseRecyclerBinding.recyclerView.setLastPage()
    }

    fun setRecyclerViewPadding(left: Int, top: Int, right: Int, bottom: Int) {
        this.mPaddingLeft = left
        this.mPaddingTop = top
        this.mPaddingRight = right
        this.mPaddingBottom = bottom
        mRecyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
    }

    fun setRecyclerClipToPadding(clipToPadding: Boolean) {
        mClipToPadding = clipToPadding.also {
            mRecyclerView.clipToPadding = it
        }
    }

    fun setHasFixedSize(hasFixedSize: Boolean) {
        baseRecyclerBinding.recyclerView.setHasFixedSize(hasFixedSize)
    }

    fun setItemAnimator(animator: RecyclerView.ItemAnimator) {
        baseRecyclerBinding.recyclerView.itemAnimator = animator
    }

    fun addItemDecoration(decor: RecyclerView.ItemDecoration) {
        baseRecyclerBinding.recyclerView.addItemDecoration(decor)
    }

    fun addItemDecoration(decor: RecyclerView.ItemDecoration, index: Int) {
        baseRecyclerBinding.recyclerView.addItemDecoration(decor, index)
    }

    fun removeItemDecoration(decor: RecyclerView.ItemDecoration) {
        baseRecyclerBinding.recyclerView.removeItemDecoration(decor)
    }

    fun getAdapter(): RecyclerView.Adapter<*>? {
        return baseRecyclerBinding.recyclerView.adapter
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        baseRecyclerBinding.recyclerView.adapter = adapter
    }

    fun setLayoutManager(layout: RecyclerView.LayoutManager) {
        baseRecyclerBinding.recyclerView.layoutManager = layout
    }

    fun setLoadingListener(listener: EndlessScrollCallback) {
        baseRecyclerBinding.recyclerView.setEndlessScrollCallback(listener)
    }

    fun scrollToPosition(position: Int) {
        baseRecyclerBinding.recyclerView.scrollToPosition(position)
    }

    /**
     * Below are some methods for managing layout visibility
     */
    private fun hideAll() {
        baseRecyclerBinding.contentRecyclerView.visibility = View.GONE
        baseEmptyBinding.contentEmpty.visibility = View.GONE
        baseErrorBinding.contentError.visibility = View.GONE
        baseShimmerBinding.contentShimmer.visibility = View.GONE
        mShimmerContainer.stopShimmer()
    }

    fun showRecycler() {
        hideAll()
        baseRecyclerBinding.contentRecyclerView.visibility = View.VISIBLE
    }

    fun showShimmer() {
        hideAll()
        baseShimmerBinding.contentShimmer.visibility = View.VISIBLE
    }

    fun stopShimmer() {
        if (mShimmerContainer.isShimmerStarted) {
            mShimmerContainer.stopShimmer()
            baseShimmerBinding.contentShimmer.visibility = View.GONE
        }
    }

    fun showEmpty() {
        hideAll()
        baseEmptyBinding.contentEmpty.visibility = View.VISIBLE
    }

    fun hideEmpty() {
        baseEmptyBinding.contentEmpty.visibility = View.GONE
        showRecycler()
    }

    fun showError() {
        hideAll()
        baseErrorBinding.contentError.visibility = View.VISIBLE
    }

    fun initialShimmer() {
        hideEmpty()
        showShimmer()
    }

    fun finishShimmer() {
        stopShimmer()
        showRecycler()
    }

    /**
     * Below are some methods for managing empty state & error state
     */

    fun setImageEmptyView(imageRes: Int) {
        baseEmptyBinding.imgEmptyView.setImageResource(imageRes)
    }

    fun setTitleEmptyView(text: String) {
        baseEmptyBinding.tvTitleEmptyView.text = text
    }

    fun setContentEmptyView(text: String) {
        baseEmptyBinding.tvContentEmptyView.text = text
    }

    fun showEmptyTitleView(status: Boolean) {
        when (status) {
            true -> baseEmptyBinding.tvTitleEmptyView.visibility = View.VISIBLE
            false -> baseEmptyBinding.tvTitleEmptyView.visibility = View.GONE
        }
    }

    fun setTextButtonEmptyView(text: String) {
        baseEmptyBinding.btnEmpty.text = text
    }

    fun setBackgroungEmptyButton(drawableRes: Int) {
        baseEmptyBinding.btnEmpty.setBackgroundResource(drawableRes)
    }

    fun setEmptyButtonListener(listener: ReloadListener) {
        baseEmptyBinding.btnEmpty.setOnClickListener(listener)
    }

    fun setImageErrorView(imageRes: Int) {
        baseErrorBinding.imgErrorView.setImageResource(imageRes)
    }

    fun setTitleErrorView(text: String) {
        baseErrorBinding.tvTitleErrorView.text = text
    }

    fun setContentErrorView(text: String) {
        baseErrorBinding.tvContentErrorView.text = text
    }

    fun showErrorTitleView(status: Boolean) {
        when (status) {
            true -> baseErrorBinding.tvTitleErrorView.visibility = View.VISIBLE
            false -> baseErrorBinding.tvTitleErrorView.visibility = View.GONE
        }
    }

    fun setTextButtonErrorView(text: String) {
        baseErrorBinding.btnError.text = text
    }

    fun setBackgroungErrorButton(drawableRes: Int) {
        baseErrorBinding.btnError.setBackgroundResource(drawableRes)
    }

    fun setErrorButtonListener(listener: ReloadListener) {
        baseErrorBinding.btnError.setOnClickListener(listener)
    }

}
