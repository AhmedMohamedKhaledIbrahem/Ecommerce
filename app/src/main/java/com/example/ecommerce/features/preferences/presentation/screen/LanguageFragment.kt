package com.example.ecommerce.features.preferences.presentation.screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
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
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.core.utils.PreferencesUtils
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.databinding.FragmentLanguageBinding
import com.example.ecommerce.features.preferences.presentation.event.PreferencesEvent
import com.example.ecommerce.features.preferences.presentation.viewmodel.PreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class LanguageFragment : DialogFragment() {

    private val preferencesViewModel by viewModels<PreferencesViewModel>()
    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!
    private var isUpdatingRadioGroup = false
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialog?.window?.decorView?.setPadding(0, 0, 0, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLanguage()
        languageRadioGroupOnCheckedChangeListener()
        preferencesState()
        preferencesEvent()
    }


    private fun languageRadioGroupOnCheckedChangeListener() {
        binding.radioGroupLanguage.setOnCheckedChangeListener { _, checkedId ->
            if (isUpdatingRadioGroup) return@setOnCheckedChangeListener
            val selectedLanguageCode = languageCodeMap[checkedId]
            PreferencesUtils.languageCode = selectedLanguageCode
            preferencesViewModel.onEvent(
                PreferencesEvent.Input.SetLanguage(
                    selectedLanguageCode ?: Locale.getDefault().language
                )
            )
            preferencesViewModel.onEvent(PreferencesEvent.Button.LanguageButton)
        }
    }

    private fun getLanguage() {
        preferencesViewModel.onEvent(PreferencesEvent.Get.GetLanguage)
    }

    private fun preferencesState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                preferencesViewModel.preferencesState.collect { state ->
                    if (state.languageCode.isNotEmpty()) {
                        languageRadioGroup(state.languageCode)
                    }
                    if (state.isFinished) {
                        updateLocale()
                    }


                }
            }
        }
    }

    private fun preferencesEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                preferencesViewModel.preferencesEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            SnackBarCustom.showSnackbar(
                                view = binding.root,
                                message = event.message
                            )
                        }

                        else -> Unit
                    }
                }
            }
        }
    }


    private fun languageRadioGroup(languageCode: String) {
        PreferencesUtils.languageCode = languageCode
        val selectedRadioButtonId =
            languageCodeMap.entries.find { it.value == languageCode }?.key
        isUpdatingRadioGroup = true
        selectedRadioButtonId?.let {
            if (binding.radioGroupLanguage.checkedRadioButtonId != it) {
                binding.radioGroupLanguage.check(it)
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