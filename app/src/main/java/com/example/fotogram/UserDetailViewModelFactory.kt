package com.example.fotogram

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
class UserDetailViewModelFactory(private val requestManager: RequestManager): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        return UserDetailViewModel(requestManager) as T
    }
}