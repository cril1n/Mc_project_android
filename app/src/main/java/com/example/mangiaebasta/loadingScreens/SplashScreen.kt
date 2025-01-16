package com.example.mangiaebasta.loadingScreens

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mangiaebasta.R
import com.example.mangiaebasta.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(model: MainViewModel) {
    Column(
        Modifier.fillMaxSize().padding(50.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(

        ){
            Text(text = "Mangia \nEbbasta",
                color = Color(0xFFF99501),
                fontSize = 50.sp,
                fontWeight = FontWeight.W900,
                fontStyle = FontStyle.Italic,
            )
        }
        Column(
        ){
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Descrizione immagine",
                modifier = Modifier.size(500.dp).padding(bottom = 16.dp)
            )
        }
        Column(){
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        model.createNewUser()
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA500)),
                elevation = ButtonDefaults.elevation(4.dp)

            ) { Text(text = "Start", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold )}
        }

    }
}