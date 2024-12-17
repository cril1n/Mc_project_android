package com.example.mangiaebasta.viewmodel

import android.util.Log
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
    private var sid: String? = null


    // APP

    private val _initialized = MutableStateFlow(false)
    val initialized: StateFlow<Boolean> = _initialized

    private val _firstRun = MutableStateFlow(true)
    val firstRun: StateFlow<Boolean> = _firstRun

    private val _locationPermissionGranted = MutableStateFlow(false)
    val locationPermissionGranted: StateFlow<Boolean> = _locationPermissionGranted


    fun checkFirstRun() {
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("MainViewModel", "Checking first run")
            sid = dataStoreManager.getSidFromDataStore().toString()
            if (sid == null || sid == "null") {
                try {
                    Log.d("MainViewModel", "First run")
                    sid = CommunicationManager.createUser()
                    dataStoreManager.setSidInDataStore(sid)
                    _firstRun.value = true
                    _initialized.value = true
                } catch (e: Exception) {
                    Log.e("MainViewModel", "Error in checkFirstRun: $e")
                }
            } else {
                Log.d("MainViewModel", "Not first run sid: $sid")
                _firstRun.value = false
                _initialized.value = true
            }
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