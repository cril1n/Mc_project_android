package com.example.mangiaebasta.screens.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mangiaebasta.model.User
import com.example.mangiaebasta.screens.profile.EditBilling
import com.example.mangiaebasta.screens.profile.EditProfile
import com.example.mangiaebasta.viewmodel.MainViewModel


@Composable
fun Home(model: MainViewModel, user: User) {


    // Creiamo un NavController che sarà responsabile della navigazione di Profile
    val navController = rememberNavController()
    // Definiamo il NavHost, che contiene tutte le destinazioni
    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("homeScreen") { HomeScreen(model, navController) }
        composable(
            "menuDetail/{mid}",
            arguments = listOf(navArgument("mid") { type = NavType.IntType })
        ) { backStackEntry ->
            val mid = backStackEntry.arguments?.getInt("mid")
            MenuDetail(mid!!, navController)
        }
        composable("orderCheckOut") { OrderCheckOut() }
        composable("profileEdit") { EditProfile(user, navController) }
        composable("billingEdit") { EditBilling(user, navController) }
    }
}