package com.example.ecommerce.core.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.ecommerce.R


class LoadingDialogFragment : DialogFragment() {

    fun getInstance(fragmentManager: FragmentManager): LoadingDialogFragment {
        val existingDialog =
            fragmentManager.findFragmentByTag("loadingDialog") as? LoadingDialogFragment
        return existingDialog ?: LoadingDialogFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_loading_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    @SuppressLint("SuspiciousIndentation")
    fun showLoading(fragmentManager: FragmentManager) {
        if (!isAdded) {
            if (!fragmentManager.isStateSaved)
                this.show(fragmentManager, "loadingDialog")
        }
    }

    fun dismissLoading() {
//        val existingDialog =
//            parentFragmentManager.findFragmentByTag("loadingDialog") as? LoadingDialogFragment
//        existingDialog?.let {
//            if (it.isAdded) {
//                parentFragmentManager.beginTransaction().remove(it).commitAllowingStateLoss()
//            }
//        }
        if (isAdded){
            dismiss()
        }

    }



}