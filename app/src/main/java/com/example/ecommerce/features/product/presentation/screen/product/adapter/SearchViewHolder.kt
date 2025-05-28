package com.example.ecommerce.features.product.presentation.screen.product.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ItemSearchBarBinding

class SearchViewHolder(
    private val binding: ItemSearchBarBinding,
    private val onSearchQueryChanged: (String) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    private val searchEditText = binding.searchProduct
    private val context = binding.root.context


    fun bind(query: String) {

        searchEditText.setText(query)
        // Text change listener
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { onSearchQueryChanged(it.toString()) }
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })


        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                searchEditText.clearFocus()
                true
            } else {
                false
            }
        }
    }


    private fun hideKeyboard() {
        val inputMethodManager =
            binding.root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

}