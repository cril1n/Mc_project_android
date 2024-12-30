package com.example.mangiaebasta.screens.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mangiaebasta.R
import com.example.mangiaebasta.model.GetUserResponse
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun ProfileScreen(model: MainViewModel, navController: NavController) {

    val user = model.user.collectAsState().value

    // Monitoriamo i cambiamenti di route
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    // Aggiorniamo lo stato quando la route cambia
    LaunchedEffect(currentRoute) {
        if (currentRoute != null) {
            model.setLastScreen(currentRoute)
        }
        val flag = model.isUserRegistered()
        if (!flag) {
            navController.navigate("firstRegistration")
        }
    }



    ProfileHeader()

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween, // Distribuisce meglio gli elementi
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageWithText(user)
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
                    .padding(16.dp), // Interno per migliorare lo spacing
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileInfo(navController)
                ProfileBillingInfo(navController)
                LastOrderInfo(navController)
            }
        }

        // LOGOUT BUTTON
        Button(
            onClick = {
                model.resetApp()
            },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE), // Colore primario in linea con il tema
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(50), // Forma rotonda
            modifier = Modifier
                .padding(vertical = 20.dp)
                .size(width = 200.dp, height = 50.dp) // Dimensioni del pulsante
        ) {
            Text(text = "Logout", fontSize = 16.sp)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHeader() {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Profile",
                    color = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF390073) // Colore di sfondo
        )
    )
}

@Composable
fun ImageWithText(user: GetUserResponse) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Descrizione immagine",
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = "${user.firstName} ${user.lastName}",
            fontSize = 20.sp
        )
    }
}

@Composable
fun ProfileInfo(navController: NavController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.navigate("profileEdit") }, Modifier.size(200.dp, 50.dp)) {
            Text(text = "Edit Profile")
        }

    }

}

@Composable
fun ProfileBillingInfo(navController: NavController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.navigate("billingEdit") }, Modifier.size(200.dp, 50.dp)) {
            Text(text = "Edit Billing Info")
        }
    }
}

@Composable
fun LastOrderInfo(navController: NavController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.navigate("lastOrder") }, Modifier.size(200.dp, 50.dp)) {
            Text(text = "See last order")
        }
    }
}