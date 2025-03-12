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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ecommerce.MainNavigationActivity
import com.example.ecommerce.R
import com.example.ecommerce.core.customer.CustomerManager
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.network.NetworkStatuesHelperViewModel
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.core.utils.NetworkStatus
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.presentation.screens.checkverificationcodescreen.CheckVerificationCodeActivity
import com.example.ecommerce.features.authentication.presentation.screens.forgetpasswordscreen.ForgetPasswordActivity
import com.example.ecommerce.features.authentication.presentation.screens.signupscreen.SignUpActivity
import com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel.AuthenticationViewModel
import com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel.IAuthenticationViewModel
import com.example.ecommerce.features.notification.presentation.viewmodel.notification.INotificationViewModel
import com.example.ecommerce.features.notification.presentation.viewmodel.notification.NotificationViewModel
import com.example.ecommerce.features.notification.presentation.viewmodel.notificationmanager.INotificationManagerViewModel
import com.example.ecommerce.features.notification.presentation.viewmodel.notificationmanager.NotificationManagerViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    private val notificationViewModel: INotificationViewModel by viewModels<NotificationViewModel>()
    private val notificationManagerViewModel: INotificationManagerViewModel by viewModels<NotificationManagerViewModel>()

    @Inject
    lateinit var customerManager: CustomerManager
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
        loginAuthenticationUiState()
        notificationUiState()
        notificationManagerUiState()


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
            if (validateInputs()) {
                val loginRequestEntity =
                    AuthenticationRequestEntity(
                        userName = username,
                        password = password
                    )
                authenticationViewModel.login(loginRequestEntity)
            }
        }
    }

    private fun loginAuthenticationUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authenticationViewModel.authenticationState.collect { state ->
                    loginAuthenticationState(state)
                }
            }
        }

    }

    private fun notificationUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                notificationViewModel.notificationState.collect { state ->
                    notificationState(state)
                }
            }
        }
    }

    private fun notificationManagerUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                notificationManagerViewModel.notificationManagerState.collect { state ->
                    notificationManagerState(state)
                }
            }
        }
    }


    private fun loginAuthenticationState(state: UiState<Any>) {
        when (state) {

            is UiState.Loading -> {
                loginAuthenticationLoadingState(state)
            }

            is UiState.Success -> {
                loginAuthenticationSuccessState(state)
            }

            is UiState.Error -> {
                loginAuthenticationErrorState(state)
            }
        }
    }

    private fun notificationState(state: UiState<Any>) {
        when (state) {
            is UiState.Loading -> {
                notificationLoadingState(state)
            }

            is UiState.Success -> {
                notificationSuccessState(state)
            }

            is UiState.Error -> {
                notificationErrorState(state)
            }
        }
    }

    private fun notificationManagerState(state: UiState<Any>) {
        when (state) {
            is UiState.Loading -> {
                notificationManagerLoadingState(state)
            }

            is UiState.Success -> {
                notificationManagerSuccessState(state)
            }

            is UiState.Error -> {
                notificationManagerErrorState(state)
            }
        }
    }

    private fun loginAuthenticationLoadingState(state: UiState.Loading) {
        when (state.source) {
            "login" -> {
                loadingDialog.showLoading(fragmentManager = supportFragmentManager)
            }
        }
    }
    private fun notificationManagerLoadingState(state: UiState.Loading) {
        when (state.source) {
            "getFcmTokenDevice" -> {}
        }
    }
    private fun notificationLoadingState(state: UiState.Loading) {
        when (state.source) {
            "addFcmTokenDevice" -> {}
        }
    }
    private fun loginAuthenticationSuccessState(state: UiState.Success<Any>) {
        when (state.source) {
            "login" -> {
                loadingDialog.dismissLoading()
                val response = state.data as AuthenticationResponseEntity
                when (response.verificationStatues) {
                    true -> {
                        customerManager.setCustomerId(response.userId)
                        notificationManagerViewModel.getFcmTokenDevice()
                        val intent = Intent(
                            this@LoginActivity,
                            MainNavigationActivity::class.java
                        )

                        startActivity(intent)
                        finish()
                    }

                    false -> {
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

            }
        }
    }

    private fun notificationManagerSuccessState(state: UiState.Success<Any>) {
        when (state.source) {
            "getFcmTokenDevice" -> {
                val token = state.data as? String
                token?.let { notificationViewModel.addFcmTokenDevice(token = it) }
            }
        }
    }

    private fun notificationSuccessState(state: UiState.Success<Any>) {
        when (state.source) {
            "addFcmTokenDevice" -> {
//                val token = state.data as? String
//                token?.let { notificationViewModel.addFcmTokenDevice(token = it) }
            }
        }
    }




    private fun loginAuthenticationErrorState(state: UiState.Error) {
        when (state.source) {
            "login" -> {
                Log.e("state3", "$state")
                loadingDialog.dismissLoading()
                SnackBarCustom.showSnackbar(
                    view = rootView,
                    message = state.message
                )
            }
        }
    }

    private fun notificationErrorState(state: UiState.Error) {
        when (state.source) {
            "addFcmTokenDevice" -> {
                SnackBarCustom.showSnackbar(
                    view = rootView,
                    message = state.message
                )
            }
        }
    }

    private fun notificationManagerErrorState(state: UiState.Error) {
        when (state.source) {
            "getFcmTokenDevice" -> {
                SnackBarCustom.showSnackbar(
                    view = rootView,
                    message = state.message
                )
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