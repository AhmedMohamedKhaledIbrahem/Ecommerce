package com.example.ecommerce.features.preferences.presentation.screen

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ecommerce.MainNavigationActivity

import com.example.ecommerce.R
import com.example.ecommerce.core.constants.languageCodeMap
import com.example.ecommerce.core.ui.state.UiState
import com.example.ecommerce.core.utils.PreferencesUtils
import com.example.ecommerce.databinding.FragmentLanguageBinding
import com.example.ecommerce.features.preferences.presentation.viewmodel.IPreferencesViewModel
import com.example.ecommerce.features.preferences.presentation.viewmodel.PreferencesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class LanguageFragment : DialogFragment() {

    private val preferencesViewModel: IPreferencesViewModel by viewModels<PreferencesViewModel>()
    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!
    private var isUpdatingRadioGroup = false
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, // Adjust width as needed
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialog?.window?.decorView?.setPadding(0, 0, 0, 0) // Remove default padding
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        _binding = FragmentLanguageBinding.inflate(inflater, null, false)
        builder.setView(binding.root)

        return builder.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesStates()
        getLanguage()
        languageRadioGroupOnCheckedChangeListener()
    }


    private fun languageRadioGroupOnCheckedChangeListener() {

        binding.radioGroupLanguage.setOnCheckedChangeListener { _, checkedId ->
            if (isUpdatingRadioGroup) return@setOnCheckedChangeListener
            val selectedLanguageCode = languageCodeMap[checkedId]
            Log.e(selectedLanguageCode, "$selectedLanguageCode: ")
            preferencesViewModel.setLanguage(
                selectedLanguageCode ?: Locale.getDefault().language
            )
            PreferencesUtils.languageCode = selectedLanguageCode

        }

    }

    private fun preferencesStates() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                preferencesViewModel.preferencesState.collect { state ->
                    preferencesUiState(state)
                }
            }
        }
    }

    private fun preferencesUiState(state: UiState<Any>) {
        when (state) {
            is UiState.Loading -> {
                preferencesUiStateLoading(state)
            }

            is UiState.Success -> {
                preferencesUiStateSuccess(state)
            }

            is UiState.Error -> {
                preferencesUiStateError(state)
            }
        }
    }


    private fun preferencesUiStateLoading(state: UiState.Loading) {
        when (state.source) {
            "setLanguage" -> {}

            "getLanguage" -> {}
        }
    }

    private fun preferencesUiStateSuccess(state: UiState.Success<Any>) {
        when (state.source) {
            "setLanguage" -> {
                updateLocale()
            }

            "getLanguage" -> {
                val languageCode = state.data as String
                PreferencesUtils.languageCode = languageCode
                val selectedRadioButtonId =
                    languageCodeMap.entries.find { it.value == languageCode }?.key
                isUpdatingRadioGroup = true
                selectedRadioButtonId?.let {
                    if (binding.radioGroupLanguage.checkedRadioButtonId != it) {
                        binding.radioGroupLanguage.check(it)
                        Log.e("RadioButton", "select:${languageCode}")
                    }
                } ?: run {
                    val defaultLanguageCode = Locale.getDefault().language
                    val radioButtonId =
                        languageCodeMap.entries.find { it.value == defaultLanguageCode }?.key
                    if (radioButtonId != null) {
                        if (binding.radioGroupLanguage.checkedRadioButtonId != radioButtonId) {
                            binding.radioGroupLanguage.check(radioButtonId)
                        }
                    }
                }
                isUpdatingRadioGroup = false
            }
        }
    }

    private fun preferencesUiStateError(state: UiState.Error) {
        when (state.source) {
            "setLanguage" -> {
                // loadingDialog.dismissLoading()
                Snackbar.make(
                    requireView(),
                    state.message,
                    Snackbar.LENGTH_SHORT
                ).show()

            }

            "getLanguage" -> {
                //loadingDialog.dismissLoading()
                Snackbar.make(
                    requireView(),
                    state.message,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getLanguage() {
        preferencesViewModel.getLanguage()
    }

    private fun updateLocale() {
        val intent =
            Intent(requireContext(), MainNavigationActivity::class.java)
        intent.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context?.startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}