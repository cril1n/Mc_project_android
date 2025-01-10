package com.example.mangiaebasta.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mangiaebasta.R
import com.example.mangiaebasta.components.CustomAlertDialog
import com.example.mangiaebasta.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
        if (!model.isUserRegistered()) {
            navController.navigate("firstRegistration")
        }
    }

    DeleteAccountDialog(model)

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Profile", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black
                    ),
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 2.dp,
                    color = Color(0xFFF99501),

                    )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFf5f5f5))
                .padding(start = 16.dp, end = 16.dp, top = 90.dp, bottom = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Logo and Name
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Menu Items Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFF99501), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MenuButton(
                        icon = R.drawable.person,
                        text = "Personal Info",
                        onClick = { navController.navigate("profileEdit") }
                    )
                    MenuButton(
                        icon = R.drawable.card,
                        text = "Payment Info",
                        onClick = { navController.navigate("billingEdit") }
                    )
                    MenuButton(
                        icon = R.drawable.order,
                        text = "Last Order",
                        onClick = { navController.navigate("lastOrder") }
                    )

                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Delete Account Button
            Button(
                onClick = {
                    model.setDeleteAccountDialog(true)
                },
                modifier = Modifier
                    .width(200.dp)
                    .padding(8.dp)
                    .height(50.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color(0xFFB14D4D),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.LightGray),
            ) {
                Text(
                    "DELETE ACCOUNT",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp // Spaziatura tra le lettere
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MenuButton(
    icon: Int,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = text,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.chevron),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun DeleteAccountDialog(model: MainViewModel, ) {
    val showDialog by model.deleteAccountDialog.collectAsState()

    if (showDialog) {
        CustomAlertDialog(
            model::setDeleteAccountDialog,
            model::resetApp,
            null,
            null,
            "Account delete",
            "Are you sure you want to delete your account?",
            "Yes, I want",
            "No, I don't"
        )
    }
}