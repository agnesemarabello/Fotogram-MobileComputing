package com.example.fotogram

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import java.lang.Exception

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

class RequestManager {
    suspend fun registrationRequest(): UserRegistrationResponse? {
        val REGISTRATION_ENDPOINT = BASE_URL + "user"
        Log.i("RequestManager", "Invio richiesta di registrazione...")
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
}