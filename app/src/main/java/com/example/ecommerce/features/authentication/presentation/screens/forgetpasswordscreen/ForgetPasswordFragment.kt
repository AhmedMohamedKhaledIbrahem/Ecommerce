package com.example.ecommerce.features.authentication.presentation.screens.forgetpasswordscreen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.core.ui.event.combinedEvents
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.databinding.FragmentForgetPasswordBinding
import com.example.ecommerce.features.authentication.presentation.event.ForgetPasswordEvent
import com.example.ecommerce.features.authentication.presentation.viewmodel.forgetpassowrd.ForgetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment() {
    private val forgetPasswordViewModel: ForgetPasswordViewModel by viewModels()
    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgetPasswordBinding.inflate(layoutInflater)
        rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textWatchers()
        forgetPasswordWithEmail()
        forgetPasswordState()
        forgetPasswordEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun forgetPasswordWithEmail() {
        binding.forgetPasswordButton.setOnClickListener {
            val email = binding.emailForgetPasswordEditText.text.toString()
            if (validateInputs()) {
                forgetPasswordViewModel.onEvent(ForgetPasswordEvent.EmailInput(email))
                forgetPasswordViewModel.onEvent(ForgetPasswordEvent.ForgetPasswordButton)

            }
        }
    }

    private fun forgetPasswordEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                forgetPasswordViewModel.forgetPasswordEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            SnackBarCustom.showSnackbar(view = rootView, message = event.message)
                        }
                        is UiEvent.CombinedEvents -> {
                            combinedEvents(
                                events = event.events,
                                onShowSnackBar = {
                                    SnackBarCustom.showSnackbar(view = rootView, message = it)
                                },
                                onNavigate = { destinationId ,args->
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

    private fun forgetPasswordState() {
        val button = binding.forgetPasswordButton
        val progress = binding.forgetPasswordButtonProgress
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                forgetPasswordViewModel.forgetPasswordState.collect { state ->
                    button.isEnabled = !state.isLoading
                    if (state.isLoading) {
                        button.text = ""
                        progress.visibility = View.VISIBLE
                    } else {
                        button.text = getString(R.string.send)
                        progress.visibility = View.GONE
                    }
                }
            }

        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        val email = binding.emailForgetPasswordEditText.text.toString()
        if (email.isBlank()) {
            binding.emailForgetPasswordTextFieldInputLayout.error =
                getString(R.string.usernameOrEmailisRequired)
            binding.emailForgetPasswordTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else if (!emailPattern.matcher(email).matches()) {
            binding.emailForgetPasswordTextFieldInputLayout.error =
                getString(R.string.invalid_email_format)
            binding.emailForgetPasswordTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            binding.emailForgetPasswordTextFieldInputLayout.error = null
        }


        return isValid
    }

    private fun textWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                validateInputs()
            }
        }
        binding.emailForgetPasswordEditText.addTextChangedListener(textWatcher)

    }


}