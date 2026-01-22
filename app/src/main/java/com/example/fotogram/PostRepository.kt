package com.example.fotogram

import android.util.Log

class PostRepository(private val requestManager: RequestManager) {
    private val postCache = mutableMapOf<Int, Post>()

    suspend fun getPost(postId: Int): Post? {
        if(postCache.containsKey(postId)) {
            Log.i("Cache", "Post $postId PRESENTE in locale")
            return postCache[postId]
        }
        val postServer = requestManager.getPostByIdRequest(postId)

        if(postServer != null) {
            postCache[postId] = postServer
            Log.i("Cache", "Post $postId SCARICATO e SALVATO in locale")
        }

        return postServer
    }
}