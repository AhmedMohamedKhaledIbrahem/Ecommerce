package com.example.ecommerce.features.authentication.presentation.screens.checkverificationcodescreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Button
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
import com.example.ecommerce.databinding.ActivityCheckVerificationCodeBinding
import com.example.ecommerce.features.authentication.domain.entites.CheckVerificationRequestEntity
import com.example.ecommerce.features.authentication.presentation.screens.loginscreen.LoginActivity
import com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel.AuthenticationViewModel
import com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel.IAuthenticationViewModel
import com.example.ecommerce.features.authentication.presentation.viewmodel.resendcodetimerviewmodel.IResendCodeTimerViewModel
import com.example.ecommerce.features.authentication.presentation.viewmodel.resendcodetimerviewmodel.ResendCodeTimerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckVerificationCodeActivity : AppCompatActivity() {
    private val networkStatusViewModel: NetworkStatuesHelperViewModel by viewModels()
    private val authenticationViewModel: IAuthenticationViewModel by viewModels<AuthenticationViewModel>()
    private val resendCodeTimerViewModel: IResendCodeTimerViewModel by viewModels<ResendCodeTimerViewModel>()
    private lateinit var loadingDialog: LoadingDialogFragment
    private lateinit var binding: ActivityCheckVerificationCodeBinding
    private lateinit var rootView: View
    private lateinit var verifyButton: Button
    private var email: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckVerificationCodeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        loadingDialog = LoadingDialogFragment.getInstance(supportFragmentManager)
        initView()
        initIntents()
        resendCodeTimer()
        setupOtpEditTexts()
        checkInternetStatus()
        verificationByCodeAndEmail()
        verificationAuthentication()

    }

    private fun initIntents() {

        email = intent.getStringExtra("emailFromSignUp")
            ?: intent.getStringExtra("emailFromLogin")
    }

    private fun initView() {
        rootView = binding.root
    }

    private fun checkInternetStatus() {
        NetworkStatus.checkInternetConnection(
            lifecycleOwner = this@CheckVerificationCodeActivity,
            networkStatus = networkStatusViewModel.networkStatus,
            loadingDialog = loadingDialog,
            fragmentManager = supportFragmentManager,
            rootView = rootView,
        )
    }

    private fun verificationByCodeAndEmail() {


        verifyButton.setOnClickListener {
            val digit1 = binding.otpDigital1TextInputEditText.text.toString()
            val digit2 = binding.otpDigital2TextInputEditText.text.toString()
            val digit3 = binding.otpDigital3TextInputEditText.text.toString()
            val digit4 = binding.otpDigital4TextInputEditText.text.toString()
            val digit5 = binding.otpDigital5TextInputEditText.text.toString()
            val digit6 = binding.otpDigital6TextInputEditText.text.toString()
            val otp = digit1 + digit2 + digit3 + digit4 + digit5 + digit6
            if (validateInputs()) {
                val checkVerificationCodeParams = email?.let { email ->
                    CheckVerificationRequestEntity(
                        email = email,
                        code = otp,
                    )

                }
                if (checkVerificationCodeParams != null) {
                    authenticationViewModel.checkVerificationCode(checkVerificationCodeParams = checkVerificationCodeParams)
                }
            }
        }
    }

    private fun verificationAuthentication() {
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
                                this@CheckVerificationCodeActivity,
                                "Verification Successful you can now login",
                                Toast.LENGTH_SHORT
                            ).show()
                            lifecycleScope.launch {
                                delay(2000L)
                                val intent =
                                    Intent(
                                        this@CheckVerificationCodeActivity,
                                        LoginActivity::class.java
                                    )
                                startActivity(intent)
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
        val digit1 = binding.otpDigital1TextInputEditText.text.toString()
        val digit2 = binding.otpDigital2TextInputEditText.text.toString()
        val digit3 = binding.otpDigital3TextInputEditText.text.toString()
        val digit4 = binding.otpDigital4TextInputEditText.text.toString()
        val digit5 = binding.otpDigital5TextInputEditText.text.toString()
        val digit6 = binding.otpDigital6TextInputEditText.text.toString()
        var isValid = true
        if (
            digit1.isBlank() &&
            digit2.isBlank() &&
            digit3.isBlank() &&
            digit4.isBlank() &&
            digit5.isBlank() &&
            digit6.isBlank()
        ) {
            isValid = false
        }
        return isValid

    }

    private fun setupOtpEditTexts() {
        val otpEditTexts = listOf(
            binding.otpDigital1TextInputEditText,
            binding.otpDigital2TextInputEditText,
            binding.otpDigital3TextInputEditText,
            binding.otpDigital4TextInputEditText,
            binding.otpDigital5TextInputEditText,
            binding.otpDigital6TextInputEditText
        )

        otpEditTexts.forEachIndexed { index, editText ->
            editText.filters = arrayOf(InputFilter.LengthFilter(1))
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrEmpty() && index < otpEditTexts.size - 1) {
                        otpEditTexts[index + 1].requestFocus()
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            editText.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_DEL && editText.text.isNullOrEmpty() && index > 0) {
                    otpEditTexts[index - 1].requestFocus()
                    true
                } else {
                    false
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun resendCodeTimer() {
        resendCodeTimerViewModel.remainingTime.observe(this@CheckVerificationCodeActivity) { timeLeft ->
            if (timeLeft > 0) {
                binding.resendCodeTextView.text =
                    getString(R.string.resend_in) + "${timeLeft / 1000}" + getString(
                        R.string.s
                    )
                binding.resendCodeTextView.isClickable = false
            } else {
                binding.resendCodeTextView.text = getString(R.string.clickToResend)
                binding.resendCodeTextView.isClickable = true
            }

        }
        binding.resendCodeTextView.setOnClickListener {

            if (resendCodeTimerViewModel.isTimerRunning.value == false) {
                resendCodeTimerViewModel.startTimer()

            }
        }
    }
}