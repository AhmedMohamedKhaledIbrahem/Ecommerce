package com.example.ecommerce.features.userprofile.presentation.screens.editscreen

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
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
import com.example.ecommerce.core.ui.state.UiState
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.databinding.FragmentEditProfileBinding
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity
import com.example.ecommerce.features.userprofile.presentation.screens.settingscreen.UpdateDisplayNameViewModel
import com.example.ecommerce.features.userprofile.presentation.viewmodel.usernamedetailsprofile.IUserNameDetailsProfileViewModel
import com.example.ecommerce.features.userprofile.presentation.viewmodel.usernamedetailsprofile.UserNameDetailsProfileViewModel
import com.example.ecommerce.features.userprofile.presentation.viewmodel.userprofile.IUserProfileViewModel
import com.example.ecommerce.features.userprofile.presentation.viewmodel.userprofile.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class EditProfileFragment : DialogFragment() {
    private lateinit var root: View
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private var idUser by Delegates.notNull<Int>()
    private lateinit var loadingDialog: LoadingDialogFragment

    private val userNameDetailsViewModel: IUserNameDetailsProfileViewModel by
    viewModels<UserNameDetailsProfileViewModel>()
    private val userProfileViewModel: IUserProfileViewModel by viewModels<UserProfileViewModel>()
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        _binding = FragmentEditProfileBinding.inflate(inflater, null, false)
        root = binding.root
        builder.setView(root)
        return builder.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialogFragment.getInstance(childFragmentManager)
        radioOnCheckedChangeListener()
        buttonCancelOnClickListener()
        getUserProfile()
        getUserProfileState()
        updateUserNameDetailsProfile()
        updateUserNameDetailsState()
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
        binding.buttonCancelEdit.setOnClickListener {
            dialog?.dismiss()
        }
        binding.buttonCancelChange.setOnClickListener {
            dialog?.dismiss()
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
                        binding.firstNameEditText.setText(userProfile.firstName)
                        binding.lastNameEditText.setText(userProfile.lastName)
                        idUser = userProfile.userId
                    }
                }
            }
        }
    }

    private fun updateUserNameDetailsProfile() {

        binding.buttonEdit.setOnClickListener {
            val firstName = binding.firstNameEditText.text.toString()
            val lastName = binding.lastNameEditText.text.toString()
            val userUpdateDisplayName =
                binding.firstNameEditText.text.toString()
                    .plus(binding.lastNameEditText.text.toString())
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
        lifecycleScope.coroutineContext.cancelChildren()
        _binding = null
    }

}