package com.example.mangiaebasta.loadingScreens

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mangiaebasta.R
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
        Modifier.fillMaxSize()
            .statusBarsPadding()
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.padding(top = 30.dp),
        ){
            Image(
                painter = painterResource(id = R.drawable.location),
                contentDescription = "Descrizione immagine",
                modifier = Modifier.size(275.dp).padding(bottom = 16.dp)
            )
        }
        Column(verticalArrangement = Arrangement.SpaceBetween){
            Text(text = "To proced, location permission is required. Please grant it by tapping the button below", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Button(
                modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp, top= 40.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA500)),
                elevation = ButtonDefaults.elevation(4.dp),
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        model.checkLocationPermission(permissionLauncher)
                    }
                },

                ) { Text(text = "Allow location", color = Color.White, fontWeight = FontWeight.Bold ) }
        }


    }
}
