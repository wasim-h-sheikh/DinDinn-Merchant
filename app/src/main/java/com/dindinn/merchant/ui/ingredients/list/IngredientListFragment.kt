package com.dindinn.merchant.ui.ingredients.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dindinn.merchant.databinding.IngredientListFragmentBinding
import com.dindinn.merchant.model.Ingredient
import com.dindinn.merchant.util.ARG_CATEGORY_ID
import com.dindinn.merchant.util.show
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class IngredientListFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private lateinit var binding: IngredientListFragmentBinding
    private val factory: IngredientListModelFactory by instance()
    private lateinit var viewModel: IngredientListViewModel
    private lateinit var mAdapter: GroupAdapter<GroupieViewHolder>
    private var cat_id = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = IngredientListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(IngredientListViewModel::class.java)
        arguments?.takeIf { it.containsKey(ARG_CATEGORY_ID) }?.apply {
            cat_id = getInt(ARG_CATEGORY_ID)
            viewModel.fetchIngredientById(cat_id)
        }
        bindUI()
    }

    private fun bindUI() {
        binding.progressBar.show()
        observableViewModel()
    }

    fun searchIngredients(query: String) {
        if (query.isEmpty()) {
            viewModel.fetchIngredientById(cat_id)
        } else {
            viewModel.searchIngredient(query)
        }
    }

    private fun observableViewModel() {
        viewModel.getIngredients().observe(requireActivity(),
            { ingredients: List<Ingredient?>? ->
                if (ingredients != null) {
                    binding.recyclerview.visibility = View.VISIBLE
                    initRecyclerView(ingredients.toIngredientItem())
                }
            })

        viewModel.getError().observe(requireActivity(),
            { isError: Boolean? ->
                if (isError != null) if (isError) {
                    binding.tvError.visibility = View.VISIBLE
                    binding.recyclerview.visibility = View.GONE
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
                        binding.recyclerview.visibility = View.GONE
                    }
                }
            })
    }

    private fun initRecyclerView(ingredientItem: List<IngredientItem>) {

        mAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(ingredientItem)
        }
        binding.recyclerview.apply {
            layoutManager = GridLayoutManager(context, 5, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = mAdapter
        }

    }


    private fun List<Ingredient?>.toIngredientItem(): List<IngredientItem> {
        return this.map {
            IngredientItem(it)
        }
    }
}