package com.example.ecommerce.features.authentication.presentation.screens.checkverificationcodescreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.UserEmailSaveState
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.core.ui.event.combinedEvents
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.databinding.FragmentCheckVerificationCodeBinding
import com.example.ecommerce.features.authentication.presentation.event.CheckVerificationCodeEvent
import com.example.ecommerce.features.authentication.presentation.viewmodel.checkverificationcode.CheckVerificationCodeViewModel
import com.example.ecommerce.features.authentication.presentation.viewmodel.resendcodetimerviewmodel.IResendCodeTimerViewModel
import com.example.ecommerce.features.authentication.presentation.viewmodel.resendcodetimerviewmodel.ResendCodeTimerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckVerificationCodeFragment : Fragment() {
    private val checkVerificationCodeViewModel: CheckVerificationCodeViewModel by viewModels<CheckVerificationCodeViewModel>()
    private val emailArgs: CheckVerificationCodeFragmentArgs by navArgs()
    private val  email = emailArgs.emailArg
    private val resendCodeTimerViewModel: IResendCodeTimerViewModel by viewModels<ResendCodeTimerViewModel>()
    //private lateinit var loadingDialog: LoadingDialogFragment
    private var _binding: FragmentCheckVerificationCodeBinding? = null
    private val binding get() = _binding!!
    private lateinit var rootView: View


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckVerificationCodeBinding.inflate(layoutInflater)
        rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //loadingDialog = LoadingDialogFragment.getInstance(childFragmentManager)
        resendCodeTimer()
        setupOtpEditTexts()
        verificationByCodeAndEmail()
        verificationState()
        verificationEvent()
    }


    private fun verificationByCodeAndEmail() {


        binding.verifyCodeButton.setOnClickListener {
            val digit1 = binding.otpDigital1TextInputEditText.text.toString()
            val digit2 = binding.otpDigital2TextInputEditText.text.toString()
            val digit3 = binding.otpDigital3TextInputEditText.text.toString()
            val digit4 = binding.otpDigital4TextInputEditText.text.toString()
            val digit5 = binding.otpDigital5TextInputEditText.text.toString()
            val digit6 = binding.otpDigital6TextInputEditText.text.toString()

            if (validateInputs()) {
                checkVerificationCodeViewModel.onEvent(
                    CheckVerificationCodeEvent.Input.Digit1(
                        digit1
                    )
                )
                checkVerificationCodeViewModel.onEvent(
                    CheckVerificationCodeEvent.Input.Digit2(
                        digit2
                    )
                )
                checkVerificationCodeViewModel.onEvent(
                    CheckVerificationCodeEvent.Input.Digit3(
                        digit3
                    )
                )
                checkVerificationCodeViewModel.onEvent(
                    CheckVerificationCodeEvent.Input.Digit4(
                        digit4
                    )
                )
                checkVerificationCodeViewModel.onEvent(
                    CheckVerificationCodeEvent.Input.Digit5(
                        digit5
                    )
                )
                checkVerificationCodeViewModel.onEvent(
                    CheckVerificationCodeEvent.Input.Digit6(
                        digit6
                    )
                )
                checkVerificationCodeViewModel.onEvent(
                    CheckVerificationCodeEvent.Input.Email(
                        email
                    )
                )
                checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.VerifyButton)

            }
        }
    }

    private fun verificationEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkVerificationCodeViewModel.checkVerificationCodeEvent.collect { event ->
                    when (event) {

                        is UiEvent.ShowSnackBar -> {
                            SnackBarCustom.showSnackbar(view = rootView, message = event.message)
                        }

                        is UiEvent.CombinedEvents -> {
                            combinedEvents(
                                events = event.events,
                                onShowSnackBar = { message, _ ->
                                    SnackBarCustom.showSnackbar(view = rootView, message = message)
                                },
                                onNavigate = { destinationId, _ ->
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

    private fun verificationState() {
        val button = binding.verifyCodeButton
        val progress = binding.verifyCodeButtonProgress
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkVerificationCodeViewModel.checkVerificationCodeState.collect { state ->
                    button.isEnabled = !state.isLoading
                    if (state.isLoading) {
                        button.text = ""
                        progress.visibility = View.VISIBLE
                    } else {
                        button.text = getString(R.string.verifyCode)
                        progress.visibility = View.GONE
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
        resendCodeTimerViewModel.remainingTime.observe(viewLifecycleOwner) { timeLeft ->
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