package com.example.fotogram

import androidx.compose.runtime.Recomposer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.request.request
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val requestManager: RequestManager, private val dataStoreManager: DataStoreManager): ViewModel() {
    private val _profileData = MutableStateFlow<ProfileDetailsResponse?>(null)
    val profileData: StateFlow<ProfileDetailsResponse?> = _profileData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            val uid = dataStoreManager.getUID()
            if (uid != null) {
                val details = requestManager.getUserDetailsRequest(uid)
                _profileData.value = details
            }
            _isLoading.value = false
        }
    }
}