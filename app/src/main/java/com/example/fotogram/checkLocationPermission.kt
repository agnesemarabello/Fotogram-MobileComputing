package com.example.fotogram

import android.content.Context
import androidx.core.content.ContextCompat

fun checkLocationPermission(context: Context) : Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
}