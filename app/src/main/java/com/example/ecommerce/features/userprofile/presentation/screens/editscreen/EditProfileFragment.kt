package com.example.ecommerce.features.userprofile.presentation.screens.editscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.Edit_profile_result
import com.example.ecommerce.core.constants.Message
import com.example.ecommerce.core.manager.customer.CustomerManager
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.core.utils.checkIsMessageOrResourceId
import com.example.ecommerce.databinding.FragmentEditProfileBinding
import com.example.ecommerce.features.authentication.presentation.event.ChangePasswordEvent
import com.example.ecommerce.features.authentication.presentation.event.ConfirmPasswordEvent
import com.example.ecommerce.features.authentication.presentation.viewmodel.change_password.ChangePasswordViewModel
import com.example.ecommerce.features.authentication.presentation.viewmodel.confirm_password.ConfirmPasswordViewModel
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserDetailsRequestEntity
import com.example.ecommerce.features.userprofile.presentation.event.UserDetailsEvent
import com.example.ecommerce.features.userprofile.presentation.event.UserProfileEvent
import com.example.ecommerce.features.userprofile.presentation.screens.settingscreen.UpdateDisplayNameViewModel
import com.example.ecommerce.features.userprofile.presentation.viewmodel.fetch_update_user_details.UserDetailsViewModel
import com.example.ecommerce.features.userprofile.presentation.viewmodel.userprofile.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class EditProfileFragment : DialogFragment() {
    private lateinit var root: View
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private var idUser by Delegates.notNull<Int>()
    // private lateinit var loadingDialog: LoadingDialogFragment

    @Inject
    lateinit var customerId: CustomerManager
    private val userDetailsViewModel by viewModels<UserDetailsViewModel>()
    private val userProfileViewModel by viewModels<UserProfileViewModel>()
    private val changePasswordViewModel by viewModels<ChangePasswordViewModel>()
    private val confirmPasswordViewModel by viewModels<ConfirmPasswordViewModel>()
    private lateinit var updateDisplayNameViewModel: UpdateDisplayNameViewModel


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialog?.window?.decorView?.setPadding(0, 0, 0, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        root = binding.root
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //loadingDialog = LoadingDialogFragment.getInstance(childFragmentManager)
        radioOnCheckedChangeListener()
        buttonCancelOnClickListener()
        getUserProfile()
        getUserProfileState()
        getUserProfileEvent()
        updateUserNameDetailsProfile()
        userDetailsState()
        userDetailsEvent()
        changePasswordState()
        changePasswordEvent()
        confirmPasswordState()
        confirmPasswordEvent()

        updateDisplayNameViewModel =
            ViewModelProvider(requireActivity())[UpdateDisplayNameViewModel::class.java]
    }


    private fun radioOnCheckedChangeListener() {
        binding.radioEditName.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.layoutEditName.visibility = View.VISIBLE
                binding.layoutChangePassword.visibility = View.GONE
            }
        }
        binding.radioChangePassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.layoutEditName.visibility = View.GONE
                binding.layoutChangePassword.visibility = View.VISIBLE
            }
        }
    }


    private fun buttonCancelOnClickListener() {
        binding.CancelChangeButton.setOnClickListener {
            dialog?.dismiss()
        }
        binding.CancelEditButton.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun getUserProfile() {
        userProfileViewModel.onEvent(event = UserProfileEvent.UserProfileLoad)
    }

    private fun getUserProfileState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userProfileViewModel.userProfileState.collect { state ->
                    if (state.userEntity != null) {
                        binding.firstNameEditText.setText(state.userEntity.firstName)
                        binding.lastNameEditText.setText(state.userEntity.lastName)
                        idUser = state.userEntity.userId
                    }
                }
            }

        }
    }

    private fun getUserProfileEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userProfileViewModel.userProfileEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            checkIsMessageOrResourceId(
                                event = event,
                                root = root,
                                context = requireContext()
                            )
                        }

                        else -> Unit
                    }
                }
            }
        }
    }


    private fun updateUserNameDetailsProfile() {
        binding.EditButton.setOnClickListener {
            val firstName = binding.firstNameEditText.text.toString()
            val lastName = binding.lastNameEditText.text.toString()
            val userUpdateDisplayName =
                binding.firstNameEditText.text.toString().plus(" ")
                    .plus(binding.lastNameEditText.text.toString())
            if (firstName.isNotEmpty() && lastName.isNotEmpty()) {

                val updateUserNameDetailsParams = UpdateUserDetailsRequestEntity(
                    id = idUser,
                    firstName = firstName,
                    lastName = lastName,
                    displayName = firstName.plus(" ").plus(lastName)
                )
                userDetailsViewModel.onEvent(
                    UserDetailsEvent.UserDetailsUpdateInput(
                        updateUserNameDetailsParams
                    )
                )
                userDetailsViewModel.onEvent(UserDetailsEvent.UserDetailsUpdateButton)
                updateDisplayNameViewModel.updateDisplayName(userUpdateDisplayName)


            } else {
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = getString(R.string.not_work)
                )
            }
        }

    }

    private fun userDetailsState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userDetailsViewModel.userDetailsState.collect { state ->
                    if (state.isUpdateLoading) {
                        changeStateEditUserDetailsView(false, View.VISIBLE, text = "")
                        changeStateView(false)
                    } else {
                        changeStateEditUserDetailsView(
                            true,
                            View.GONE,
                            text = getString(R.string.edit)
                        )
                        changeStateView(true)

                    }
                    if (state.isUpdateSuccess) {
                        dialog?.dismiss()
                    }
                }
            }

        }
    }

    private fun userDetailsEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userDetailsViewModel.userDetailsEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            parentFragmentManager.setFragmentResult(
                                Edit_profile_result, bundleOf(
                                    Message to checkIsMessageOrResourceId(
                                        event = event,
                                        context = requireContext()
                                    )
                                )
                            )
                        }

                        else -> Unit
                    }

                }
            }
        }
    }

    private fun changePasswordState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                changePasswordViewModel.changePasswordState.collect { state ->
                    if (state.isLoading) {
                        changeStateChangePasswordView(false, View.VISIBLE, text = "")
                        changeStateView(false)
                    } else {
                        changeStateChangePasswordView(
                            true,
                            View.GONE,
                            text = getString(R.string.change)
                        )
                        changeStateView(true)
                    }
                    if (state.isFinished) {
                        confirmPasswordInput()
                        changeVisibilityConfirmPasswordView(View.VISIBLE, false)

                    } else {
                        changePasswordInput()
                        changeVisibilityConfirmPasswordView(View.GONE, true)

                    }
                }
            }
        }
    }

    private fun changePasswordEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                changePasswordViewModel.changePasswordEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            parentFragmentManager.setFragmentResult(
                                Edit_profile_result, bundleOf(
                                    Message to checkIsMessageOrResourceId(
                                        event = event,
                                        context = requireContext()
                                    )
                                )
                            )
                        }

                        else -> Unit

                    }
                }
            }

        }
    }

    private fun confirmPasswordState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                confirmPasswordViewModel.confirmPasswordState.collect { state ->
                    if (state.isLoading) {
                        changeStateChangePasswordView(false, View.VISIBLE, text = "")
                        changeStateView(false)

                    } else {
                        changeStateChangePasswordView(
                            true,
                            View.GONE,
                            text = getString(R.string.change)
                        )
                        changeStateView(true)
                    }

                }
            }
        }

    }

    private fun confirmPasswordEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                confirmPasswordViewModel.confirmPasswordEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            parentFragmentManager.setFragmentResult(
                                Edit_profile_result, bundleOf(
                                    Message to checkIsMessageOrResourceId(
                                        event = event,
                                        context = requireContext()
                                    )
                                )
                            )
                        }

                        else -> Unit
                    }

                }
            }
        }
    }

    private fun changeStateEditUserDetailsView(
        isEnable: Boolean,
        visibility: Int,
        text: String
    ) {
        binding.EditButton.isEnabled = isEnable
        binding.EditButton.text = text
        binding.CancelEditButton.isEnabled = isEnable
        binding.progressBarButtonEdit.visibility = visibility
    }

    private fun changeStateChangePasswordView(
        isEnable: Boolean,
        visibility: Int,
        text: String
    ) {
        binding.ChangePasswordButton.isEnabled = isEnable
        binding.ChangePasswordButton.text = text
        binding.CancelChangeButton.isEnabled = isEnable
        binding.progressBarButtonChangePassword.visibility = visibility
    }

    private fun changeVisibilityConfirmPasswordView(visibility: Int, enable: Boolean) {
        binding.passwordTextFieldInputLayout.isEnabled = enable
        binding.newPasswordTextFieldInputLayout.visibility = visibility
        binding.otpTextFieldInputLayout.visibility = visibility
    }

    private fun changeStateView(isEnable: Boolean) {
        binding.radioEditName.isEnabled = isEnable
        binding.radioChangePassword.isEnabled = isEnable
    }

    private fun changePasswordInput() {
        binding.ChangePasswordButton.setOnClickListener {
            val password = binding.passwordEditText.text.toString()
            changePasswordViewModel.onEvent(
                ChangePasswordEvent.PasswordInput(
                    password = password
                )
            )
            changePasswordViewModel.onEvent(
                ChangePasswordEvent.UserIdInput(
                    userId = customerId.getCustomerId()
                )
            )
            changePasswordViewModel.onEvent(ChangePasswordEvent.ChangePasswordButton)
        }
    }

    private fun confirmPasswordInput() {


        binding.ChangePasswordButton.setOnClickListener {
            val password = binding.passwordEditText.text.toString()
            val otpText = binding.otpPasswordEditText.text.toString()

            if (password.isNotEmpty() && otpText.isNotEmpty()) {
                confirmPasswordViewModel.onEvent(
                    ConfirmPasswordEvent.UserIdInput(
                        userId = customerId.getCustomerId()
                    )
                )
                confirmPasswordViewModel.onEvent(
                    ConfirmPasswordEvent.NewPasswordInput(
                        newPassword = password
                    )
                )
                confirmPasswordViewModel.onEvent(
                    ConfirmPasswordEvent.OtpInput(
                        otp = otpText.toInt()
                    )
                )
                confirmPasswordViewModel.onEvent(ConfirmPasswordEvent.ConfirmPasswordButton)
            } else {
                binding.ChangePasswordButton.isEnabled = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.coroutineContext.cancelChildren()
        _binding = null
    }

}