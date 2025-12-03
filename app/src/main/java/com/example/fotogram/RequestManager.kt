package com.example.fotogram

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import java.lang.Exception
import io.ktor.client.request.put
import io.ktor.client.request.setBody

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
    val dateOfBirth: String,
    val profilePicture: String? = null, //da modificare più avanti perchè l'immagine è obligatoria
    val isYourFollower: Boolean,
    val isYourFollowing: Boolean,
    val followersCount: Int,
    val followingCount: Int,
    val postsCount: Int

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
}