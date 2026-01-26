package com.example.fotogram

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun checkLocationPermission(context: Context) : Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}