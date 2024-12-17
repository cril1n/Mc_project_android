package com.example.mangiaebasta.screens.home

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.mangiaebasta.model.Menu

@Composable
fun MenuDetail(menu: Menu?, navController: NavController) {
    Text(text = menu?.name ?: "Nessun menu selezionato")
}