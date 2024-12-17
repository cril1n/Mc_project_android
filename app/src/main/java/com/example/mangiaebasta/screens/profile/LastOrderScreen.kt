package com.example.mangiaebasta.screens.profile

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.mangiaebasta.model.User
import com.example.mangiaebasta.components.TopBarWithBackArrow

@Composable
fun LastOrderScreen(user: User, navController: NavController) {
    TopBarWithBackArrow("Last order info", navController)
}