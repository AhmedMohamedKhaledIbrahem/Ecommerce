package com.example.ecommerce.features.authentication.presentation.screens.signupscreen

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
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.core.ui.event.combinedEvents
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.databinding.FragmentSignUpBinding
import com.example.ecommerce.features.authentication.presentation.event.SignUpEvent
import com.example.ecommerce.features.authentication.presentation.viewmodel.signup.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private val signUpViewModel: SignUpViewModel by viewModels()
    private lateinit var rootView: View
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(layoutInflater)
        rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textWatchers()
        signUp()
        signInNavigation()
        signUpState()
        signUpEvent()
    }

    private fun signUpEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                signUpViewModel.signUpEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            SnackBarCustom.showSnackbar(view = rootView, message = event.message)
                        }

                        is UiEvent.Navigation.SignIn -> {
                            findNavController().navigate(event.destinationId)
                        }

                        is UiEvent.CombinedEvents -> {
                            combinedEvents(
                                events = event.events,
                                onShowSnackBar = {message, _ ->
                                    SnackBarCustom.showSnackbar(view = rootView, message = message)
                                },
                                onNavigate = { destinationId, args ->
                                    val action =
                                        SignUpFragmentDirections.actionSignUpFragmentToCheckVerificationCodeFragment(
                                            emailArg = args ?: ""
                                        )
                                    findNavController().navigate(action)
                                }
                            )
                        }

                        else -> Unit
                    }
                }

            }
        }
    }


    private fun signInNavigation() {
        binding.loginTextView.setOnClickListener {
            signUpViewModel.onEvent(SignUpEvent.Button.SignIn)
        }
    }


    private fun signUp() {
        binding.signUpButton.setOnClickListener {
            val username = binding.userNameEditText.text.toString()
            val firsName = binding.firstNameEditText.text.toString()
            val lastName = binding.lastNameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (validateInputs()) {
                signUpViewModel.onEvent(SignUpEvent.Input.UserName(username))
                signUpViewModel.onEvent(SignUpEvent.Input.FirstName(firsName))
                signUpViewModel.onEvent(SignUpEvent.Input.LastName(lastName))
                signUpViewModel.onEvent(SignUpEvent.Input.Email(email))
                signUpViewModel.onEvent(SignUpEvent.Input.Password(password))
                signUpViewModel.onEvent(SignUpEvent.Button.SignUp)
            }
        }
    }

    private fun signUpState() {
        val button = binding.signUpButton
        val progress = binding.signUpButtonProgress
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                signUpViewModel.signUpState.collect { state ->
                    button.isEnabled = !state.isLoading
                    if (state.isLoading) {
                        button.text = ""
                        progress.visibility = View.VISIBLE
                    } else {
                        button.text = getString(R.string.SignUpNow)
                        progress.visibility = View.GONE
                    }
                }

            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        val usernamePattern = Regex("^[a-zA-Z][a-zA-Z0-9_]*$")
        val userName = binding.userNameEditText.text.toString()
        if (userName.isBlank()) {
            binding.userNameTextFieldInputLayout.error = getString(R.string.username_is_required)
            binding.userNameTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else if (!usernamePattern.matches(userName)) {
            binding.userNameTextFieldInputLayout.error =
                getString(R.string.username_must_start_with_a_letter_and_contain_only_letters_numbers_or_underscores)
            binding.userNameTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            binding.userNameTextFieldInputLayout.error = null
        }
        if (binding.firstNameEditText.text.toString().isBlank()) {
            binding.firstNameTextFieldInputLayout.error = getString(R.string.first_name_is_required)
            binding.firstNameTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            binding.firstNameTextFieldInputLayout.error = null
        }
        if (binding.lastNameEditText.text.toString().isBlank()) {
            binding.lastNameTextFieldInputLayout.error = getString(R.string.last_name_is_required)
            binding.lastNameTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            binding.lastNameTextFieldInputLayout.error = null
        }
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        val email = binding.emailEditText.text.toString()
        if (email.isBlank()) {
            binding.emailTextFieldInputLayout.error = getString(R.string.email_is_required)
            binding.emailTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else if (!emailPattern.matcher(email).matches()) {
            binding.emailTextFieldInputLayout.error = getString(R.string.invalid_email_format)
            binding.emailTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            binding.emailTextFieldInputLayout.error = null
        }
        val password = binding.passwordEditText.text.toString()
        if (password.isBlank()) {
            binding.passwordTextFieldInputLayout.error = getString(R.string.password_is_required)
            binding.passwordTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else if (password.length < 6) {
            binding.passwordTextFieldInputLayout.error =
                getString(R.string.password_must_be_at_least_6_characters_long)
            binding.passwordTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            binding.passwordTextFieldInputLayout.error = null
        }

        val confirmPassword = binding.confirmPasswordEditText.text.toString()
        if (confirmPassword.isBlank()) {
            binding.ConfirmPasswordTextFieldInputLayout.error =
                getString(R.string.please_confirm_your_password)
            binding.ConfirmPasswordTextFieldInputLayout.errorIconDrawable = null
            isValid = false

        } else if (password != confirmPassword) {
            binding.ConfirmPasswordTextFieldInputLayout.error =
                getString(R.string.passwords_do_not_match)
            binding.ConfirmPasswordTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            binding.ConfirmPasswordTextFieldInputLayout.error = null
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


        binding.userNameEditText.addTextChangedListener(textWatcher)
        binding.firstNameEditText.addTextChangedListener(textWatcher)
        binding.lastNameEditText.addTextChangedListener(textWatcher)
        binding.emailEditText.addTextChangedListener(textWatcher)
        binding.passwordEditText.addTextChangedListener(textWatcher)
        binding.confirmPasswordEditText.addTextChangedListener(textWatcher)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}