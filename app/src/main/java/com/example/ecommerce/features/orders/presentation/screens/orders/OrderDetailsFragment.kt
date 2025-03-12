package com.example.ecommerce.features.orders.presentation.screens.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.features.orders.presentation.screens.adapter.order_details_adapter.OrderDetailsAdapter
import com.example.ecommerce.features.orders.presentation.screens.adapter.order_details_adapter.ProductDetailsAdapter

class OrderDetailsFragment : Fragment() {
    private val args: OrderDetailsFragmentArgs by navArgs()
    private lateinit var orderDetailsRecyclerView: RecyclerView
    private lateinit var orderDetailsAdapter: OrderDetailsAdapter
    private lateinit var productDetailsAdapter: ProductDetailsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_details, container, false)
        initView(view = view)
        return view
    }

    private fun initView(view: View) {
        orderDetailsRecyclerView = view.findViewById(R.id.orderDetailsRecyclerView)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orderItemEntity = args.orderDetailsArgs.toList()
        initOrderRecycleView(orderItemEntity = orderItemEntity)

    }

    private fun initOrderRecycleView(orderItemEntity: List<OrderItemEntity>) {
        orderDetailsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        orderDetailsAdapter =
            OrderDetailsAdapter(orderWithItems = orderItemEntity, requireContext())
        productDetailsAdapter = ProductDetailsAdapter(orderItemEntity, requireContext())
        val concatAdapter = ConcatAdapter(orderDetailsAdapter,productDetailsAdapter)
        orderDetailsRecyclerView.adapter = concatAdapter
    }


}