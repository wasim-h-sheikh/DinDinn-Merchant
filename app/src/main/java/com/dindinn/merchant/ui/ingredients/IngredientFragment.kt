package com.dindinn.merchant.ui.ingredients

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dindinn.merchant.databinding.IngredientFragmentBinding
import com.dindinn.merchant.model.Category
import com.dindinn.merchant.ui.ingredients.list.IngredientListFragment
import com.dindinn.merchant.util.ORDER_FRAGMENT_TAG
import com.dindinn.merchant.util.manageFragmentTransaction
import com.dindinn.merchant.util.show
import com.google.android.material.tabs.TabLayoutMediator
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class IngredientFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private lateinit var binding: IngredientFragmentBinding
    private val factory: IngredientModelFactory by instance()
    private lateinit var viewModel: IngredientViewModel
    private lateinit var pageAdapter: PageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = IngredientFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(IngredientViewModel::class.java)
        bindUI()
    }

    private fun bindUI() {
        setupSearch()
        binding.imageViewBack.setOnClickListener {
            requireActivity().supportFragmentManager.manageFragmentTransaction(
                ORDER_FRAGMENT_TAG
            )

        }
        binding.progressBar.show()
        observableViewModel()
    }

    private fun setupSearch() {
        binding.editTextSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                filterIngredients(binding.editTextSearch.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    filterIngredients("")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun initViewPager(categories: List<Category?>?) {

        pageAdapter = PageAdapter(requireActivity(), categories)
        binding.viewPager.adapter = pageAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = categories?.get(position)?.title
        }.attach()
    }

    private fun filterIngredients(query: String) {
        val currentFragment =
            requireActivity().supportFragmentManager.findFragmentByTag("f" + binding.viewPager.currentItem)
        (currentFragment as IngredientListFragment).searchIngredients(query)
    }

    private fun observableViewModel() {
        viewModel.getCategories().observe(requireActivity(),
            { categories: List<Category?>? ->
                if (categories != null) {
                    binding.viewPager.visibility = View.VISIBLE
                    initViewPager(categories)
                }
            })

        viewModel.getError().observe(requireActivity(),
            { isError: Boolean? ->
                if (isError != null) if (isError) {
                    binding.tvError.visibility = View.VISIBLE
                    binding.viewPager.visibility = View.GONE
                    binding.tvError.text = "An Error Occurred While Loading Data!"
                } else {
                    binding.tvError.visibility = View.GONE
                    binding.tvError.text = null
                }
            })

        viewModel.getLoading().observe(requireActivity(),
            { isLoading: Boolean? ->
                if (isLoading != null) {
                    binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    if (isLoading) {
                        binding.tvError.visibility = View.GONE
                        binding.viewPager.visibility = View.GONE
                    }
                }
            })
    }


}