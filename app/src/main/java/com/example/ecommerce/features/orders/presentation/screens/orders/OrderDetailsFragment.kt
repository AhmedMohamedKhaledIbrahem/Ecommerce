package com.example.ecommerce.features.orders.presentation.screens.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.databinding.FragmentOrderDetailsBinding
import com.example.ecommerce.features.orders.presentation.screens.adapter.order_details_adapter.OrderDetailsAdapter
import com.example.ecommerce.features.orders.presentation.screens.adapter.order_details_adapter.ProductDetailsAdapter

class OrderDetailsFragment : Fragment() {
    private val args: OrderDetailsFragmentArgs by navArgs()

    private lateinit var orderDetailsAdapter: OrderDetailsAdapter
    private lateinit var productDetailsAdapter: ProductDetailsAdapter
    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orderItemEntity = args.orderDetailsArgs.toList()
        initOrderRecycleView(orderItemEntity = orderItemEntity)

    }

    private fun initOrderRecycleView(orderItemEntity: List<OrderItemEntity>) {
        binding.orderDetailsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        orderDetailsAdapter =
            OrderDetailsAdapter(orderWithItems = orderItemEntity)
        productDetailsAdapter = ProductDetailsAdapter(orderItemEntity)
        val concatAdapter = ConcatAdapter(orderDetailsAdapter, productDetailsAdapter)
        binding.orderDetailsRecyclerView.adapter = concatAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}