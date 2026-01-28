package com.example.fotogram

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 *** ProfileViewModel ***:
Gestisce la logica di business per la schermata del profilo utente
 */
class ProfileViewModel(
    private val requestManager: RequestManager,
    private val dataStoreManager: DataStoreManager,
    private val postRepository: PostRepository
    ): ViewModel() {
    private val _profileData = MutableStateFlow<ProfileDetailsResponse?>(null)
    val profileData: StateFlow<ProfileDetailsResponse?> = _profileData.asStateFlow()

    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation: StateFlow<Location?> = _userLocation.asStateFlow()

    private val _userPosts = MutableStateFlow<List<FeedPostUI>>(emptyList())
    val userPosts: StateFlow<List<FeedPostUI>> = _userPosts.asStateFlow()

    private val _myUserId = MutableStateFlow<Int?>(null)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    //  Recupero dell'UID e caricamento del profilo utente all'inizializzazione
    init {
        viewModelScope.launch {
            _myUserId.value = requestManager.getMyUserId()
        }
        loadUserProfile()
    }

    //  Aggiorna la posizione dell'utente
    fun updateUserLocation(location: Location) {
        _userLocation.value = location
    }

    //  Carica il profilo completo utente
    fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            val uid = dataStoreManager.getUID()

            if (uid != null) {
                val details = requestManager.getUserDetailsRequest(uid)
                Log.d("ProfileViewModel", "Dettagli profilo caricati: $details")
                _profileData.value = details

                val postIds = requestManager.getUserPostsRequest(uid)
                if (postIds != null) {
                    val posts = postIds.mapNotNull { id ->
                        val post = postRepository.getPost(id)
                        if(post != null && details != null) {
                            FeedPostUI(
                                post = post,
                                authorUsername = details.username,
                                authorProfilePicture = details.profilePicture,
                                isFollowingAuthor = false,
                                location = post.location
                            )
                        } else null
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


    //  Aggiorna i dettagli del profilo utente: username, bio, data di nascita e immagine del profilo
    fun updateProfileDetails(username: String, bio: String, dateOfBirth: String, base64img: String?) {
        viewModelScope.launch {
            requestManager.updateProfileRequest(username, bio, dateOfBirth)
            if(base64img != null) {
                requestManager.updateProfilePictureRequest(base64img)
            }
            loadUserProfile()
        }
    }

    //  Crea un nuovo post con immagine, descrizione e posizione opzionale
    fun createNewPost(img: String, description: String, lat: Double?, lon: Double?) {
        viewModelScope.launch {
            Log.d("API_DEBUG", "Memorizzo Laz: $lat, Lon: $lon")
            val success = requestManager.createPostRequest(description, img, lat, lon)
            if(success) {
                postRepository.clearCache()
                loadUserProfile()
            }
        }
    }

}