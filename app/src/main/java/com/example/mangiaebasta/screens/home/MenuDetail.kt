package com.example.mangiaebasta.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.mangiaebasta.components.TopBarWithBackArrow
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun MenuDetail(mid: Int, image: String, navController: NavController, model: MainViewModel) {

    val menuDetailed by model.menuDetailed.collectAsState()

    LaunchedEffect(menuDetailed) {
        model.loadMenuDetailed(mid)
    }



    Column {
        TopBarWithBackArrow("Menu Detail", navController)
        if (menuDetailed.name != null) {

            Text(text = menuDetailed.name!!)

        } else {
            Text(text = "Loading...")
        }
    }
}