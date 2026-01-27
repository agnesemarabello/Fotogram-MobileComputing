package com.example.fotogram

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FeedViewModel(private val requestManager: RequestManager, private val postRepository: PostRepository) : ViewModel() {

    private val _posts = MutableStateFlow<List<FeedPostUI>>(emptyList())
    val posts: StateFlow<List<FeedPostUI>> = _posts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _myUserId = MutableStateFlow<Int?>(null)
    val myUserId: StateFlow<Int?> = _myUserId.asStateFlow()

    private var lastPostId: Int? = null

    private val _userLocation = MutableStateFlow<Location?>(null)

    init {
        viewModelScope.launch {
            _myUserId.value = requestManager.getMyUserId()
        }
        loadFeed()
    }

    fun updateUserLocation(location: Location) {
        _userLocation.value = location
    }

    fun toggleFollow(authorId: Int, currentFollowing: Boolean) {
        viewModelScope.launch {
            val success = if (currentFollowing) {
                requestManager.unfollowUserRequest(authorId)
            } else {
                requestManager.followUserRequest(authorId)
            }
            if(success) {
                _posts.value = _posts.value.map { feedPost ->
                    if(feedPost.post.authorId == authorId) {
                        feedPost.copy(isFollowingAuthor = !currentFollowing)
                    } else {
                        feedPost
                    }
                }
            }
        }
    }
    fun refreshFeed() {
        lastPostId = null
        postRepository.clearCache()
        _posts.value = emptyList()
        loadFeed(isRefresh = true)
    }

    fun updateFollowState(authorId: Int, isFollowing: Boolean) {
        _posts.value = _posts.value.map { feedPost ->
            if(feedPost.post.authorId == authorId) {
                feedPost.copy(isFollowingAuthor = isFollowing)
            } else {
                feedPost
            }
        }
    }
    fun loadFeed(isRefresh: Boolean = false) {

        if(_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true

            if(isRefresh) {
                lastPostId = null 
            }

            val postIds = requestManager.getFeedRequest(limit = 10, maxPostId = lastPostId)

            if(postIds != null) {
               val aggregatedPost = postIds.mapNotNull { id ->
                   val post = postRepository.getPost(id)
                   if(post != null) {
                       Log.d("FeedViewModel", "Ricevuti ${postIds.size} post dal server")
                       val authorDetails = requestManager.getUserDetailsRequest(post.authorId)
                        if(authorDetails != null) {
                            FeedPostUI(
                                post = post,
                                authorUsername = authorDetails.username,
                                authorProfilePicture = authorDetails.profilePicture,
                                isFollowingAuthor = authorDetails.isYourFollowing,
                                location = post.location
                            )
                        } else null
                   } else null
               }
                if(isRefresh) {
                    Log.d("FeedViewModel", "Refresh feed: sostituisco i post esistenti")
                    _posts.value = aggregatedPost
                } else {
                    Log.d("FeedViewModel", "Caricamento feed: aggiungo nuovi post a quelli esistenti")
                    _posts.value = _posts.value + aggregatedPost
                }
                lastPostId = postIds.last()

            } else {
                Log.e("FeedViewModel", "Caricamento feed preview fallito")
            }

            _isLoading.value = false
        }
    }

}