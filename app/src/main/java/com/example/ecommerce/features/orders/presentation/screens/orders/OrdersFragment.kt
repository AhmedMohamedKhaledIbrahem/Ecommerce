package com.example.ecommerce.features.orders.presentation.screens.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.core.ui.event.navigationWithArgs
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.core.utils.detectScrollEnd
import com.example.ecommerce.databinding.FragmentOrdersBinding
import com.example.ecommerce.features.orders.presentation.event.OrderEvent
import com.example.ecommerce.features.orders.presentation.screens.adapter.order_adapter.OrderAdapter
import com.example.ecommerce.features.orders.presentation.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private lateinit var orderAdapter: OrderAdapter
    private val orderViewModel by viewModels<OrderViewModel>()
    private lateinit var root: View
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detectScrollEnd(binding.ordersRecyclerView)
        orderViewModel.onEvent(OrderEvent.LoadOrders)
        orderState()
        orderEvent()

    }


    private fun orderRecyclerView(orderWithItems: List<OrderWithItems>) {
        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        orderAdapter = OrderAdapter(
            orderWithItems = orderWithItems
        ) {

            orderViewModel.onEvent(OrderEvent.OnOrderItemCardClick(it.toTypedArray()))
            orderViewModel.onEvent(OrderEvent.OnOrderCardClick)

        }
        binding.ordersRecyclerView.adapter = orderAdapter
    }

    private fun orderState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                orderViewModel.orderState.collect { state ->
                    val orders = state.orders
                    if (!state.isOrdersLoading && orders.isNotEmpty()) {
                        orderRecyclerView(orderWithItems = orders)
                    }
                }

            }
        }
    }

    private fun orderEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                orderViewModel.orderEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            SnackBarCustom.showSnackbar(
                                view = root,
                                message = event.message,
                            )
                        }

                        is UiEvent.Navigation.OrderDetails -> {
                            navigationWithArgs(
                                event = event,
                                onNavigate = { _, args ->
                                    val orderItems = args as? Array<OrderItemEntity> ?: emptyArray()
                                    val action =
                                        OrdersFragmentDirections.actionOrdersFragmentToOrderDetailsFragment(
                                            orderItems

                                        )
                                    findNavController().navigate(action)
                                }
                            )
                        }

                        else -> Unit
                    }
                }

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}