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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

val Context.dataStore by preferencesDataStore(name = "settings")


class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

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
            MyApp(mainViewModel)
        }
    }
}


@Composable
fun MyApp(model: MainViewModel) {

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





