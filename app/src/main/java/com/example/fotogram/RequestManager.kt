package com.example.fotogram

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import java.lang.Exception
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess
import kotlinx.serialization.SerialName

const val BASE_URL = "https://develop.ewlab.di.unimi.it/mc/2526/"

val httpClient: HttpClient by lazy {
    HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }
}

@Serializable
data class UserRegistrationResponse(
     val sessionId: String,
     val userId: Int
)

@Serializable
data class UpdateProfileRequest(
    val username: String,
    val bio: String,
    val dateOfBirth: String
)

@Serializable
data class ProfileDetailsResponse(
    val id: Int,
    val username: String,
    val bio: String,
    val dateOfBirth: String? = null,
    val profilePicture: String? = null, //da modificare più avanti perchè l'immagine è obligatoria
    val isYourFollower: Boolean,
    val isYourFollowing: Boolean,
    val followersCount: Int,
    val followingCount: Int,
    val postsCount: Int

)

@Serializable
data class PostLocation(
    val latitude: Double?,
    val longitude: Double?
)

@Serializable
data class FeedPreview(
    val id: Int,
    val areYouFollowingAuthor: Boolean
)
@Serializable
data class Post(
    val id: Int,

    val authorId: Int,
    val createdAt: String,
    val contentPicture: String,

    val contentText: String? = null, //assumo per ora che il testo sia opzionale
    val location: PostLocation? = null, //idem

)

@Serializable
data class FeedPostUI(
    val post: Post,
    val authorUsername: String,
    val authorProfilePicture: String? = null,
)

class RequestManager(private val dataStoreManager: DataStoreManager) {
    suspend fun registrationRequest(): UserRegistrationResponse? {
        val REGISTRATION_ENDPOINT = BASE_URL + "user"

        Log.i("RequestManager", "Registrazione in corso...")
        return try {
            val response = httpClient.post(REGISTRATION_ENDPOINT) {
                accept(ContentType.Application.Json)
            }

            if(response.status.value == 200) {
                val userResponse = response.body<UserRegistrationResponse>()
                Log.i("RequestManager", "sessionId: ${userResponse.sessionId}, userId: ${userResponse.userId}")
                return userResponse
            } else {
                Log.d("RequestManager", "Registrazione fallita. Status code: ${response.status.value}")
                return null
            }
        } catch (e: Exception) {
            Log.d("RequestManager", "Errore durante la registrazione -> ${e.message}")
            null
        }
    }

    suspend fun updateProfileRequest(
        newUsername: String,
        newBio: String,
        newDateOfBirth: String
    ): ProfileDetailsResponse? {

        val UPDATE_ENDPOINT = BASE_URL + "user"
        val SID = dataStoreManager.getSID()
        val UID = dataStoreManager.getUID()

        if(SID.isNullOrEmpty() || UID == null) {
            Log.d("RequestManager", "Impossibile aggiornare il profilo -> SID o UID mancante.")
            return null
        }

        val requestBody = UpdateProfileRequest(
            username = newUsername,
            bio = newBio,
            dateOfBirth = newDateOfBirth
        )

        Log.i("RequestManager", "Aggiornamento profilo in corso...")

        try{
            val response = httpClient.put(UPDATE_ENDPOINT) {
                header("x-session-id", SID)
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            if(response.status.value == 200) {
                Log.i("RequestManager", "Profilo aggiornato con successo.")
                Log.i("RequestManager", "Response: ${response.body<String>()}")
                return response.body<ProfileDetailsResponse>()
            } else {
                Log.d("RequestManager", "Aggiornamento profilo fallito. Status code: ${response.status.value}")
                return null
            }

        } catch (e: Exception) {
            Log.d("RequestManager", "Errore durante l'aggiornamento del profilo -> ${e.message}")
            return null
        }
    }

    suspend fun getUserDetailsRequest(userId: Int): ProfileDetailsResponse? {
        val USER_BY_ID_ENDPOINT = BASE_URL + "user/$userId"
        val SID = dataStoreManager.getSID()

        if(SID.isNullOrEmpty()) {
            Log.d("RequestManager", "Impossibile ottenere i dettagli utente -> SID mancante.")
            return null
        }

        Log.i("RequestManager", "Caricamento dettagli utente ID: $userId in corso...")
        try {
            val response = httpClient.get(USER_BY_ID_ENDPOINT) {
                header("x-session-id", SID)
                accept(ContentType.Application.Json)
            }
            if(response.status.isSuccess()) {
                Log.i("RequestManager", "Dettagli utente ID: $userId caricati con successo.")
                return response.body<ProfileDetailsResponse>()
            } else {
                Log.d("RequestManager", "Caricamento dettagli utente ID: $userId fallito. Status code: ${response.status.value}")
                return null
            }
        } catch (e: Exception) {
            Log.d("RequestManager", "Errore durante il caricamento dei dettagli utente ID: $userId -> ${e.message}")
            return null
        }
    }

    suspend fun getFeedRequest(
        maxPostId: Int? = null,
        limit: Int? = 10,
        seed: Int? = null
    ): List<Int>? {
        val FEED_ENDPOINT = BASE_URL + "feed"
        val SID = dataStoreManager.getSID()
        if(SID.isNullOrEmpty()) {
            Log.d("RequestManager", "ERRORE -> SID mancante.")
            return null
        }

        Log.i("RequestManager", "Caricamento Feed in corso...")
        try {
            val response = httpClient.get(FEED_ENDPOINT) {
                header("x-session-id", SID)

                maxPostId?.let {
                    parameter("maxPostId", it)
                }

                limit?.let {
                    parameter("limit", it)
                }

                seed?.let {
                    parameter("limit", it)
                }
                accept(ContentType.Application.Json)
            }

            if(response.status.isSuccess()) {
                Log.i("RequestManager", "Feed caricato con successo.")
                val ids = response.body<List<Int>>()
                Log.i("RequestManager", "Feed Post IDs: $ids")
                return ids
            } else {
                Log.d("RequestManager", "Caricamento Feed fallito. Status code: ${response.status.value}")
                return null
            }
        } catch (e: Exception) {
            Log.d("RequestManager", "Errore durante il caricamento del Feed -> ${e.message}")
            return null
        }
    }

    suspend fun getFeedPostIds(
        maxPostId: Int? = null,
        limit: Int? = null
    ): List<Int>? {
        val FEED_ENDPOINT = BASE_URL + "feed"
        val SID = dataStoreManager.getSID()
        if(SID.isNullOrEmpty()) return null

        try {
            val response = httpClient.get(FEED_ENDPOINT) {
                header("x-session-id", SID)

                maxPostId?.let {
                    parameter("maxPostId", it)
                }

                limit?.let {
                    parameter("limit", it)
                }
            }

            if(response.status.isSuccess()) {
                Log.i("RequestManager", "Post IDs caricati con successo.")
                return response.body<List<Int>>()
            } else {
                Log.d("RequestManager", "Caricamento Post IDs fallito. Status code: ${response.status.value}")
                return null
            }
        } catch (e: Exception) {
            Log.d("RequestManager", "Errore durante il caricamento dei Post IDs -> ${e.message}")
            return null
        }
    }

    suspend fun getPostByIdRequest(postId: Int): Post? {
        val POST_BY_ID_ENDPOINT = BASE_URL + "post/$postId"
        val SID = dataStoreManager.getSID()
        if(SID.isNullOrEmpty()) return null

        Log.i("RequestManager", "Caricamento Post ID: $postId in corso...")
        try {
            val response = httpClient.get(POST_BY_ID_ENDPOINT) {
                header("x-session-id", SID)
                accept(ContentType.Application.Json)
            }

            if(response.status.isSuccess()) {
                Log.i("RequestManager", "Post ID: $postId caricato con successo.")
                return response.body<Post>()
            } else {
                Log.d("RequestManager", "Caricamento Post ID: $postId fallito. Status code: ${response.status.value}")
                return null
            }
        } catch (e: Exception) {
            Log.d("RequestManager", "Errore durante il caricamento del Post ID: $postId -> ${e.message}")
            return null
        }
    }

    suspend fun getFeedPreviews(maxPostId: Int? = null): List<FeedPreview>? {
        val FEED_ENDPOINT = BASE_URL + "feed"
        val SID = dataStoreManager.getSID()
        if(SID.isNullOrEmpty()) return null

        try {
            val response = httpClient.get(FEED_ENDPOINT) {
                header("x-session-id", SID)

                maxPostId?.let {
                    parameter("maxPostId", it)
                }
                accept(ContentType.Application.Json)
            }

            if(response.status.isSuccess()) {
                Log.i("RequestManager", "Feed Previews caricati con successo.")
                return response.body<List<FeedPreview>>()
            } else {
                Log.d("RequestManager", "Caricamento Feed Previews fallito. Status code: ${response.status.value}")
                return null
            }
        } catch (e: Exception) {
            Log.d("RequestManager", "Errore durante il caricamento dei Feed Previews -> ${e.message}")
            return null
        }
    }

}