package com.example.fotogram

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val requestManager: RequestManager, private val dataStoreManager: DataStoreManager): ViewModel() {
    private val _profileData = MutableStateFlow<ProfileDetailsResponse?>(null)
    val profileData: StateFlow<ProfileDetailsResponse?> = _profileData.asStateFlow()

    private val _userPosts = MutableStateFlow<List<Post>>(emptyList())
    val userPosts: StateFlow<List<Post>> = _userPosts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            val uid = dataStoreManager.getUID()

            Log.d("ProfileViewModel", "UID recuperato: $uid")
            if (uid != null) {
                val details = requestManager.getUserDetailsRequest(uid)
                Log.d("ProfileViewModel", "Dettagli profilo caricati: $details")
                _profileData.value = details

                val postIds = requestManager.getUserPostsRequest(uid)
                if (postIds != null) {
                    val posts = postIds.mapNotNull { id ->
                        requestManager.getSinglePost(id)
                    }
                    Log.d("ProfileViewModel", "Post utente caricati: ${posts.size}")
                    _userPosts.value = posts
                } else {
                    Log.e("ProfileViewModel", "Impossibile caricare i post dell'utente")
                }
            } else {
                Log.e("ProfileViewModel", "UID è null, impossibile caricare i dettagli del profilo")
            }
            _isLoading.value = false
        }
    }
}