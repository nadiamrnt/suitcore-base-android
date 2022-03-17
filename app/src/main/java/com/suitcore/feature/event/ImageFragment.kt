package com.suitcore.feature.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.suitcore.base.ui.BaseFragment
import com.suitcore.data.model.Event
import com.suitcore.databinding.ItemEventBinding


class ImageFragment : BaseFragment<ItemEventBinding>() {

    var data: Event? = null

    companion object {
        fun newInstance(data: Event): Fragment {
            val fragment = ImageFragment()
            fragment.data = data
            return fragment
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): ItemEventBinding =
        ItemEventBinding.inflate(inflater, container, false)

    override fun onViewReady(savedInstanceState: Bundle?) {
        binding.imgItemEvent.setImageURI(data?.imgUrl)
        binding.tvItemEventName.text = data?.name
    }

}