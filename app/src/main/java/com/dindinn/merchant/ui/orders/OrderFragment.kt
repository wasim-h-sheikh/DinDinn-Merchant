package com.dindinn.merchant.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dindinn.merchant.databinding.OrderFragmentBinding
import com.dindinn.merchant.model.Order
import com.dindinn.merchant.util.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class OrderFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private lateinit var binding: OrderFragmentBinding
    private val factory: OrderViewModelFactory by instance()
    private lateinit var viewModel: OrderViewModel
    private lateinit var mAdapter: GroupAdapter<GroupieViewHolder>
    private var NEXT_PAGE_POSITION: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OrderFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(OrderViewModel::class.java)
        bindUI()
    }

    private fun bindUI() {
        binding.imageViewShowMore.setOnClickListener { showMore() }

        binding.customToolbar.imageViewManage.setOnClickListener {
            requireActivity().supportFragmentManager.manageFragmentTransaction(
                INGREDIENT_FRAGMENT_TAG
            )
        }

        binding.progressBar.show()
        observableViewModel()
    }



    private fun showMore() {
        NEXT_PAGE_POSITION += 3
        if (NEXT_PAGE_POSITION >= mAdapter.itemCount) {
            NEXT_PAGE_POSITION = mAdapter.itemCount - 1
        }
        binding.recyclerview.post { binding.recyclerview.smoothScrollToPosition(NEXT_PAGE_POSITION) }
    }

    private fun initRecyclerView(orderItem: List<OrderItem>) {

        mAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(orderItem)
        }

        binding.recyclerview.apply {
            layoutManager = GridLayoutManager(context, 1, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = mAdapter
        }

    }


    private fun List<Order?>.toOrderItem(): List<OrderItem> {
        return this.map {
            OrderItem(this@OrderFragment, it, requireActivity().getScreenWidth())
        }
    }


    private fun List<Order?>.setAlarms() {
        forEach {
            if (it != null) {
                requireActivity().setAlarm(it)
            }
        }
    }

    fun removeItem(pos: Int) {
        if (mAdapter.itemCount > 0) {
            mAdapter.remove(mAdapter.getItem(pos))
            mAdapter.notifyDataSetChanged()
        }

    }

    private fun observableViewModel() {
        viewModel.getOrders().observe(requireActivity(),
            { orders: List<Order?>? ->
                if (orders != null) {
                    binding.recyclerview.visibility = View.VISIBLE
                    initRecyclerView(orders.toOrderItem())
                    orders.setAlarms()
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
}