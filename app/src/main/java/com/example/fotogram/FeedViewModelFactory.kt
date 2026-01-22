package com.example.fotogram

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel

class FeedViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            val dataStoreManager = DataStoreManager(context)
            val requestManager = RequestManager(dataStoreManager)
            val postRepository = PostRepository(requestManager)

            @Suppress("UNCHECKED_CAST")
            return FeedViewModel(requestManager, postRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}