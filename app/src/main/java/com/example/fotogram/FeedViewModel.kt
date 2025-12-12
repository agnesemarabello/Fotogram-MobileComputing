package com.example.fotogram

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FeedViewModel(private val requestManager: RequestManager) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadFeed()
    }

    fun loadFeed() {
        if(_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true

            val postPreview = requestManager.getFeedPreviews()

            if(postPreview != null) {
                val fetchedPosts = mutableListOf<Post>()
                val postsDeferred = postPreview.mapNotNull { preview ->
                    requestManager.getPostByIdRequest(preview.id)
                }
                _posts.value = postsDeferred
            } else {
                Log.i("FeedViewModel", "Errore nel caricamento del feed -> anteprime non disponibili.")
            }

            _isLoading.value = false
        }
    }

}