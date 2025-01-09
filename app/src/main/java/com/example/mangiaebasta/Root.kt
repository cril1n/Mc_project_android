package com.example.mangiaebasta

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mangiaebasta.screens.home.HomeScreen
import com.example.mangiaebasta.screens.home.MenuDetail
import com.example.mangiaebasta.screens.home.OrderCheckOut
import com.example.mangiaebasta.screens.order.OrderTrack
import com.example.mangiaebasta.screens.profile.EditBilling
import com.example.mangiaebasta.screens.profile.EditProfile
import com.example.mangiaebasta.screens.profile.LastOrderScreen
import com.example.mangiaebasta.screens.profile.PrimaRegistrazione
import com.example.mangiaebasta.screens.profile.ProfileScreen
import com.example.mangiaebasta.viewmodel.MainViewModel
import androidx.compose.foundation.layout.*


@SuppressLint("RestrictedApi", "StateFlowValueCalledInComposition")
@Composable
fun Root(model: MainViewModel) {
    Log.d("Root", "Root called")
    val lastScreen = model.lastScreen.value
    var tabScreen by remember { mutableStateOf("") }
    val navController = rememberNavController()


    LaunchedEffect(Unit) {
        tabScreen = getTabScreenFromLastScreen(lastScreen)
    }

    fun Modifier.topBorder(color: Color, width: Dp) = this.drawBehind {
        drawLine(
            color = color,
            start = androidx.compose.ui.geometry.Offset(0f, 0f),
            end = androidx.compose.ui.geometry.Offset(size.width, 0f),
            strokeWidth = width.toPx()
        )
    }

    if (lastScreen != "" && tabScreen != "") {
        Scaffold(
            bottomBar = {
                BottomNavigation(
                    modifier = Modifier
                        .height(70.dp)
                        .topBorder(Color(0xFFF99501), 4.dp),
                    backgroundColor = Color.White,
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    BottomNavigationItem(
                        modifier = Modifier.padding(top = 5.dp),
                        icon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = { Text("Home") },
                        selected = currentDestination?.route == "homeScreen" || currentDestination?.route == "menuDetail" || currentDestination?.route == "orderCheckOut/{menuString}",
                        onClick = {
                            navController.navigate("homeScreen") {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )

                    BottomNavigationItem(
                        modifier = Modifier.padding(top = 5.dp),
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.drone2),
                                modifier = Modifier.size(21.dp),
                                contentDescription = "Order Track"
                            )
                        },
                        label = { Text("Order Track") },
                        selected = currentDestination?.route == "orderTrack",
                        onClick = {
                            navController.navigate("orderTrack") {
                                popUpTo("orderTrack") {
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )

                    BottomNavigationItem(
                        modifier = Modifier.padding(top = 5.dp),
                        icon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile"
                            )
                        },
                        label = { Text("Profile") },
                        selected = currentDestination?.route == "firstRegistration" || currentDestination?.route == "profileScreen" || currentDestination?.route == "profileEdit" || currentDestination?.route == "billingEdit" || currentDestination?.route == "lastOrder",
                        onClick = {
                            navController.navigate("profileScreen") {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(navController, startDestination = tabScreen, Modifier.padding(innerPadding)) {

                navigation(
                    startDestination = if (tabScreen == "home_stack") lastScreen else "homeScreen",
                    route = "home_stack"
                ) {
                    composable("homeScreen") { HomeScreen(model, navController) }
                    composable("menuDetail") { MenuDetail(navController, model) }
                    composable("orderCheckOut",) { OrderCheckOut(navController, model) }
                }

                navigation(startDestination = "orderTrack", route = "order_stack") {
                    composable("orderTrack") {
                        OrderTrack(model, navController)
                    }
                }

                navigation(
                    startDestination = if (tabScreen == "profile_stack") lastScreen else "profileScreen",
                    route = "profile_stack"
                ) {
                    composable("firstRegistration") { PrimaRegistrazione(model, navController) }
                    composable("profileScreen") { ProfileScreen(model, navController) }
                    composable("profileEdit") { EditProfile(model, navController) }
                    composable("billingEdit") { EditBilling(model, navController) }
                    composable("lastOrder") { LastOrderScreen(model, navController) }
                }
            }

        }
    } else {
        LoadingScreen("Loading data...")
    }
}


fun getTabScreenFromLastScreen(screen: String): String {
    return when (screen) {
        "homeScreen", "menuDetail", "orderCheckOut" -> "home_stack"
        "orderTrack" -> "order_stack"
        "firstRegistration", "profileScreen", "profileEdit", "billingEdit", "lastOrder" -> "profile_stack"
        else -> "home_stack"
    }
}




