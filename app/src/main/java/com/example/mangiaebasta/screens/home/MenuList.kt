package com.example.mangiaebasta.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mangiaebasta.model.MenuWImage

@Composable
fun MenuList(menuList: List<MenuWImage>, navController: NavController) {

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        items(menuList) { menu ->
            MenuCard(menu, navController)
        }
    }


}

