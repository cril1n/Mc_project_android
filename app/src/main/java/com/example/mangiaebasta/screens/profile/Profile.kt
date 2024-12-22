//package com.example.mangiaebasta.screens.profile
//
//import android.util.Log
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.mangiaebasta.R
//import com.example.mangiaebasta.model.User
//import com.example.mangiaebasta.viewmodel.MainViewModel
//
//@Composable
//fun Profile(model: MainViewModel, user: User) {
//    // Creiamo un NavController che sarà responsabile della navigazione di Profile
//    val navController = rememberNavController()
//    var startDestination = model.startDestination.collectAsState()
//    val user = model.user.collectAsState().value
//
//    Log.d("Profile", "User: $user")
//    if (!user.firstName.isEmpty() || !user.lastName.isEmpty()) {
//        Log.d("Profile", "User name is not empty")
//        model.setStartDestination("profile")
//    } else {
//        Log.d("Profile", "User name is empty")
//        model.setStartDestination("firstRegistration")
//    }
//    // Mostra "PrimaRegistrazione" se il nome o il cognome è vuoto
//    NavHost(navController = navController, startDestination = startDestination.value) {
//        // Definiamo il NavHost, che contiene tutte le destinazioni
//        composable("firstRegistration") { PrimaRegistrazione(model, navController) }
//        composable("profile") { ProfileScreen(user, navController) }
//        composable("profileEdit") { EditProfile(model, user, navController) }
//        composable("billingEdit") { EditBilling(model, user, navController) }
//        composable("lastOrder") { LastOrderScreen(user, navController) }
//    }
//
//
//}
//
//
//
//
