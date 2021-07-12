package com.dindinn.merchant.ui.ingredients.list

import android.view.View
import com.dindinn.merchant.R
import com.dindinn.merchant.databinding.ItemIngredientBinding
import com.dindinn.merchant.model.Ingredient
import com.dindinn.merchant.util.loadImageFromUrl
import com.dindinn.merchant.util.setTextColorFromResource
import com.xwray.groupie.viewbinding.BindableItem

class IngredientItem(
    private val ingredient: Ingredient?
) : BindableItem<ItemIngredientBinding>() {

    override fun getLayout() = R.layout.item_ingredient

    override fun bind(viewBinding: ItemIngredientBinding, position: Int) {
        viewBinding.textViewTitle.text = ingredient?.title
        viewBinding.textViewAvailableQty.text = ingredient?.available.toString()
        if (ingredient?.available!! <= 5) {
            viewBinding.layoutAvailable.setBackgroundResource(R.drawable.bg_available_low)
            viewBinding.textViewAvailableQty.setTextColorFromResource(R.color.color_red_light)
        } else {
            viewBinding.layoutAvailable.setBackgroundResource(R.drawable.bg_available)
            viewBinding.textViewAvailableQty.setTextColorFromResource(R.color.color_text_primary)
        }
        ingredient.thumbnail?.let { viewBinding.imageViewBanner.loadImageFromUrl(it) }
    }


    override fun initializeViewBinding(view: View): ItemIngredientBinding =
        ItemIngredientBinding.bind(view)


}