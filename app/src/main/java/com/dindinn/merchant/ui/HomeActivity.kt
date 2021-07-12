package com.dindinn.merchant.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.dindinn.merchant.R
import com.dindinn.merchant.databinding.ActivityHomeBinding
import com.dindinn.merchant.model.Category
import com.dindinn.merchant.ui.ingredients.IngredientFragment
import com.dindinn.merchant.ui.ingredients.PageAdapter
import com.dindinn.merchant.util.ORDER_FRAGMENT_TAG
import com.dindinn.merchant.util.manageFragmentTransaction
import com.google.android.material.tabs.TabLayoutMediator
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class HomeActivity : FragmentActivity() , KodeinAware {
    override val kodein by kodein()
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_content)
//        val action = OrderFragmentDirections.actionOrderFragmentSelf()
//        navController.navigate(action)

        supportFragmentManager.manageFragmentTransaction(ORDER_FRAGMENT_TAG)

    }

}