package com.example.mangiaebasta

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.datastore.preferences.preferencesDataStore
import com.example.mangiaebasta.viewmodel.MainViewModel

val Context.dataStore by preferencesDataStore(name = "settings")


class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppDependencies.init(this, dataStore)
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

    LaunchedEffect(Unit) {
        model.checkFirstRun()
    }

    if (initialized.value) {
        if (firstRun.value) {
            SplashScreen()
        } else {
            if (locationPermissionGranted.value) {
                Root(model)
            } else {
                LocationPermissionScreen()
            }
        }
    } else {
        CircularProgressIndicator()
    }

}





