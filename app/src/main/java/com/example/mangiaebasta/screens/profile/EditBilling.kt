package com.example.mangiaebasta.screens.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.mangiaebasta.model.User
import com.example.mangiaebasta.components.TopBarWithBackArrow

@Composable
fun EditBilling(user: User, navController: NavController) {
    TopBarWithBackArrow("Billing edit", navController)
    Text(text = "Name: ${user.cardNumber} ${user.cardCVV}")
}