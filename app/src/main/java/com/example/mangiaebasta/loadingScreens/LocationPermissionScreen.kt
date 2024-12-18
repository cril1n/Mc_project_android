package com.example.mangiaebasta.loadingScreens

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mangiaebasta.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LocationPermissionScreen(
    model: MainViewModel,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    model.checkLocationPermission(permissionLauncher)
                }
            },
        ) { Text(text = "Allow location") }
    }
}