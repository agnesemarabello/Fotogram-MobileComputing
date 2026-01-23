package com.example.fotogram

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            val dataStoreManager = DataStoreManager(context)
            val requestManager = RequestManager(dataStoreManager)
            val postRepository = PostRepository(requestManager)
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(requestManager, dataStoreManager, postRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}