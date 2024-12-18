package com.example.mangiaebasta.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun HomeScreen(model: MainViewModel) {

    HomeScreenHeader(model)
    HomeScreenBody(model)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenHeader(model: MainViewModel) {
    val selectedSection = model.selectedSection.collectAsState()

    TopAppBar(
        title = { },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        ),
        actions = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Sezione 1
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .background(
                            color = if (selectedSection.value == 1) Color.Gray else Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { model.switchScreen() }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Menu List",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Sezione 2
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .background(
                            color = if (selectedSection.value == 2) Color.Gray else Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { model.switchScreen() }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Menu map",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    )
}

@Composable
fun HomeScreenBody(model: MainViewModel) {
    val selectedSection = model.selectedSection.collectAsState()

    when (selectedSection.value) {
        1 -> MenuList()
        2 -> MenuMap()
    }
}

@Composable
fun MenuList() {
    Text(text = "Menu List")
}

@Composable
fun MenuMap() {
    Text(text = "Menu Map")
}


