package com.example.ecommerce.features.address.presentation.screen

import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ecommerce.R
import com.example.ecommerce.core.data.entities.CustomerAddressEntity
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.network.NetworkStatuesHelperViewModel
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.core.utils.AddressUtil
import com.example.ecommerce.core.utils.NetworkStatus
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.features.address.presentation.screen.addressrecyclerview.AddressAdapter
import com.example.ecommerce.features.address.presentation.screen.addressrecyclerview.AddressItem
import com.example.ecommerce.features.address.presentation.viewmodel.AddressViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.IAddressViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AddressFragment : Fragment() {
    private lateinit var addressRecyclerView: RecyclerView
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var rootView: View
    private lateinit var addressShimmerLayout: ShimmerFrameLayout
    private lateinit var swipeRefreshAddressLayout: SwipeRefreshLayout
    private val networkStatusViewModel: NetworkStatuesHelperViewModel by viewModels()
    private val addressViewModel: IAddressViewModel by viewModels<AddressViewModel>()
    private val loadingDialog by lazy {
        LoadingDialogFragment().getInstance()
    }

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
        checkInternetStatus()
        addressState()
        getAddressById(AddressUtil.addressId)
        onSwipeRefreshListener()
    }

    private fun initView(view: View) {
        addressRecyclerView = view.findViewById(R.id.addressRecyclerView)
        addressRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        addressShimmerLayout = view.findViewById(R.id.addressShimmerFrameLayout)
        swipeRefreshAddressLayout = view.findViewById(R.id.addressSwipeRefreshViewLayout)
        rootView = view
    }

    private fun checkInternetStatus() {
        NetworkStatus.checkInternetConnection(
            lifecycleOwner = this@AddressFragment,
            networkStatus = networkStatusViewModel.networkStatus,
            loadingDialog = loadingDialog,
            fragmentManager = parentFragmentManager,
            rootView = rootView,
        )
    }

    private fun addressState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addressViewModel.addressState.collect { state ->
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

    private fun addressUiSourceStateError(state: UiState.Error) {
        when (state.source) {
            "getAddressById" -> {
                shimmerStopWhenDataSuccess()
                SnackBarCustom.showSnackbar(
                    view = rootView,
                    message = state.message
                )
            }

            "checkUpdateAddress" -> {
                shimmerStopWhenDataSuccess()
                SnackBarCustom.showSnackbar(
                    view = rootView,
                    message = state.message
                )

            }
        }
    }


    private fun addressUiSourceStateSuccess(state: UiState.Success<Any>) {
        when (state.source) {
            "getAddressById" -> {
                shimmerStopWhenDataSuccess()
                val addressData = state.data as CustomerAddressEntity
                initRecyclerView(addressData)
            }

            "checkUpdateAddress" -> {
                addressViewModel.getAddressById(AddressUtil.addressId)
                shimmerStopWhenDataSuccess()

            }
        }
    }

    private fun shimmerStopWhenDataSuccess() {
        addressShimmerLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        addressRecyclerView.visibility = View.VISIBLE
    }

    private fun addressUiSourceStateLoading(state: UiState.Loading) {
        when (state.source) {
            "getAddressById" -> {
                shimmerStartWhenLoading()

            }

            "checkUpdateAddress" -> {
                Log.d("addressUiSourceStateLoading?", "yes")
                shimmerStartWhenLoading()
            }

        }
    }

    private fun shimmerStartWhenLoading() {
        addressShimmerLayout.apply {
            visibility = View.VISIBLE // Ensure the shimmer is visible
            startShimmer()            // Start the shimmer animation
        }
        addressRecyclerView.apply {
            visibility = View.GONE
        }
    }

    private fun getAddressById(id: Int) {
        addressViewModel.getAddressById(id = id)
    }

    private fun clickNav() {

        val navController = findNavController()
        navController.navigate(R.id.editAddressFragment)


    }

    private fun onSwipeRefreshListener() {
        swipeRefreshAddressLayout.setOnRefreshListener {
            lifecycleScope.launch {
                delay(1000)
                addressViewModel.checkUpdateAddress()

                swipeRefreshAddressLayout.isRefreshing = false


            }
        }
    }

    private fun initRecyclerView(addressData: CustomerAddressEntity) {
        lifecycleScope.launch {
            val data = withContext(Dispatchers.IO) { setData(addressData) }
            addressAdapter = AddressAdapter(data) {
                clickNav()
            }
            addressRecyclerView.adapter = addressAdapter
        }
    }

    private fun setData(addressData: CustomerAddressEntity): List<AddressItem> {
        val addressItem = listOf(
            AddressItem(
                fullName = "FullName: " + addressData.firstName + " " + addressData.lastName,
                emailAddress = "Email: " + addressData.email,
                phoneNumber = "PhoneNumber: " + addressData.phone,
                country = "Country: " + addressData.country,
                city = "City: " + addressData.city,
                state = "State:" + addressData.state,
                streetAddress = "Street Address: " + addressData.address,
                postCode = "Post Code: " + addressData.zipCode,
            ),
        )
        return addressItem
    }


}