package com.example.mangiaebasta.viewmodel

import android.Manifest
import android.location.Location
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.example.mangiaebasta.AppDependencies
import com.example.mangiaebasta.datasource.CommunicationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val dataStoreManager = AppDependencies.dataStoreManager
    private val databaseManager = AppDependencies.databaseManager
    private val locationManager = AppDependencies.locationManager
    private var sid: String? = null


    // APP

    private val _initialized = MutableStateFlow(false)
    val initialized: StateFlow<Boolean> = _initialized

    private val _firstRun = MutableStateFlow(true)
    val firstRun: StateFlow<Boolean> = _firstRun

    private var _locationPermissionGranted = MutableStateFlow(false)
    val locationPermissionGranted: StateFlow<Boolean> = _locationPermissionGranted

    private var _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location


    suspend fun checkFirstRun(): Boolean {
        return try {
            Log.d("MainViewModel", "Checking first run")
            sid = dataStoreManager.getSidFromDataStore().toString()
            if (sid == null || sid == "null") {
                Log.d("MainViewModel", "First run")
                _firstRun.value = true
                _initialized.value = true
                true
            } else {
                Log.d("MainViewModel", "Not first run sid: $sid")
                _firstRun.value = false
                _initialized.value = true
                false
            }
        } catch (e: Exception) {
            Log.e("MainViewModel", "Error in checkFirstRun: $e")
            false
        }
    }

    suspend fun createNewUser() {
        Log.d("MainViewModel", "Creating new user")
        try {
            sid = CommunicationManager.createUser()
            dataStoreManager.setSidInDataStore(sid)
            Log.d("MainViewModel", "New user create, sid: $sid")
            _firstRun.value = false
        } catch (e: Exception) {
            Log.e("MainViewModel", "Error in createNewUser: $e")
        }

    }

    suspend fun checkLocationPermission(permissionLauncher: ManagedActivityResultLauncher<String, Boolean>) {
        _locationPermissionGranted.value = locationManager.hasLocationPermission()
        if (_locationPermissionGranted.value) {
            calculateCurrentLocation()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    suspend fun calculateCurrentLocation() {
        _locationPermissionGranted.value = locationManager.hasLocationPermission()
        if (_locationPermissionGranted.value) {
            _location.value = locationManager.getCurrentLocation()
        }
    }


    //HOME

    private val _selectedSection = MutableStateFlow(1)
    val selectedSection: StateFlow<Int> = _selectedSection

    fun switchScreen() {
        if (_selectedSection.value == 1) {
            _selectedSection.value = 2
        } else {
            _selectedSection.value = 1
        }
    }
}