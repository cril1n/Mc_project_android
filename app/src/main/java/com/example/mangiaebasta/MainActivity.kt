package com.example.mangiaebasta

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mangiaebasta.datasource.DatabaseManager
import com.example.mangiaebasta.datasource.DatastoreManager
import com.example.mangiaebasta.datasource.LocationManager
import com.example.mangiaebasta.loadingScreens.LocationPermissionDeniedScreen
import com.example.mangiaebasta.loadingScreens.LocationPermissionScreen
import com.example.mangiaebasta.loadingScreens.SplashScreen
import com.example.mangiaebasta.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.key


val Context.dataStore by preferencesDataStore(name = "settings")


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val databaseManager = DatabaseManager(this)
        val dataStoreManager = DatastoreManager(dataStore)
        val locationManager = LocationManager(this)
        val geocoder = Geocoder(this)
        val factory = viewModelFactory {
            initializer {
                MainViewModel(databaseManager, dataStoreManager, locationManager, geocoder)
            }
        }
        val mainViewModel: MainViewModel by viewModels() { factory }
        setContent {
            val resetKey by mainViewModel.reset.collectAsState()
            // Usa il valore di resetKey per forzare una nuova composizione
            CompositionLocalProvider {
                MyApp(mainViewModel, resetKey)
            }
        }
    }
}


@Composable
fun MyApp(model: MainViewModel, key: Boolean) {
    // Cambia il valore della chiave per invalidare la composizione
    key(key) {
        val initialized = model.initialized.collectAsState()
        val firstRun = model.firstRun.collectAsState()
        val locationPermissionGranted = model.locationPermissionGranted.collectAsState()
        var locationPermissionDenied by remember { mutableStateOf(false) }

        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Log.d("MainActivity", "Permission granted")
                CoroutineScope(Dispatchers.Main).launch {
                    model.calculateCurrentLocation()
                }
            } else {
                Log.d("MainActivity", "Permission denied")
                locationPermissionDenied = true
            }
        }

        LaunchedEffect(Unit) {
            val fRun = model.checkFirstRun()
            if (!fRun) {
                model.checkLocationPermission(permissionLauncher)
            }
        }

        if (locationPermissionDenied) {
            LocationPermissionDeniedScreen()
        } else {
            if (initialized.value) {
                if (firstRun.value) {
                    SplashScreen(model)
                } else {
                    if (locationPermissionGranted.value) {
                        Root(model)
                    } else {
                        LocationPermissionScreen(model, permissionLauncher)
                    }
                }
            } else {
                LoadingScreen("Initializing...")
            }
        }
    }
}






