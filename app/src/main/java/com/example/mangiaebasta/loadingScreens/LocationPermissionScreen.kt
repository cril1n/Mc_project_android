package com.example.mangiaebasta.loadingScreens

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
            modifier = Modifier
                .padding(top = 100.dp),
        ){
            Image(
                painter = painterResource(id = R.drawable.location),
                contentDescription = "Descrizione immagine",
                modifier = Modifier.size(300.dp)
            )
        }
        Column(verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "To proced, location permissions are required. Please grant it by tapping the button below",
                modifier = Modifier.padding(top = 110.dp, bottom = 40.dp, start = 12.dp, end = 12.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold)
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA500)),
                elevation = ButtonDefaults.elevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        model.checkLocationPermission(permissionLauncher)
                    }
                },

                ) { Text(text = "Allow location", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp ) }
        }


    }
}
