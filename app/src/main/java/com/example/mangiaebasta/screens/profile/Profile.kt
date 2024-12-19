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
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
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
    var startDestination = model.startDestination.collectAsState()
    val user = model.user.collectAsState().value

    Log.d("Profile", "User: $user")
    if(!user.firstName.isEmpty() || !user.lastName.isEmpty()){
        Log.d("Profile", "User name is not empty")
        model.setStartDestination("profile")
    }else{
        Log.d("Profile", "User name is empty")
        model.setStartDestination("firstRegistration")
    }
    // Mostra "PrimaRegistrazione" se il nome o il cognome è vuoto
    NavHost(navController = navController, startDestination = startDestination.value) {
        // Definiamo il NavHost, che contiene tutte le destinazioni
        composable("firstRegistration") { PrimaRegistrazione(model, navController)}
        composable("profile") { ProfileScreen(user, navController) }
        composable("profileEdit") { EditProfile(model, user, navController) }
        composable("billingEdit") { EditBilling(model, user, navController) }
        composable("lastOrder") { LastOrderScreen(user, navController) }
    }
}

@Composable
fun ProfileScreen(user: User, navController: NavController) {
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
                ProfileInfo(user, navController)
                ProfileBillingInfo(user, navController)
                LastOrderInfo(user, navController)
            }
        }

        // LOGOUT BUTTON
        Button(
            onClick = {
                //TODO
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
@Composable
fun PrimaRegistrazione(model: MainViewModel, navController: NavHostController) {
    var firstNameForm by remember { mutableStateOf("") }
    var lastNameForm by remember { mutableStateOf("") }
    val isSubmitEnabled = firstNameForm.isNotBlank() && lastNameForm.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            androidx.compose.material.Text(
                text = "First Registration",
                textAlign = TextAlign.Center
            )
            androidx.compose.material.Text(
                text = "Fill in the following fields to get started",
                textAlign = TextAlign.Center
            )

            TextField(
                value = firstNameForm,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^[a-zA-Z]*$"))) {
                        firstNameForm = newValue
                    }
                },
                label = { androidx.compose.material.Text("First name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = lastNameForm,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^[a-zA-Z]*$"))) {
                        lastNameForm = newValue
                    }
                },
                label = { androidx.compose.material.Text("Last name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            androidx.compose.material.Button(
                onClick = {
                    model.setFirstNameForm(firstNameForm)
                    model.setLastNameForm(lastNameForm)
                    model.updateUserNameData()
                    navController.navigate("profile")
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isSubmitEnabled, // Abilita solo se entrambi i campi sono pieni
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA500))
            ) {
                androidx.compose.material.Text(text = "Submit", color = Color.White)
            }
        }
    }

}