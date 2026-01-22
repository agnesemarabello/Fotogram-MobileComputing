package com.example.fotogram

import android.location.Location
import kotlinx.coroutines.tasks.await
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

object LocationHelper {
    suspend fun getCurrentLocation(fusedLocationClient: FusedLocationProviderClient): Location? {
        return try {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).await()
        } catch (e: Exception) {
            null
        }
    }
    fun isNearby(
        userLoc: Location,
        postLoc: Location,
        thresholdInMeters: Float = 10000f
    ): Boolean {
        return userLoc.distanceTo(postLoc) <= thresholdInMeters
    }
}

