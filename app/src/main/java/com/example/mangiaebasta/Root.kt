package com.example.mangiaebasta

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mangiaebasta.screens.home.Home
import com.example.mangiaebasta.screens.order.OrderTrack
import com.example.mangiaebasta.screens.profile.Profile
import com.example.mangiaebasta.viewmodel.MainViewModel

data class TopLevelRoute(val name: String, val route: String, val icon: ImageVector)

val topLevelRoutes = listOf(
    TopLevelRoute("Home", "home", Icons.Default.Home),
    TopLevelRoute("OrderTrack", "order track", Icons.Default.ShoppingCart),
    TopLevelRoute("Profile", "profile", Icons.Default.Person)
)
@SuppressLint("RestrictedApi")
@Composable
fun Root(model: MainViewModel) {
    val user = model.user.collectAsState()
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                topLevelRoutes.forEach { topLevelRoute ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                topLevelRoute.icon,
                                contentDescription = topLevelRoute.name
                            )
                        },
                        label = { Text(topLevelRoute.name) },
                        selected = currentDestination?.route == topLevelRoute.route,
                        onClick = {
                            navController.navigate(topLevelRoute.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = "home", Modifier.padding(innerPadding)) {
            composable("home") { Home(model, user.value, navController) }
            composable("order track") { OrderTrack(user.value) }
            composable("profile") { Profile(model, user.value) }
        }
    }

}


