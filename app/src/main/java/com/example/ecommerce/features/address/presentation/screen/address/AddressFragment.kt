package com.example.ecommerce.features.address.presentation.screen.address

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.EditAddress
import com.example.ecommerce.core.constants.InsertAddress
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.extension.isTablet
import com.example.ecommerce.core.manager.address.AddressManager
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.core.utils.checkIsMessageOrResourceId
import com.example.ecommerce.core.utils.detectScrollEnd
import com.example.ecommerce.databinding.FragmentAddressBinding
import com.example.ecommerce.features.address.presentation.event.AddressEvent
import com.example.ecommerce.features.address.presentation.event.DeleteAddressEvent
import com.example.ecommerce.features.address.presentation.event.SelectAddressEvent
import com.example.ecommerce.features.address.presentation.screen.address.addressrecyclerview.AddressAdapter
import com.example.ecommerce.features.address.presentation.viewmodel.address.AddressViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.address.DeleteAddressViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.address.SelectAddressViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.addressaction.AddressActionViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.addressaction.IAddressActionViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.customer.CustomerViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.customer.ICustomerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddressFragment : Fragment() {
    @Inject
    lateinit var addressManager: AddressManager
    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var rootView: View
    private val addressViewModel by viewModels<AddressViewModel>()
    private val selectAddressViewModel by viewModels<SelectAddressViewModel>()
    private val deleteAddressViewModel by viewModels<DeleteAddressViewModel>()
    private val customerViewModel: ICustomerViewModel by activityViewModels<CustomerViewModel>()
    private val addressActionViewModel: IAddressActionViewModel by activityViewModels<AddressActionViewModel>()
    private var addressList: MutableList<CustomerAddressEntity> = mutableListOf()
    private var customerAddressEntity: CustomerAddressEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        rootView = binding.root


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isTablet(requireContext())) {
            adaptiveFBAPosition(landScapeVerticalBase = 0.84f, portraitVerticalBase = 0.88f)
        } else {
            adaptiveFBAPosition(landScapeVerticalBase = 0.61f, portraitVerticalBase = 0.84f)
        }
        addressViewModel.onEvent(AddressEvent.LoadAllAddress)
        addressEvent()
        addressState()
        deleteAddressEvent()
        deleteAddressState()
        selectAddressEvent()
        selectAddressState()

        floatingActionButtonAddAddressOnClickListener()
        detectScrollEnd(binding.addressRecyclerView)
    }


    private fun addressState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addressViewModel.addressState.collect { state ->
                    if (!state.isGetAllAddressLoading) {
                        initRecyclerView(state.addressList)
                    }


                }
            }
        }
    }

    private fun deleteAddressState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                deleteAddressViewModel.deleteAddressState.collect { state ->
                    if (!state.isDeleteAddressLoading) {
                        deleteCustomerAddress()
                    }
                }
            }
        }
    }

    private fun selectAddressState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                selectAddressViewModel.selectAddressState.collect { state ->
                    if (state.customerAddressId != -1) {
                        addressManager.setAddressId(state.customerAddressId)
                    }
                }
            }
        }
    }

    private fun addressEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addressViewModel.addressEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            checkIsMessageOrResourceId(
                                message = event.message,
                                resId = event.resId,
                                root = rootView,
                                context = requireContext()
                            )
                        }

                        else -> Unit
                    }

                }
            }
        }
    }

    private fun deleteAddressEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                deleteAddressViewModel.deleteAddressEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            checkIsMessageOrResourceId(
                                message = event.message,
                                resId = event.resId,
                                root = rootView,
                                context = requireContext()
                            )

                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun selectAddressEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                selectAddressViewModel.selectAddressEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            checkIsMessageOrResourceId(
                                message = event.message,
                                resId = event.resId,
                                root = rootView,
                                context = requireContext()
                            )
                        }

                        else -> Unit
                    }

                }
            }
        }
    }


    private fun navigate() {
        val navController = findNavController()
        navController.navigate(R.id.editAddressFragment)
    }

    private fun floatingActionButtonAddAddressOnClickListener() {
        binding.floatingActionButtonAddAddress.setOnClickListener {
            addressActionViewModel.setAddressAction(InsertAddress)
            navigate()
        }
    }


    private fun initRecyclerView(addressData: List<CustomerAddressEntity>) {
        addressList = addressData.toMutableList()
        binding.addressRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        addressAdapter = AddressAdapter(
            addressData,
            onCardClickListener = { customerAddressEntity ->

                selectAddressViewModel.onEvent(
                    SelectAddressEvent.SetCustomerAddressId(
                        customerAddressEntity.id
                    )
                )
                selectAddressViewModel.onEvent(SelectAddressEvent.SetSelected(customerAddressEntity.isSelect))
                selectAddressViewModel.onEvent(SelectAddressEvent.UnSelectAddress)
                selectAddressViewModel.onEvent(SelectAddressEvent.SelectAddress)
                addressViewModel.onEvent(AddressEvent.LoadAllAddress)

            },
            onDeleteClickListener = { customerAddressEntity ->
                this.customerAddressEntity = customerAddressEntity
                deleteAddressViewModel.onEvent(
                    DeleteAddressEvent.DeleteAddressInput(
                        customerAddressEntity
                    )
                )
                deleteAddressViewModel.onEvent(DeleteAddressEvent.DeleteAddressButton)


            },
            onEditClickListener = {
                customerViewModel.setCustomerEntity(customerEntity = it)
                addressActionViewModel.setAddressAction(EditAddress)
                navigate()
            },
        )
        binding.addressRecyclerView.adapter = addressAdapter

    }

    private fun deleteCustomerAddress() {
        val position = addressList.indexOf(customerAddressEntity)
        if (position != -1) {
            addressViewModel.onEvent(AddressEvent.LoadAllAddress)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun adaptiveFBAPosition(landScapeVerticalBase: Float, portraitVerticalBase: Float) {
        val orientation = resources.configuration.orientation
        val fab = binding.floatingActionButtonAddAddress
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            horizontalBias = 0.9f
        }

        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                layoutParams.verticalBias = landScapeVerticalBase
                fab.layoutParams = layoutParams
            }

            Configuration.ORIENTATION_PORTRAIT -> {
                layoutParams.verticalBias = portraitVerticalBase
                fab.layoutParams = layoutParams
            }

            else -> Unit
        }
    }


}