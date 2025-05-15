package com.example.ecommerce.features.authentication.presentation.screens.loginscreen

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
import com.example.ecommerce.core.manager.customer.CustomerManager
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.core.ui.event.combinedEvents
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.databinding.FragmentLoginBinding
import com.example.ecommerce.features.authentication.presentation.event.LoginEvent
import com.example.ecommerce.features.authentication.presentation.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel by viewModels<LoginViewModel>()


    @Inject
    lateinit var customerManager: CustomerManager
    private lateinit var loadingDialog: LoadingDialogFragment
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialogFragment.getInstance(childFragmentManager)
        textWatchers()
        loginWithUserNameOrEmailAndPassword()
        signUpNavigation()
        forgetPasswordNavigation()
        loginState()
        loginEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loginWithUserNameOrEmailAndPassword() {
        binding.loginButton.setOnClickListener {
            val username = binding.userNameOrEmailEditText.text.toString()
            val password = binding.passwordLoginEditText.text.toString()
            if (validateInputs()) {
                loginViewModel.onEvent(LoginEvent.UserNameInput(username))
                loginViewModel.onEvent(LoginEvent.PasswordInput(password))
                loginViewModel.onEvent(LoginEvent.Button.SignIn)

            }
        }
    }

    private fun signUpNavigation() {
        binding.SignUpTextView.setOnClickListener {
            loginViewModel.onEvent(LoginEvent.Button.SignUp)
        }
    }

    private fun forgetPasswordNavigation() {
        binding.forgetPasswordTextView.setOnClickListener {
            loginViewModel.onEvent(LoginEvent.Button.ForgetPassword)
        }
    }

    private fun loginEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.loginEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            SnackBarCustom.showSnackbar(view = rootView, message = event.message)
                        }

                        is UiEvent.Navigation.SignUp -> {
                            findNavController().navigate(event.destinationId)
                        }

                        is UiEvent.Navigation.ForgetPassword -> {
                            findNavController().navigate(event.destinationId)
                        }

                        is UiEvent.Navigation.Home -> {
                            findNavController().navigate(event.destinationId)
                        }

                        is UiEvent.CombinedEvents -> {
                            combinedEvents(
                                events = event.events,
                                onShowSnackBar = {
                                    SnackBarCustom.showSnackbar(view = rootView, message = it)
                                },
                                onNavigate = { destinationId, args ->

                                    val action =
                                        LoginFragmentDirections.actionLoginFragmentToCheckVerificationCodeFragment(
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

    private fun loginState() {
        val button = binding.loginButton
        val progress = binding.loginButtonProgress
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.loginState.collect { state ->
                    button.isEnabled = !state.isLoading
                    if (state.isLoading) {
                        button.text = ""
                        progress.visibility = View.VISIBLE
                    } else {
                        button.text = getString(R.string.loginButton)
                        progress.visibility = View.GONE
                    }
                }
            }
        }
    }


    private fun validateInputs(): Boolean {
        var isValid = true
        val userName = binding.userNameOrEmailEditText.text.toString()
        if (userName.isBlank()) {

            binding.userNameOrEmailTextFieldInputLayout.error =
                getString(R.string.usernameOrEmailisRequired)
            binding.userNameOrEmailTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            binding.userNameOrEmailTextFieldInputLayout.error = null
        }

        val password = binding.passwordLoginEditText.text.toString()
        if (password.isBlank()) {
            binding.passwordLoginTextFieldInputLayout.error =
                getString(R.string.password_is_required)
            binding.passwordLoginTextFieldInputLayout.errorIconDrawable = null
            isValid = false
        } else {
            binding.passwordLoginTextFieldInputLayout.error = null
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
        binding.userNameOrEmailEditText.addTextChangedListener(textWatcher)
        binding.passwordLoginEditText.addTextChangedListener(textWatcher)
    }


}