package com.example.fotogram

import android.util.Log
/**
    *** PostRepository ***:
    Implementa la logica di chaching per il recupero dei post, in modo da ottimizzare le prestazioni.
    Salva un post in locale in modo da evitare si richiede lo stesso post più volte al server.
**/
class PostRepository(private val requestManager: RequestManager) {
    private val postCache = mutableMapOf<Int, Post>()

    // Svuota la cache dei post (solo se avviene il refresh del feed)
    fun clearCache() {
        postCache.clear()
    }

    // Recupera un post tramite il suo ID solo se non è presente in locale
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