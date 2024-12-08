package com.example.ecommerce.features.address.presentation.screen

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.countryStateMap
import com.example.ecommerce.core.constants.regionCodeMap
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.network.NetworkStatuesHelperViewModel
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.core.utils.NetworkStatus
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.ShippingInfoRequestEntity
import com.example.ecommerce.features.address.presentation.viewmodel.AddressViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.IAddressViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditAddressFragment : Fragment() {
    private lateinit var firstNameAddressEditText: TextInputEditText
    private lateinit var lastNameAddressEditText: TextInputEditText
    private lateinit var emailNameAddressEditText: TextInputEditText
    private lateinit var phoneNumberAddressEditText: TextInputEditText
    private lateinit var cityAddressEditText: TextInputEditText
    private lateinit var streetAddressEditText: TextInputEditText
    private lateinit var postCodeAddressEditText: TextInputEditText
    private lateinit var firstNameAddressTextFieldInputLayout: TextInputLayout
    private lateinit var lastNameAddressTextFieldInputLayout: TextInputLayout
    private lateinit var streetAddressTextFieldInputLayout: TextInputLayout
    private lateinit var emailAddressTextFieldInputLayout: TextInputLayout
    private lateinit var phoneNumberAddressTextFieldInputLayout: TextInputLayout
    private lateinit var cityAddressTextFieldInputLayout: TextInputLayout
    private lateinit var postCodeAddressTextFieldInputLayout: TextInputLayout
    private lateinit var buttonSave: MaterialButton
    private lateinit var buttonLocation: MaterialButton
    private lateinit var spinnerCountry: Spinner
    private lateinit var spinnerState: Spinner
    private val addressViewModel: IAddressViewModel by viewModels<AddressViewModel>()
    private val loadingDialog by lazy {
        LoadingDialogFragment().getInstance()
    }
    private val networkStatusViewModel: NetworkStatuesHelperViewModel by viewModels()
    private lateinit var root: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_address, container, false)
        initView(view)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkInternetStatus()
        spinnerCountryOnItemSelected()
        textWatchers()
        saveButtonOnClickedListener()
        addressState()


    }

    private fun initView(view: View) {
        firstNameAddressEditText = view.findViewById(R.id.firstNameAddressEditText)
        lastNameAddressEditText = view.findViewById(R.id.lastNameAddressEditText)
        emailNameAddressEditText = view.findViewById(R.id.emailNameAddressEditText)
        phoneNumberAddressEditText = view.findViewById(R.id.phoneNumberAddressEditText)
        cityAddressEditText = view.findViewById(R.id.cityAddressEditText)
        postCodeAddressEditText = view.findViewById(R.id.postCodeAddressEditText)
        streetAddressEditText = view.findViewById(R.id.streetAddressEditText)
        firstNameAddressTextFieldInputLayout =
            view.findViewById(R.id.firstNameAddressTextFieldInputLayout)
        lastNameAddressTextFieldInputLayout =
            view.findViewById(R.id.lastNameAddressTextFieldInputLayout)
        emailAddressTextFieldInputLayout = view.findViewById(R.id.emailAddressTextFieldInputLayout)
        phoneNumberAddressTextFieldInputLayout =
            view.findViewById(R.id.phoneNumberAddressTextFieldInputLayout)
        cityAddressTextFieldInputLayout = view.findViewById(R.id.cityAddressTextFieldInputLayout)
        postCodeAddressTextFieldInputLayout =
            view.findViewById(R.id.postCodeAddressTextFieldInputLayout)
        streetAddressTextFieldInputLayout =
            view.findViewById(R.id.streetAddressTextFieldInputLayout)
        buttonSave = view.findViewById(R.id.buttonSave)
        buttonLocation = view.findViewById(R.id.buttonLocation)
        spinnerCountry = view.findViewById(R.id.spinnerCountry)
        spinnerState = view.findViewById(R.id.spinnerState)
        root = view
    }

    private fun checkInternetStatus() {
        NetworkStatus.checkInternetConnection(
            lifecycleOwner = this@EditAddressFragment,
            networkStatus = networkStatusViewModel.networkStatus,
            loadingDialog = loadingDialog,
            fragmentManager = parentFragmentManager,
            rootView = root,
        )
    }

    private fun spinnerCountryOnItemSelected() {
        val countries = countryStateMap.keys.toList()
        val countryAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countries)
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.adapter = countryAdapter

        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedCountry = countries[p2]
                val regions = countryStateMap[selectedCountry] ?: listOf("No regions available")
                spinnerRegionAdapter(regions)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d("onNothingSelected", "onNothingSelected ")
            }


        }
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
                cityValidateInput()
                postCodeValidateInput()
            }
        }
        firstNameAddressEditText.addTextChangedListener(textWatcher)
        lastNameAddressEditText.addTextChangedListener(textWatcher)
        emailNameAddressEditText.addTextChangedListener(textWatcher)
        phoneNumberAddressEditText.addTextChangedListener(textWatcher)
        cityAddressEditText.addTextChangedListener(textWatcher)
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

    private fun cityValidateInput(): Boolean {
        var isValid = true
        val cityPattern = Regex("^[a-zA-Z][a-zA-Z0-9_]*$")
        val city = cityAddressEditText.text.toString()
        if (city.isBlank()) {
            cityAddressTextFieldInputLayout.error = getString(R.string.city_is_required)
            cityAddressTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else if (!cityPattern.matches(city)) {
            cityAddressTextFieldInputLayout.error = getString(R.string.invalid_city_format)
            cityAddressTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            cityAddressTextFieldInputLayout.error = null
        }
        return isValid
    }

    private fun streetAddressValidateInput(): Boolean {
        var isValid = true
        val streetAddressPattern = Regex("^[a-zA-Z0-9_]+$")
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
            val city = cityAddressEditText.text.toString()
            val address = streetAddressEditText.text.toString()
            val postCode = postCodeAddressEditText.text.toString()
            val country = spinnerCountry.selectedItem.toString()
            val state = spinnerState.selectedItem.toString()
            val stateCode = regionCodeMap[state] ?: "unknown"
            if (
                firstNameValidateInput() &&
                lastNameValidateInput() &&
                emailValidateInput() &&
                phoneNumberValidateInput() &&
                cityValidateInput() &&
                postCodeValidateInput() &&
                streetAddressValidateInput() &&
                spinnerCountry.selectedItem.toString() != "Select Country" &&
                spinnerState.selectedItem.toString() != "Select State"
            ) {
                val billingInfoAddressRequest = BillingInfoRequestEntity(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    address = address,
                    city = city,
                    state = stateCode,
                    country = country,
                    postCode = postCode,
                    phone = phoneNumber,
                )
                val shippingInfoAddressRequest = ShippingInfoRequestEntity(
                    firstName = firstName,
                    lastName = lastName,
                    address = address,
                    city = city,
                    state = stateCode,
                    country = country,
                    postCode = postCode,
                )
                val addressRequestEntity = AddressRequestEntity(
                    shipping = shippingInfoAddressRequest,
                    billing = billingInfoAddressRequest,
                )
                updateAddress(addressRequestEntity = addressRequestEntity)

            } else if (
                spinnerCountry.selectedItem.toString() == "Select Country" ||
                spinnerState.selectedItem.toString() == "Select State"
            ) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_select_country_and_state),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    private fun updateAddress(addressRequestEntity: AddressRequestEntity) {
        addressViewModel.updateAddress(updateAddressParams = addressRequestEntity)
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

    private fun addressUiSourceStateLoading(state: UiState.Loading) {
        when (state.source) {
            "updateAddress" -> {
                loadingDialog.showLoading(parentFragmentManager)
            }
        }
    }

    private fun addressUiSourceStateSuccess(state: UiState.Success<Any>) {
        when (state.source) {
            "updateAddress" -> {
                val navController = findNavController()
                loadingDialog.dismissLoading()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.address_updated_successfully), Toast.LENGTH_SHORT
                )
                    .show()

                lifecycleScope.launch {
                    delay(500L)
                    navController.navigate(R.id.addressFragment)
                }


            }
        }

    }

    private fun addressUiSourceStateError(state: UiState.Error) {
        when (state.source) {
            "updateAddress" -> {
                loadingDialog.dismissLoading()
                SnackBarCustom.showSnackbar(root, state.message)
            }
        }
    }
}