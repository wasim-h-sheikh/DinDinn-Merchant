package com.dindinn.merchant.ui.orders

import android.view.View
import com.dindinn.merchant.R
import com.dindinn.merchant.databinding.ItemAddonBinding
import com.dindinn.merchant.model.Addon
import com.xwray.groupie.viewbinding.BindableItem

class AddonItem(
    private val addon: Addon?,
) : BindableItem<ItemAddonBinding>() {

    override fun getLayout() = R.layout.item_addon

    override fun bind(viewBinding: ItemAddonBinding, position: Int) {
        viewBinding.textViewTitle.text = addon?.title
        viewBinding.textViewQty.text = addon?.quantity.toString()
    }


    override fun initializeViewBinding(view: View): ItemAddonBinding = ItemAddonBinding.bind(view)


}