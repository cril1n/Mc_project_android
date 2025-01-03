package com.example.mangiaebasta.screens.home

import android.location.Location
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mangiaebasta.LoadingScreen
import com.example.mangiaebasta.model.MenuWImage
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    model: MainViewModel,
    navController: NavHostController,
) {

    // Monitoriamo i cambiamenti di route
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    // Aggiorniamo lo stato quando la route cambia
    LaunchedEffect(currentRoute) {
        if (currentRoute != null) {
            model.setLastScreen(currentRoute)
        }
    }

    val menuList = model.menuList.collectAsState()
    val location = model.location.collectAsState()


    LaunchedEffect(location.value) {
        Log.d("HomeScreen", "Location loaded: ${location.value}")
        if (location.value != null) {
            model.loadMenuList()
        }
    }


    if (menuList.value.isEmpty()) {
        LoadingScreen("Loading menus...")
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f)
                ) {
                    HomeScreenHeader(model)
                }
                HomeScreenBody(model, menuList.value, location.value, navController)
            }
        }
    }
}

fun Modifier.bottomBorder(color: Color, width: Dp) = this.drawBehind {
    drawLine(
        color = color,
        start = Offset(0f, size.height),  // Inizia dalla parte inferiore della TopAppBar
        end = Offset(size.width, size.height),  // Finisce su tutta la larghezza
        strokeWidth = width.toPx()  // Imposta lo spessore del bordo
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenHeader(model: MainViewModel) {
    val selectedSection = model.selectedSection.collectAsState()

    TopAppBar(
        title = { },
        modifier = Modifier
            .fillMaxWidth()
            .bottomBorder(Color(0xFFF99501), 4.dp),
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
                            color = if (selectedSection.value == 1) Color(0xFFf0f0f0) else Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { model.switchScreen() }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Menu List",
                        fontSize = 16.sp,
                        fontWeight = if (selectedSection.value == 1) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Sezione 2
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .background(
                            color = if (selectedSection.value == 2) Color(0xFFf0f0f0) else Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { model.switchScreen() }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Menu map",
                        fontSize = 16.sp,
                        fontWeight = if (selectedSection.value == 2) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
                        color = Color.Black
                    )
                }
            }
        }
    )
}

@Composable
fun HomeScreenBody(
    model: MainViewModel,
    menuList: List<MenuWImage>,
    location: Location?,
    navController: NavController,
) {
    val selectedSection = model.selectedSection.collectAsState()

    when (selectedSection.value) {
        1 -> MenuList(menuList, navController, model)
        2 -> MenuMap(menuList, location, navController, model)
    }
}






