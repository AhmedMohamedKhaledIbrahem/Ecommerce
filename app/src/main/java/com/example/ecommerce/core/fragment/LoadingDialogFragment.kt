package com.example.ecommerce.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.ecommerce.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LoadingDialogFragment : DialogFragment() {
    companion object {
        private var instance: LoadingDialogFragment? = null
    }
    fun getInstance():LoadingDialogFragment{
        if (instance ==null){
            instance = LoadingDialogFragment()
        }
        return instance!!
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make the dialog non-cancelable
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
    fun showLoading(fragmentManager: FragmentManager) {
        if (!isAdded) {
            this.show(fragmentManager, "loadingDialog")
        }
    }

    fun dismissLoading() {
        if (isAdded) {
            this.dismiss()
        }
    }

}