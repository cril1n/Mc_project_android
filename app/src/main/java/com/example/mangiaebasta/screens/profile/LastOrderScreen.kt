package com.example.mangiaebasta.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.example.mangiaebasta.components.TopBarWithBackArrow
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun LastOrderScreen(model: MainViewModel, navController: NavController) {
    val user = model.user.collectAsState().value
    TopBarWithBackArrow("Last order info", navController)
}