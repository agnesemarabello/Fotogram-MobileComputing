package com.example.fotogram

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FeedViewModel(private val requestManager: RequestManager) : ViewModel() {

    private val _posts = MutableStateFlow<List<FeedPostUI>>(emptyList())
    val posts: StateFlow<List<FeedPostUI>> = _posts.asStateFlow()

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
               val aggregatedPost = postPreview.mapNotNull { preview ->
                   val post = requestManager.getPostByIdRequest(preview.id)
                   if(post != null) {
                       val authorDetails = requestManager.getUserDetailsRequest(post.authorId)
                       if(authorDetails != null) {
                           FeedPostUI(
                               post = post,
                               authorUsername = authorDetails.username,
                               authorProfilePicture = authorDetails.profilePicture,
                               isFollowingAuthor = preview.areYouFollowingAuthor
                           )
                       } else {
                           Log.e("FeedViewModel", "Dettagli autore non trovati per l'ID ${post.authorId}")
                           null
                       }
                   } else {
                          Log.e("FeedViewModel", "Post non trovato per l'ID ${preview.id}")
                          null
                   }
               }
                _posts.value = aggregatedPost
            } else {
                Log.e("FeedViewModel", "Caricamento feed preview fallito")
            }

            _isLoading.value = false
        }
    }

}