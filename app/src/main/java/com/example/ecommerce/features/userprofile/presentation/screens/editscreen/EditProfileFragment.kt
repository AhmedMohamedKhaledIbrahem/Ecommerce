package com.example.ecommerce.features.userprofile.presentation.screens.editscreen

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.user.UserEntity
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity
import com.example.ecommerce.features.userprofile.presentation.screens.settingscreen.UpdateDisplayNameViewModel
import com.example.ecommerce.features.userprofile.presentation.viewmodel.usernamedetailsprofile.IUserNameDetailsProfileViewModel
import com.example.ecommerce.features.userprofile.presentation.viewmodel.usernamedetailsprofile.UserNameDetailsProfileViewModel
import com.example.ecommerce.features.userprofile.presentation.viewmodel.userprofile.IUserProfileViewModel
import com.example.ecommerce.features.userprofile.presentation.viewmodel.userprofile.UserProfileViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class EditProfileFragment : DialogFragment() {
    private lateinit var radioEditName: RadioButton
    private lateinit var radioChangePassword: RadioButton
    private lateinit var layoutEditName: LinearLayout
    private lateinit var layoutChangePassword: LinearLayout
    private lateinit var firstNameTextFieldLayout: TextInputLayout
    private lateinit var lastNameTextFieldLayout: TextInputLayout
    private lateinit var newPasswordTextFieldLayout: TextInputLayout
    private lateinit var confirmPasswordTextFieldLayout: TextInputLayout
    private lateinit var firstNameTextField: TextInputEditText
    private lateinit var lastNameTextField: TextInputEditText
    private lateinit var newPasswordTextField: TextInputEditText
    private lateinit var confirmPasswordTextField: TextInputEditText
    private lateinit var editButton: Button
    private lateinit var cancelEditButton: Button
    private lateinit var changeButton: Button
    private lateinit var cancelChangeButton: Button
    private lateinit var root: View
    private var idUser by Delegates.notNull<Int>()
    private val loadingDialog by lazy {
        LoadingDialogFragment().getInstance(parentFragmentManager)
    }
    private val userNameDetailsViewModel: IUserNameDetailsProfileViewModel by
    viewModels<UserNameDetailsProfileViewModel>()
    private val userProfileViewModel: IUserProfileViewModel by viewModels<UserProfileViewModel>()
    private lateinit var updateDisplayNameViewModel :UpdateDisplayNameViewModel


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, // Adjust width as needed
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialog?.window?.decorView?.setPadding(0, 0, 0, 0) // Remove default padding
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_edit_profile, null, false)
        initView(view)
        builder.setView(view)
        radioOnCheckedChangeListener()
        buttonCancelOnClickListener()
        updateDisplayNameViewModel = ViewModelProvider(requireActivity())[UpdateDisplayNameViewModel::class.java]
        getUserProfile()
        getUserProfileState()
        updateUserNameDetailsProfile()
        updateUserNameDetailsState()
        return builder.create()
    }

    private fun initView(view: View) {
        radioEditName = view.findViewById(R.id.radioEditName)
        radioChangePassword = view.findViewById(R.id.radioChangePassword)
        layoutEditName = view.findViewById(R.id.layoutEditName)
        layoutChangePassword = view.findViewById(R.id.layoutChangePassword)
        firstNameTextFieldLayout = view.findViewById(R.id.firstNameTextFieldInputLayout)
        lastNameTextFieldLayout = view.findViewById(R.id.lastNameTextFieldInputLayout)
        newPasswordTextFieldLayout = view.findViewById(R.id.newPasswordTextFieldInputLayout)
        confirmPasswordTextFieldLayout = view.findViewById(R.id.confirmPasswordTextFieldInputLayout)
        firstNameTextField = view.findViewById(R.id.firstNameEditText)
        lastNameTextField = view.findViewById(R.id.lastNameEditText)
        newPasswordTextField = view.findViewById(R.id.newPasswordEditText)
        confirmPasswordTextField = view.findViewById(R.id.confirmPasswordEditText)
        editButton = view.findViewById(R.id.buttonEdit)
        cancelEditButton = view.findViewById(R.id.buttonCancelEdit)
        changeButton = view.findViewById(R.id.buttonChangePassword)
        cancelChangeButton = view.findViewById(R.id.buttonCancelChange)
        root = view
    }

    private fun radioOnCheckedChangeListener() {
        radioEditName.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                layoutEditName.visibility = View.VISIBLE
                layoutChangePassword.visibility = View.GONE
            }
        }
        radioChangePassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                layoutEditName.visibility = View.GONE
                layoutChangePassword.visibility = View.VISIBLE
            }
        }
    }


    private fun buttonCancelOnClickListener() {
        cancelEditButton.setOnClickListener { dialog?.dismiss()
        }
        cancelChangeButton.setOnClickListener { dialog?.dismiss()
        }
    }

    private fun getUserProfile() {
        userProfileViewModel.getUserProfile()
    }

    private fun getUserProfileState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userProfileViewModel.userProfileState.collect { state ->
                    getUserProfileUiStates(state)
                }
            }

        }
    }

    private fun getUserProfileUiStates(state: UiState<Any>) {
        when (state) {
            is UiState.Loading -> {
                when (state.source) {
                    "getUserProfile" -> {}
                }

            }
            is UiState.Success -> {
                when (state.source) {
                    "getUserProfile" -> {
                        val userProfile = state.data as UserEntity
                        firstNameTextField.setText(userProfile.firstName)
                        lastNameTextField.setText(userProfile.lastName)
                        idUser = userProfile.userId
                    }
                }
            }
        }
    }

    private fun updateUserNameDetailsProfile() {

        editButton.setOnClickListener {
            val firstName = firstNameTextField.text.toString()
            val lastName = lastNameTextField.text.toString()
            val userUpdateDisplayName =
                firstNameTextField.text.toString() + lastNameTextField.text.toString()
            if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                val updateUserNameDetailsParams = UpdateUserNameDetailsRequestEntity(
                    id = idUser,
                    firstName = firstName,
                    lastName = lastName,
                    displayName = firstName + lastName
                )
                userNameDetailsViewModel.updateUserNameDetails(
                    updateUserNameDetailsParams = updateUserNameDetailsParams
                )

                updateDisplayNameViewModel.updateDisplayName(userUpdateDisplayName)
            }
        }

    }

    private fun updateUserNameDetailsState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userNameDetailsViewModel.userNameDetailsProfileState.collect { state ->
                    updateUserNameDetailsUiState(state)

                }
            }

        }
    }

    private fun updateUserNameDetailsUiState(state: UiState<Any>) {
        when (state) {
            is UiState.Loading -> {
                when (state.source) {
                    "updateUserNameDetails" -> {
                        loadingDialog.showLoading(fragmentManager = parentFragmentManager)
                    }
                }
            }

            is UiState.Success -> {
                when (state.source) {
                    "updateUserNameDetails" -> {


                        loadingDialog.dismissLoading()
                        val navController = findNavController()
                        navController.navigate(R.id.settingFragment)
                        Toast.makeText(
                            requireContext(),
                            "Edit Name successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                }
                }
            }

            is UiState.Error -> {
                when (state.source) {
                    "updateUserNameDetails" -> {
                        loadingDialog.dismissLoading()
                        SnackBarCustom.showSnackbar(
                            view = root,
                            message = state.message,
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.coroutineContext.cancelChildren() // Cancel all child coroutines
    }

}