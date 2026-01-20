package com.example.fotogram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SetUpProfileViewModel(
    private val requestManager: RequestManager,
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun registerUser(
        username: String,
        base64img: String,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val regResponse = requestManager.registrationRequest()
            if (regResponse != null) {
                dataStoreManager.saveSession(regResponse.sessionId, regResponse.userId)
                requestManager.updateProfileRequest(newUsername = username, newBio = "", newDateOfBirth = null)
                if(!base64img.isNullOrEmpty()) {
                    requestManager.updateProfilePictureRequest(base64img)
                }
                onComplete()
            }
            _isLoading.value = false
        }
    }

}