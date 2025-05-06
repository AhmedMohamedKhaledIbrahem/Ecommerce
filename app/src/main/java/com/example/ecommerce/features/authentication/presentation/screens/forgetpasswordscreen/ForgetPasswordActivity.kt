package com.example.ecommerce.features.authentication.presentation.screens.forgetpasswordscreen

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
import com.example.ecommerce.databinding.ActivityForgetPasswordBinding
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.presentation.screens.loginscreen.LoginActivity
import com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel.AuthenticationViewModel
import com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel.IAuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgetPasswordActivity : AppCompatActivity() {
    private val networkStatusViewModel: NetworkStatuesHelperViewModel by viewModels()
    private val authenticationViewModel: IAuthenticationViewModel by viewModels<AuthenticationViewModel>()
    private lateinit var loadingDialog: LoadingDialogFragment
    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        loadingDialog = LoadingDialogFragment.getInstance(supportFragmentManager)
        initView()
        checkInternetStatus()
        textWatchers()
        forgetPasswordWithEmail()
        forgetPasswordAuthentication()

    }

    private fun initView() {
        rootView = binding.root
    }

    private fun checkInternetStatus() {
        NetworkStatus.checkInternetConnection(
            lifecycleOwner = this@ForgetPasswordActivity,
            networkStatus = networkStatusViewModel.networkStatus,
            loadingDialog = loadingDialog,
            fragmentManager = supportFragmentManager,
            rootView = rootView,
        )
    }

    private fun forgetPasswordWithEmail() {
        binding.forgetPasswordButton.setOnClickListener {
            val email = binding.emailForgetPasswordEditText.text.toString()
            if (validateInputs()) {
                val resetPasswordParams = EmailRequestEntity(email = email)
                authenticationViewModel.resetPassword(resetPasswordParams = resetPasswordParams)
            }
        }
    }

    private fun forgetPasswordAuthentication() {
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
                                this@ForgetPasswordActivity,
                                "${state.data}",
                                Toast.LENGTH_SHORT
                            ).show()
                            lifecycleScope.launch {
                                delay(1000)
                                naiveteToLoginActivity()
                            }

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

    private fun naiveteToLoginActivity() {
        val intent = Intent(this@ForgetPasswordActivity, LoginActivity::class.java)
        startActivity(intent)

    }
}