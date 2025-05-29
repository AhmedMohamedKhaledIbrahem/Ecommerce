package com.example.ecommerce.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.ecommerce.databinding.FragmentLoadingDialogBinding


class LoadingDialogFragment : DialogFragment() {
    private var _binding: FragmentLoadingDialogBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun getInstance(fragmentManager: FragmentManager): LoadingDialogFragment {
            return fragmentManager.findFragmentByTag(TAG) as? LoadingDialogFragment
                ?: LoadingDialogFragment()
        }

        private const val TAG = "loadingDialog"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun showLoading(fragmentManager: FragmentManager) {
        if (!isAdded && !fragmentManager.isStateSaved) {
            show(fragmentManager, TAG)
        }
    }

    fun dismissLoading() {
        if (isAdded && dialog?.isShowing == true) {
            dismissAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}