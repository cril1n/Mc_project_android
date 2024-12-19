package com.example.mangiaebasta.screens.profile

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mangiaebasta.R
import com.example.mangiaebasta.model.User
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun Profile(model : MainViewModel, user: User) {
    // Creiamo un NavController che sarà responsabile della navigazione di Profile
    val navController = rememberNavController()
    // Definiamo il NavHost, che contiene tutte le destinazioni
    NavHost(navController = navController, startDestination = "profile") {
        composable("profile") { ProfileScreen(user, navController) }
        composable("profileEdit") { EditProfile(model, user, navController) }
        composable("billingEdit") { EditBilling(model, user, navController) }
        composable("lastOrder") { LastOrderScreen(user, navController) }
    }
}

@Composable
fun ProfileScreen( user: User, navController: NavController) {
    ProfileHeader()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 100.dp, horizontal = 40.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        ImageWithText(user)
        Column(
            Modifier
                .fillMaxSize()
                .padding(vertical = 40.dp, horizontal = 40.dp)
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .background(Color.LightGray),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileInfo(user, navController)
            ProfileBillingInfo(user, navController)
            LastOrderInfo(user, navController)
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
                    color = Color.White                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF390073) // Colore di sfondo
        )
    )
}

@Composable
fun ProfileInfo( user: User, navController: NavController) {
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
fun ProfileBillingInfo(user: User, navController: NavController) {
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
fun LastOrderInfo(user: User, navController: NavController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.navigate("lastOrder") }, Modifier.size(200.dp, 50.dp)) {
            Text(text = "See last order")
        }
    }
}

@Composable
fun ImageWithText(user: User) {
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