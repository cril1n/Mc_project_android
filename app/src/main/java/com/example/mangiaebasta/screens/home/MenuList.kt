package com.example.mangiaebasta.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mangiaebasta.model.MenuWImage
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun MenuList(menuList: List<MenuWImage>, navController: NavController, model: MainViewModel) {

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFf5f5f5)),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {

        items(menuList) { menu ->
            MenuCard(menu, navController, model)
        }
    }

}

