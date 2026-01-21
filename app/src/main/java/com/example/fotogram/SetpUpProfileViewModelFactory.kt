package com.example.fotogram

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SetUpProfileViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetUpProfileViewModel::class.java)) {
            val dataStoreManager = DataStoreManager(context)
            val requestManager = RequestManager(dataStoreManager)
            @Suppress("UNCHECKED_CAST")
            return SetUpProfileViewModel(requestManager, dataStoreManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}