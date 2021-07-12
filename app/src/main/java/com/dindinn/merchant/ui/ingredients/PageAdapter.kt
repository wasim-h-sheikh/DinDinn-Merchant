package com.dindinn.merchant.ui.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dindinn.merchant.model.Category
import com.dindinn.merchant.ui.ingredients.list.IngredientListFragment
import com.dindinn.merchant.util.ARG_CATEGORY_ID

class PageAdapter(fa: FragmentActivity, private val categories: List<Category?>?) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = categories?.size!!

    override fun createFragment(position: Int): Fragment {
        val fragment = IngredientListFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_CATEGORY_ID, categories?.get(position)?.id!!)
        }
        return fragment
    }


}