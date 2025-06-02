package com.example.ecommerce.features.address.presentation.screen.editaddress

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.EditAddress
import com.example.ecommerce.core.constants.InsertAddress
import com.example.ecommerce.core.constants.countryCode
import com.example.ecommerce.core.constants.countryStateMap
import com.example.ecommerce.core.constants.regionCodeMap
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.core.ui.event.combinedEvents
import com.example.ecommerce.core.utils.AddressUtil
import com.example.ecommerce.core.utils.checkIsMessageOrResourceId
import com.example.ecommerce.databinding.FragmentEditAddressBinding
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.ShippingInfoRequestEntity
import com.example.ecommerce.features.address.presentation.event.AddressEvent
import com.example.ecommerce.features.address.presentation.viewmodel.address.AddressViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.addressaction.AddressActionViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.addressaction.IAddressActionViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.customer.CustomerViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.customer.ICustomerViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditAddressFragment : Fragment() {
    private var _binding: FragmentEditAddressBinding? = null
    private val binding get() = _binding!!
    private lateinit var firstNameAddressEditText: TextInputEditText
    private lateinit var lastNameAddressEditText: TextInputEditText
    private lateinit var emailNameAddressEditText: TextInputEditText
    private lateinit var phoneNumberAddressEditText: TextInputEditText

    private lateinit var streetAddressEditText: TextInputEditText
    private lateinit var postCodeAddressEditText: TextInputEditText
    private lateinit var firstNameAddressTextFieldInputLayout: TextInputLayout
    private lateinit var lastNameAddressTextFieldInputLayout: TextInputLayout
    private lateinit var streetAddressTextFieldInputLayout: TextInputLayout
    private lateinit var emailAddressTextFieldInputLayout: TextInputLayout
    private lateinit var phoneNumberAddressTextFieldInputLayout: TextInputLayout

    private lateinit var postCodeAddressTextFieldInputLayout: TextInputLayout
    private val customerViewModel: ICustomerViewModel by activityViewModels<CustomerViewModel>()
    private val addressActionViewModel: IAddressActionViewModel by activityViewModels<AddressActionViewModel>()
    private lateinit var buttonSave: MaterialButton
    private lateinit var spinnerCountry: Spinner
    private lateinit var spinnerState: Spinner


    private val addressViewModel by viewModels<AddressViewModel>()


    private lateinit var root: View
    private lateinit var customerAddressEntity: CustomerAddressEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditAddressBinding.inflate(inflater, container, false)
        root = binding.root
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        spinnerCountryOnItemSelected()
        textWatchers()
        saveButtonOnClickedListener()
        addressState()
        addressEvent()
        showInitAddress()
        customerAddressObserver()
    }

    private fun customerAddressObserver() {
        customerViewModel.customerEntity.observe(viewLifecycleOwner) {
            if (addressActionObserver() == EditAddress) {
                customerAddressEntity = it
                populateFields(address = it)
            }

        }
    }

    private fun addressActionObserver(): String {
        var addressAction = ""
        addressActionViewModel.addressAction.observe(viewLifecycleOwner) {
            addressAction = it
        }
        return addressAction
    }

    private fun initView() {
        firstNameAddressEditText = binding.firstNameAddressEditText
        lastNameAddressEditText = binding.lastNameAddressEditText
        emailNameAddressEditText = binding.emailNameAddressEditText
        phoneNumberAddressEditText = binding.phoneNumberAddressEditText

        postCodeAddressEditText = binding.postCodeAddressEditText
        streetAddressEditText = binding.streetAddressEditText
        firstNameAddressTextFieldInputLayout =
            binding.firstNameAddressTextFieldInputLayout
        lastNameAddressTextFieldInputLayout =
            binding.lastNameAddressTextFieldInputLayout
        emailAddressTextFieldInputLayout = binding.emailAddressTextFieldInputLayout
        phoneNumberAddressTextFieldInputLayout =
            binding.phoneNumberAddressTextFieldInputLayout

        postCodeAddressTextFieldInputLayout =
            binding.postCodeAddressTextFieldInputLayout
        streetAddressTextFieldInputLayout =
            binding.streetAddressTextFieldInputLayout
        buttonSave = binding.buttonSave
        spinnerCountry = binding.spinnerCountry
        spinnerState = binding.spinnerState

    }


    private fun spinnerCountryOnItemSelected() {
        val countries = countryStateMap.keys.map {
            if (it is Int) getString(it) else it.toString()
        }
        val countryAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countries)
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.adapter = countryAdapter

        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                val selectedCountry = countries[position]
                val selectedKey = countryStateMap.keys.firstOrNull {
                    (it is Int && getString(it) == selectedCountry) || (it.toString() == selectedCountry)
                }
                val regionsRaw =
                    countryStateMap[selectedKey] ?: listOf(R.string.no_regions_available)
                val regions = regionsRaw.map {
                    if (it is Int) getString(it) else it.toString()
                }
                spinnerRegionAdapter(regions)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d("onNothingSelected", "onNothingSelected ")
            }


        }
    }


    private fun populateFields(address: CustomerAddressEntity?) {
        if (address == null) return
        firstNameAddressEditText.setText(address.firstName)
        lastNameAddressEditText.setText(address.lastName)
        emailNameAddressEditText.setText(address.email)
        phoneNumberAddressEditText.setText(address.phone)
        streetAddressEditText.setText(address.address)
        postCodeAddressEditText.setText(address.zipCode)
        val countryIndex =
            (spinnerCountry.adapter as ArrayAdapter<String>).getPosition(address.country)
        spinnerCountry.setSelection(countryIndex)

    }

    private fun spinnerRegionAdapter(regions: List<String>) {
        val regionAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, regions)
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerState.adapter = regionAdapter
        spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                regions[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d("RegionSpinner", "No region selected")
            }

        }

    }

    private fun textWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                firstNameValidateInput()
                lastNameValidateInput()
                emailValidateInput()
                phoneNumberValidateInput()

                postCodeValidateInput()
            }
        }
        firstNameAddressEditText.addTextChangedListener(textWatcher)
        lastNameAddressEditText.addTextChangedListener(textWatcher)
        emailNameAddressEditText.addTextChangedListener(textWatcher)
        phoneNumberAddressEditText.addTextChangedListener(textWatcher)
        postCodeAddressEditText.addTextChangedListener(textWatcher)
    }

    fun firstNameValidateInput(): Boolean {
        var isValid = true
        val firstNamePattern = Regex("^[a-zA-Z][a-zA-Z0-9_]*$")
        val firstName = firstNameAddressEditText.text.toString()

        if (firstName.isBlank()) {

            firstNameAddressTextFieldInputLayout.error = getString(R.string.first_name_is_required)
            firstNameAddressTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else if (!firstNamePattern.matches(firstName)) {
            firstNameAddressTextFieldInputLayout.error =
                getString(R.string.username_must_start_with_a_letter_and_contain_only_letters_numbers_or_underscores)
            firstNameAddressTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            firstNameAddressTextFieldInputLayout.error = null
        }
        return isValid
    }

    fun lastNameValidateInput(): Boolean {
        var isValid = true
        if (lastNameAddressEditText.text.toString().isBlank()) {
            lastNameAddressTextFieldInputLayout.error = getString(R.string.last_name_is_required)
            lastNameAddressTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            lastNameAddressTextFieldInputLayout.error = null
        }
        return isValid
    }

    fun emailValidateInput(): Boolean {
        var isValid = true
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        val email = emailNameAddressEditText.text.toString()


        if (email.isBlank()) {
            emailAddressTextFieldInputLayout.error = getString(R.string.email_is_required)
            emailAddressTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else if (!emailPattern.matcher(email).matches()) {
            emailAddressTextFieldInputLayout.error = getString(R.string.invalid_email_format)
            emailAddressTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            emailAddressTextFieldInputLayout.error = null
        }
        return isValid
    }

    private fun phoneNumberValidateInput(): Boolean {
        var isValid = true
        val phoneNumberPattern = Regex("^(010|011|012|015)\\d{8}$")
        val phoneNumber = phoneNumberAddressEditText.text.toString()
        if (phoneNumber.isBlank()) {
            phoneNumberAddressTextFieldInputLayout.error =
                getString(R.string.phone_number_is_required)
            phoneNumberAddressTextFieldInputLayout.errorIconDrawable = null
        } else if (!phoneNumberPattern.matches(phoneNumber)) {
            phoneNumberAddressTextFieldInputLayout.error =
                getString(R.string.invalid_phone_number_format)
            phoneNumberAddressTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            phoneNumberAddressTextFieldInputLayout.error = null

        }
        return isValid
    }

    private fun postCodeValidateInput(): Boolean {
        var isValid = true
        val postCodeRegex = Regex("\\d{5,8}")
        val postCode = postCodeAddressEditText.text.toString()
        if (postCode.isBlank()) {
            postCodeAddressTextFieldInputLayout.error =
                getString(R.string.post_code_is_required)
            postCodeAddressTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else if (!postCodeRegex.matches(postCode)) {
            postCodeAddressTextFieldInputLayout.error =
                getString(R.string.invalid_post_code_format)
            postCodeAddressTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            postCodeAddressTextFieldInputLayout.error = null
        }
        return isValid
    }

    private fun streetAddressValidateInput(): Boolean {
        var isValid = true
        val streetAddressPattern = Regex("^[a-zA-Z0-9_\\s]+$")
        if (streetAddressEditText.text.toString().isBlank()) {
            streetAddressTextFieldInputLayout.error = getString(R.string.street_address_is_required)
            streetAddressTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else if (!streetAddressPattern.matches(streetAddressEditText.text.toString())) {
            streetAddressTextFieldInputLayout.error =
                getString(R.string.invalid_street_address_format)
            streetAddressTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            streetAddressTextFieldInputLayout.error = null
        }

        return isValid
    }

    private fun saveButtonOnClickedListener() {
        buttonSave.setOnClickListener {
            val firstName = firstNameAddressEditText.text.toString()
            val lastName = lastNameAddressEditText.text.toString()
            val email = emailNameAddressEditText.text.toString()
            val phoneNumber = phoneNumberAddressEditText.text.toString()
            val address = streetAddressEditText.text.toString()
            val postCode = postCodeAddressEditText.text.toString()
            val country = spinnerCountry.selectedItem.toString()
            val state = spinnerState.selectedItem.toString()
            val stateCode = regionCodeMap[state] ?: getString(R.string.unknown)
            val countryCode = countryCode[country] ?: getString(R.string.unknown)
            if (
                firstNameValidateInput() &&
                lastNameValidateInput() &&
                emailValidateInput() &&
                phoneNumberValidateInput() &&
                postCodeValidateInput() &&
                streetAddressValidateInput() &&
                spinnerCountry.selectedItem.toString() != getString(R.string.select_country) &&
                spinnerState.selectedItem.toString() != getString(R.string.select_state)
            ) {
                val billingInfoAddressRequest = BillingInfoRequestEntity(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    address = address,
                    city = state,
                    country = countryCode,
                    postCode = postCode,
                    phone = phoneNumber,
                )
                val shippingInfoAddressRequest = ShippingInfoRequestEntity(
                    firstName = firstName,
                    lastName = lastName,
                    address = address,
                    city = state,
                    state = stateCode,
                    country = countryCode,
                    postCode = postCode,
                )
                val addressRequestEntity = AddressRequestEntity(
                    shipping = shippingInfoAddressRequest,
                    billing = billingInfoAddressRequest,
                )
                addressAction(addressRequestEntity)
            } else if (
                spinnerCountry.selectedItem.toString() == getString(R.string.select_country) ||
                spinnerState.selectedItem.toString() == getString(R.string.select_state)
            ) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_select_country_and_state),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    private fun addressAction(addressRequestEntity: AddressRequestEntity) {
        when (addressActionObserver()) {
            InsertAddress -> {
                insertAddress(addressRequestEntity = addressRequestEntity)
            }

            EditAddress -> {
                updateAddress(addressRequestEntity = addressRequestEntity)
            }

        }
    }

    private fun updateAddress(addressRequestEntity: AddressRequestEntity) {
        addressViewModel.onEvent(
            AddressEvent.Input.UpdateAddress(
                id = customerAddressEntity.id,
                addressRequestEntity = addressRequestEntity
            )
        )
        addressViewModel.onEvent(AddressEvent.Button.UpdateAddressButton)
    }

    private fun insertAddress(addressRequestEntity: AddressRequestEntity) {
        addressViewModel.onEvent(
            AddressEvent.Input.InsertAddress(
                addressRequestEntity = addressRequestEntity
            )
        )
        addressViewModel.onEvent(AddressEvent.Button.InsertAddressButton)
    }

    private fun addressState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addressViewModel.addressState.collectLatest { state ->

                    if (state.isInsertLoading || state.isUpdateLoading) {
                        showProgressBar()
                    } else {
                        hideProgressBar()
                    }

                }
            }
        }

    }

    private fun showProgressBar() {
        buttonSave.isEnabled = false
        buttonSave.text = ""
        binding.buttonSaveProgress.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        buttonSave.isEnabled = true
        buttonSave.text = getString(R.string.save)
        binding.buttonSaveProgress.visibility = View.GONE
    }


    private fun addressEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addressViewModel.addressEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            checkIsMessageOrResourceId(
                                event = event,
                                context = requireContext(),
                                root = binding.root,
                            )
                        }

                        is UiEvent.CombinedEvents -> {
                            combinedEvents(
                                events = event.events,
                                onShowSnackBar = { message, resId ->
                                    checkIsMessageOrResourceId(
                                        message = message,
                                        resId = resId,
                                        context = requireContext(),
                                        root = binding.root,
                                    )
                                },
                                onNavigate = { destinationId, _ ->
                                    findNavController().navigate(destinationId)

                                }
                            )
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun showInitAddress() {
        val address = AddressUtil.addressEntity
        address?.let {
            firstNameAddressEditText.setText(it.firstName)
            lastNameAddressEditText.setText(it.lastName)
            emailNameAddressEditText.setText(it.email)
            phoneNumberAddressEditText.setText(it.phone)
            postCodeAddressEditText.setText(it.zipCode)
            streetAddressEditText.setText(it.address)
            spinnerCountry.setSelection(countryStateMap.keys.indexOf(it.country))


        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}