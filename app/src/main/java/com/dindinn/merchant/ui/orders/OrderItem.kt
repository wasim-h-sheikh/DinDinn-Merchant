package com.dindinn.merchant.ui.orders

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dindinn.merchant.R
import com.dindinn.merchant.databinding.ItemOrderBinding
import com.dindinn.merchant.model.Addon
import com.dindinn.merchant.model.Order
import com.dindinn.merchant.util.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem

class OrderItem(
    private val parent: OrderFragment,
    private val order: Order?,
    private var displayWidth: Int
) : BindableItem<ItemOrderBinding>() {

    override fun getLayout() = R.layout.item_order

    override fun bind(viewBinding: ItemOrderBinding, position: Int) {
        val layoutParams = viewBinding.cardMainLayout.layoutParams
        layoutParams.width = (displayWidth / 3.4).toInt()
        viewBinding.cardMainLayout.layoutParams = layoutParams

        viewBinding.textViewOrderID.text = "#" + order?.id.toString()
        viewBinding.textViewItemName.text = order?.title
        viewBinding.textViewItemQuantity.text = order?.quantity.toString()
        viewBinding.textViewOrderAt.text =
            order?.created_at?.getDateWithServerTimeStamp()?.formatTo("HH:mm a")
        val totalQty = order?.quantity
        viewBinding.textViewTotalItems.text =
            if (totalQty!! > 1) order?.quantity.toString() + " items" else order?.quantity.toString() + " item"

        val simpleCountDownTimer = SimpleCountDownTimer(
            5, 0,
            OrderCountDownListener(viewBinding), 1
        )
        simpleCountDownTimer.setTimerPattern("m:s")
        simpleCountDownTimer.start(false)


        viewBinding.buttonAccept.setOnClickListener {
            if (viewBinding.buttonAccept.text == viewBinding.buttonAccept.getStringFromResource(R.string.expired)) {
                simpleCountDownTimer.pause()
            }
            parent.requireActivity().cancelAlarm(order?.id!!)
            parent.removeItem(position)
        }

        val mAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(order?.addon?.toAddonItem()!!)
        }
        viewBinding.recyclerviewAddons.apply {
            layoutManager = GridLayoutManager(
                viewBinding.recyclerviewAddons.context,
                1,
                RecyclerView.VERTICAL,
                false
            )
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    class OrderCountDownListener(private val viewBinding: ItemOrderBinding) :
        SimpleCountDownTimer.OnCountDownListener {

        override fun onCountDownActive(time: String) {
            Coroutines.main {
                viewBinding.textViewTimer.text = time.toTimerFormat()
                viewBinding.imageViewIndicator.setImageResource(getIndicatorImage(time))
            }
        }

        override fun onCountDownFinished() {
            Coroutines.main {
                viewBinding.textViewTimer.text = "0 s"
                viewBinding.buttonAccept.setBackgroundResource(R.drawable.button_disable_rounded)
                viewBinding.buttonAccept.setTextFromResource(R.string.expired)
            }
        }

    }

    override fun initializeViewBinding(view: View): ItemOrderBinding = ItemOrderBinding.bind(view)

    fun getAddons() = order?.addon

    private fun List<Addon?>.toAddonItem(): List<AddonItem> {
        return this.map {
            AddonItem(it)
        }
    }
}