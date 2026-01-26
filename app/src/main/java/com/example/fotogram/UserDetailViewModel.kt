package com.example.fotogram

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserDetailViewModel(private val requestManager: RequestManager) : ViewModel() {
    private val _userData = MutableStateFlow<ProfileDetailsResponse?>(null)
    val userData: StateFlow<ProfileDetailsResponse?> = _userData.asStateFlow()

    private val _userPosts = MutableStateFlow<List<Post>>(emptyList())
    val userPosts: StateFlow<List<Post>> = _userPosts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadUser(userId: Int) {
        Log.i("DETTAGLI_UTENTE", "Caricamento dettagli utente per userId: $userId -> ${_isLoading.value}")
        if(_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            val details = requestManager.getUserDetailsRequest(userId)
            Log.i("DETTAGLI_UTENTE", "Dettagli utente caricati: $details")
            if(details != null) {
                _userData.value = details
                val postIds = requestManager.getUserPostsRequest(userId)
                val loadedPosts = postIds?.mapNotNull { postId ->
                    requestManager.getPostByIdRequest(postId)
                } ?: emptyList()

                _userPosts.value = loadedPosts
            }
            _isLoading.value = false
        }
    }
    fun toggleFollow(
        userId: Int,
        isCurrentlyFollowing: Boolean,
        onResult: (Int, Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val success = if(isCurrentlyFollowing) {
                requestManager.unfollowUserRequest(userId)
            } else {
                requestManager.followUserRequest(userId)
            }
            if(success) {
                val updateDetails = requestManager.getUserDetailsRequest(userId)
                if(updateDetails != null) {
                    _userData.value = updateDetails
                    onResult(userId, !isCurrentlyFollowing)
                }
                _userPosts.value = _userPosts.value.map {it}
            }
        }
    }
}