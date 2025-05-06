package com.example.ecommerce.features.address.presentation.screen.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.EditAddress
import com.example.ecommerce.core.constants.InsertAddress
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.ui.UiState
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.core.utils.detectScrollEnd
import com.example.ecommerce.features.address.presentation.screen.address.addressrecyclerview.AddressAdapter
import com.example.ecommerce.features.address.presentation.viewmodel.address.AddressViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.address.IAddressViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.addressaction.AddressActionViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.addressaction.IAddressActionViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.customer.CustomerViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.customer.ICustomerViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressFragment : Fragment() {
    private lateinit var addressRecyclerView: RecyclerView
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var rootView: View
    private lateinit var floatingActionButtonAddAddress: FloatingActionButton
    private val addressViewModel: IAddressViewModel by viewModels<AddressViewModel>()
    private val customerViewModel: ICustomerViewModel by activityViewModels<CustomerViewModel>()
    private val addressActionViewModel: IAddressActionViewModel by activityViewModels<AddressActionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_address, container, false)
        initView(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAddress()
        addressEvent()
        addressState()
        floatingActionButtonAddAddressOnClickListener()
        detectScrollEnd(addressRecyclerView)
    }


    private fun initView(view: View) {
        addressRecyclerView = view.findViewById(R.id.addressRecyclerView)
        addressRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        floatingActionButtonAddAddress = view.findViewById(R.id.floatingActionButtonAddAddress)
        rootView = view
    }

    private fun addressState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addressViewModel.addressState.collect { state ->
                    initRecyclerView(state)
                }
            }
        }
    }

    private fun addressEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addressViewModel.addressEvent.collect { state ->
                    addressUiStates(state)
                }
            }
        }
    }

    private fun addressUiStates(state: UiState<Any>) {
        when (state) {
            is UiState.Loading -> {
                addressUiSourceStateLoading(state)
            }

            is UiState.Success -> {
                addressUiSourceStateSuccess(state)
            }

            is UiState.Error -> {
                addressUiSourceStateError(state)

            }
        }
    }

    private fun addressUiSourceStateLoading(state: UiState.Loading) {
        when (state.source) {
            "getAddress" -> {
            }
        }
    }

    private fun addressUiSourceStateSuccess(state: UiState.Success<Any>) {
        when (state.source) {
            "getAddress" -> {}
        }
    }


    private fun addressUiSourceStateError(state: UiState.Error) {
        when (state.source) {
            "getAddress" -> {
                SnackBarCustom.showSnackbar(
                    view = rootView,
                    message = state.message
                )
            }
        }
    }

    private fun getAddress() {
        addressViewModel.getAddress()
    }

    private fun navigate() {
        val navController = findNavController()
        navController.navigate(R.id.editAddressFragment)
    }

    private fun floatingActionButtonAddAddressOnClickListener() {
        floatingActionButtonAddAddress.setOnClickListener {
            addressActionViewModel.setAddressAction(InsertAddress)
            navigate()
        }
    }


    private fun initRecyclerView(addressData: List<CustomerAddressEntity>) {
        lifecycleScope.launch {
            addressAdapter = AddressAdapter(
                addressData,
                onCardClickListener = {},
                onDeleteClickListener = {},
                onEditClickListener = {
                    customerViewModel.setCustomerEntity(customerEntity = it)
                    addressActionViewModel.setAddressAction(EditAddress)
                    navigate()
                },
            )
            addressRecyclerView.adapter = addressAdapter
        }
    }


}