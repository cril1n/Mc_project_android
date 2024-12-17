package com.example.mangiaebasta.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.mangiaebasta.model.User
import com.example.mangiaebasta.components.TopBarWithBackArrow


@Composable
fun EditProfile(user: User, navController: NavController) {
    Column {
        TopBarWithBackArrow("Profile edit", navController)
        Text(text = "Name: ${user.firstName} ${user.lastName}")
    }
}
