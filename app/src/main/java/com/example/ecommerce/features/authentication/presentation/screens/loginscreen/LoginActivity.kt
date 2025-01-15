package com.example.ecommerce.features.authentication.presentation.screens.loginscreen

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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ecommerce.MainNavigationActivity
import com.example.ecommerce.R
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.network.NetworkStatuesHelperViewModel
import com.example.ecommerce.core.utils.NetworkStatus
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.presentation.screens.signupscreen.SignUpActivity
import com.example.ecommerce.features.authentication.presentation.screens.checkverificationcodescreen.CheckVerificationCodeActivity
import com.example.ecommerce.features.authentication.presentation.screens.forgetpasswordscreen.ForgetPasswordActivity
import com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel.AuthenticationViewModel
import com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel.IAuthenticationViewModel
import com.example.ecommerce.core.state.UiState
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var userNameOrEmailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var userNameOrEmailTextFieldLayout: TextInputLayout
    private lateinit var passwordTextFieldLayout: TextInputLayout
    private lateinit var signUpTextView: TextView
    private lateinit var forgetPasswordTextView: TextView
    private lateinit var loginButton: Button
    private val networkStatusViewModel: NetworkStatuesHelperViewModel by viewModels()
    private val authenticationViewModel: IAuthenticationViewModel by viewModels<AuthenticationViewModel>()
    private lateinit var authenticationObserver: Observer<UiState<Any>>
    private val loadingDialog by lazy {
        LoadingDialogFragment().getInstance(supportFragmentManager)
    }
    private lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        initView()
        checkInternetStatus()
        textWatchers()
        navigateToSignUpActivity()
        navigateToForgetPasswordActivity()
        loginWithUserNameOrEmailAndPassword()
        loginAuthentication()


    }

    private fun initView() {
        userNameOrEmailEditText = findViewById(R.id.userNameOrEmailEditText)
        userNameOrEmailTextFieldLayout = findViewById(R.id.userNameOrEmailTextFieldInputLayout)
        passwordEditText = findViewById(R.id.passwordLoginEditText)
        passwordTextFieldLayout = findViewById(R.id.passwordLoginTextFieldInputLayout)
        signUpTextView = findViewById(R.id.SignUpTextView)
        loginButton = findViewById(R.id.loginButton)
        forgetPasswordTextView = findViewById(R.id.forgetPasswordTextView)
        rootView = findViewById(android.R.id.content)
    }

    private fun checkInternetStatus() {
        NetworkStatus.checkInternetConnection(
            lifecycleOwner = this@LoginActivity,
            networkStatus = networkStatusViewModel.networkStatus,
            loadingDialog = loadingDialog,
            fragmentManager = supportFragmentManager,
            rootView = rootView,
        )
    }

    private fun loginWithUserNameOrEmailAndPassword() {


        loginButton.setOnClickListener {
            val username = userNameOrEmailEditText.text.toString()
            val password = passwordEditText.text.toString()
            Log.e(
                "loginWithUserNameOrEmailAndPassword",
                "loginWithUserNameOrEmailAndPassword:$username ",
            )
            if (validateInputs()) {
                val loginRequestEntity =
                    AuthenticationRequestEntity(
                        userName = username,
                        password = password
                    )
                Log.e(
                    "loginWithUserNameOrEmailAndPassword",
                    "loginWithUserNameOrEmailAndPassword:${loginRequestEntity.userName} ",
                )

                authenticationViewModel.login(loginRequestEntity)
            }
        }
    }

    private fun loginAuthentication() {
        rootView = findViewById(android.R.id.content)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authenticationViewModel.authenticationState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            Log.e("state1", "$state")
                            loadingDialog.showLoading(fragmentManager = supportFragmentManager)
                        }
                        is UiState.Success -> {
                            loadingDialog.dismissLoading()
                            val response = state.data as AuthenticationResponseEntity
                            if (response.verificationStatues) {
                                val intent = Intent(
                                    this@LoginActivity,
                                    MainNavigationActivity::class.java
                                )
                                startActivity(intent)
                                finish()
                            } else {
                                val email = state.data.userEmail
                                val sendVerificationParams = EmailRequestEntity(email = email)
                                authenticationViewModel.sendVerificationCode(
                                    sendVerificationCodeParams = sendVerificationParams
                                )
                                val intent = Intent(
                                    this@LoginActivity,
                                    CheckVerificationCodeActivity::class.java
                                ).apply {
                                    putExtra("emailFromLogin", email)
                                }
                                lifecycleScope.launch {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "check your email",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    delay(1500L)
                                    startActivity(intent)
                                }
                            }

                        }

                        is UiState.Error -> {
                            Log.e("state3", "$state")
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

    private fun validateInputs(): Boolean {
        var isValid = true
        val userName = userNameOrEmailEditText.text.toString()
        if (userName.isBlank()) {

            userNameOrEmailTextFieldLayout.error = getString(R.string.usernameOrEmailisRequired)
            userNameOrEmailTextFieldLayout.errorIconDrawable = null
            isValid = false
        } else {
            userNameOrEmailTextFieldLayout.error = null
        }

        val password = passwordEditText.text.toString()
        if (password.isBlank()) {
            passwordTextFieldLayout.error = getString(R.string.password_is_required)
            passwordTextFieldLayout.errorIconDrawable = null
            isValid = false
        } else {
            passwordTextFieldLayout.error = null
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
        userNameOrEmailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
    }

    private fun navigateToSignUpActivity() {
        signUpTextView.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToForgetPasswordActivity() {
        forgetPasswordTextView.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}