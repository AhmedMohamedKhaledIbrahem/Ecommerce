package com.example.ecommerce.features.authentication.presentation.screens.signupscreen

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ecommerce.R
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.network.NetworkStatuesHelperViewModel
import com.example.ecommerce.core.ui.UiState
import com.example.ecommerce.core.utils.NetworkStatus
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.databinding.ActivitySignUpBinding
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.features.authentication.presentation.screens.checkverificationcodescreen.CheckVerificationCodeActivity
import com.example.ecommerce.features.authentication.presentation.screens.loginscreen.LoginActivity
import com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel.AuthenticationViewModel
import com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel.IAuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private val networkStatusViewModel: NetworkStatuesHelperViewModel by viewModels()
    private val authenticationViewModel: IAuthenticationViewModel by viewModels<AuthenticationViewModel>()
    private lateinit var rootView: View
    private lateinit var loadingDialog: LoadingDialogFragment
    private var emailFromSignUp = ""
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = LoadingDialogFragment.getInstance(supportFragmentManager)
        navigateToLoginActivity()
        checkInternetConnection()
        textWatchers()
        submitUser()
        signUpAuthentication()
    }

    private fun signUpAuthentication() {
        rootView = findViewById(android.R.id.content)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authenticationViewModel.authenticationState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            loadingDialog.showLoading(fragmentManager = supportFragmentManager)
                        }

                        is UiState.Success -> {
                            loadingDialog.dismissLoading()
                            Toast.makeText(
                                this@SignUpActivity,
                                "${state.data}, check your email",
                                Toast.LENGTH_SHORT
                            ).show()

                            val sendVerificationParams = EmailRequestEntity(email = emailFromSignUp)
                            authenticationViewModel.sendVerificationCode(
                                sendVerificationCodeParams = sendVerificationParams
                            )

                            val intent = Intent(
                                this@SignUpActivity,
                                CheckVerificationCodeActivity::class.java
                            ).apply {
                                putExtra("emailFromSignUp", emailFromSignUp)
                            }
                            delay(2000L)
                            startActivity(intent)
                        }

                        is UiState.Error -> {
                            loadingDialog.dismissLoading()
                            SnackBarCustom.showSnackbar(
                                view = rootView,
                                message = state.message
                            )
                        }
                    }
                }
            }
        }
    }


    private fun checkInternetConnection() {
        rootView = findViewById(android.R.id.content)
        NetworkStatus.checkInternetConnection(
            lifecycleOwner = this@SignUpActivity,
            networkStatus = networkStatusViewModel.networkStatus,
            loadingDialog = loadingDialog,
            fragmentManager = supportFragmentManager,
            rootView = rootView
        )
    }


    private fun submitUser() {

        binding.signUpButton.setOnClickListener {
            val username = binding.userNameEditText.text.toString()
            val firsName = binding.firstNameEditText.text.toString()
            val lastName = binding.lastNameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            emailFromSignUp = email
            val password = binding.passwordEditText.text.toString()
            if (validateInputs()) {
                val signUpRequestEntity =
                    SignUpRequestEntity(
                        username,
                        firsName,
                        lastName,
                        email,
                        password
                    )
                authenticationViewModel.signUp(signUpRequestEntity)
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

    private fun navigateToLoginActivity() {
        binding.loginTextView.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}