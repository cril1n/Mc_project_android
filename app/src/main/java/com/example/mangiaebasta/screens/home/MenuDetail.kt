package com.example.mangiaebasta.screens.home

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.mangiaebasta.components.TopBarWithBackArrow

@Composable
fun MenuDetail(mid: Int, navController: NavController) {
    TopBarWithBackArrow("Menu Detail", navController)
    Text(text = mid.toString() ?: "Nessun menu selezionato")
}