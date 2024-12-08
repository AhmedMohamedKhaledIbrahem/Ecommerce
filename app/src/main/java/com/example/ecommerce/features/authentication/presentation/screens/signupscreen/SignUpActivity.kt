package com.example.ecommerce.features.authentication.presentation.screens.signupscreen

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
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
import com.example.ecommerce.core.utils.NetworkStatus
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.features.authentication.presentation.screens.checkverificationcodescreen.CheckVerificationCodeActivity
import com.example.ecommerce.features.authentication.presentation.screens.loginscreen.LoginActivity
import com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel.AuthenticationViewModel
import com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel.IAuthenticationViewModel
import com.example.ecommerce.core.state.UiState
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private val networkStatusViewModel: NetworkStatuesHelperViewModel by viewModels()
    private val authenticationViewModel: IAuthenticationViewModel by viewModels<AuthenticationViewModel>()
    private lateinit var rootView: View
    private val loadingDialog by lazy {
        LoadingDialogFragment().getInstance()
    }
    private var emailFromSignUp = ""
    private lateinit var loginNow: TextView
    private lateinit var userNameEditText: TextInputEditText
    private lateinit var firstNameEditText: TextInputEditText
    private lateinit var lastNameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText
    private lateinit var userNameTextFieldLayout: TextInputLayout
    private lateinit var firstNameTextFieldLayout: TextInputLayout
    private lateinit var lastNameTextFieldLayout: TextInputLayout
    private lateinit var emailTextFieldLayout: TextInputLayout
    private lateinit var passwordTextFieldLayout: TextInputLayout
    private lateinit var confirmPasswordTextFieldLayout: TextInputLayout
    private lateinit var signUpButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        initView()
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

    private fun initView() {
        userNameEditText = findViewById(R.id.userNameEditText)
        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        userNameTextFieldLayout = findViewById(R.id.userNameTextFieldInputLayout)
        firstNameTextFieldLayout = findViewById(R.id.firstNameTextFieldInputLayout)
        lastNameTextFieldLayout = findViewById(R.id.lastNameTextFieldInputLayout)
        emailTextFieldLayout = findViewById(R.id.emailTextFieldInputLayout)
        passwordTextFieldLayout = findViewById(R.id.passwordTextFieldInputLayout)
        confirmPasswordTextFieldLayout = findViewById(R.id.ConfirmPasswordTextFieldInputLayout)

        loginNow = findViewById(R.id.loginTextView)
        signUpButton = findViewById(R.id.signUpButton)
    }

    private fun submitUser() {

        signUpButton.setOnClickListener {
            val username = userNameEditText.text.toString()
            val firsName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val email = emailEditText.text.toString()
            emailFromSignUp = email
            val password = passwordEditText.text.toString()
            // val confirmPassword = confirmPasswordEditText.text.toString()
            Log.e("userName", "submitUser:${username} ")
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
        // Username validation (not starting with a number or special character)
        val usernamePattern = Regex("^[a-zA-Z][a-zA-Z0-9_]*$")
        val userName = userNameEditText.text.toString()
        if (userName.isBlank()) {
            //userNameEditText.error = "Username is required"
            userNameTextFieldLayout.error = getString(R.string.username_is_required)
            userNameTextFieldLayout.errorIconDrawable = null
            isValid = false
        } else if (!usernamePattern.matches(userName)) {
            userNameTextFieldLayout.error =
                getString(R.string.username_must_start_with_a_letter_and_contain_only_letters_numbers_or_underscores)
            userNameTextFieldLayout.errorIconDrawable = null
            isValid = false
        } else {
            userNameTextFieldLayout.error = null
        }
        // First Name validation
        if (firstNameEditText.text.toString().isBlank()) {
            firstNameTextFieldLayout.error = getString(R.string.first_name_is_required)
            firstNameTextFieldLayout.errorIconDrawable = null
            isValid = false
        } else {
            firstNameTextFieldLayout.error = null
        }
        // Last Name validation
        if (lastNameEditText.text.toString().isBlank()) {
            lastNameTextFieldLayout.error = getString(R.string.last_name_is_required)
            lastNameTextFieldLayout.errorIconDrawable = null
            isValid = false
        } else {
            lastNameTextFieldLayout.error = null
        }
        // Email validation
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        val email = emailEditText.text.toString()
        if (email.isBlank()) {
            emailTextFieldLayout.error = getString(R.string.email_is_required)
            emailTextFieldLayout.errorIconDrawable = null
            isValid = false
        } else if (!emailPattern.matcher(email).matches()) {
            emailTextFieldLayout.error = getString(R.string.invalid_email_format)
            emailTextFieldLayout.errorIconDrawable = null
            isValid = false
        } else {
            emailTextFieldLayout.error = null
        }
        // Password validation (minimum length)
        val password = passwordEditText.text.toString()
        if (password.isBlank()) {
            passwordTextFieldLayout.error = getString(R.string.password_is_required)
            passwordTextFieldLayout.errorIconDrawable = null
            isValid = false
        } else if (password.length < 6) {
            passwordTextFieldLayout.error =
                getString(R.string.password_must_be_at_least_6_characters_long)
            passwordTextFieldLayout.errorIconDrawable = null
            isValid = false
        } else {
            passwordTextFieldLayout.error = null
        }

        // Confirm Password validation
        val confirmPassword = confirmPasswordEditText.text.toString()
        if (confirmPassword.isBlank()) {
            confirmPasswordTextFieldLayout.error = getString(R.string.please_confirm_your_password)
            confirmPasswordTextFieldLayout.errorIconDrawable = null
            isValid = false

        } else if (password != confirmPassword) {
            confirmPasswordTextFieldLayout.error = getString(R.string.passwords_do_not_match)
            confirmPasswordTextFieldLayout.errorIconDrawable = null
            isValid = false
        } else {
            confirmPasswordTextFieldLayout.error = null
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


        userNameEditText.addTextChangedListener(textWatcher)
        firstNameEditText.addTextChangedListener(textWatcher)
        lastNameEditText.addTextChangedListener(textWatcher)
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
        confirmPasswordEditText.addTextChangedListener(textWatcher)
    }

    private fun navigateToLoginActivity() {
        loginNow.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        //authenticationViewModel.authenticationState.removeObserver(authenticationObserver)
    }
}