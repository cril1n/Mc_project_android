package com.example.mangiaebasta.datasource

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await

class LocationManager(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)


    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        if (!hasLocationPermission()) {
            Log.d("LocationManager", "Location permission not granted")
        }

        Log.d("LocationManager", "Fetching location")
        return try {
            val cancellationTokenSource = CancellationTokenSource()
            val location: Location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).await()
            Log.d("LocationManager", "Location fetched: $location")
            location
        } catch (e: Exception) {
            Log.e("LocationManager", "Error fetching location: ${e.message}")
            null
        }
    }
}