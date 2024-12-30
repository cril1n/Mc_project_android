package com.example.mangiaebasta.screens.order

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mangiaebasta.R
import com.example.mangiaebasta.viewmodel.MainViewModel
import kotlinx.coroutines.delay


@Composable
fun OrderTrack(model: MainViewModel, navController: NavController) {
    val user = model.user.collectAsState()
    val orderOnDelivery = model.orderOnDelivery.collectAsState()
    val initialRegion = model.initialRegion.collectAsState()
    val menuOrdered = model.menuOrdered.collectAsState()
    val orderOnFocus = model.orderOnFocus.collectAsState()


    // Monitoriamo i cambiamenti di route
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    // Aggiorniamo lo stato quando la route cambia
    LaunchedEffect(currentRoute) {
        if (currentRoute != "orderTrack") {
            model.setOrderOnFocus(false)
        } else {
            model.setOrderOnFocus(true)
            model.setLastScreen(currentRoute)
        }
    }

    // Effetto separato per gestire il polling
    LaunchedEffect(orderOnFocus.value) {
        Log.d("OrderTrack", "LaunchedEffect Unit")
        if (user.value.orderStatus == "ON_DELIVERY") {
            Log.d("OrderTrack", "LaunchedEffect")
            model.setOrderOnDelivery()
            model.setMenuOrdered()
        }

        while (orderOnFocus.value) {
            Log.d("OrderTrack", "User status: ${user.value}")
            Log.d("OrderTrack", "OnDelivery order status: ${orderOnDelivery.value}")
            if (user.value.orderStatus == "COMPLETED" || orderOnDelivery.value?.status != "ON_DELIVERY") {
                break
            }
            model.setOrderOnDelivery()

            delay(5000)
        }

    }

    OrderCompleteDialog(model, navController)

    Column {
        OrderTrackTopBar()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Log.d("OrderTrack", "OrderOnDelivery: ${orderOnDelivery.value}")
            Log.d("OrderTrack", "Menu String: ${menuOrdered.value}")

            if (orderOnDelivery.value != null && menuOrdered.value != null) {
                if (user.value.orderStatus != null && user.value.orderStatus != "COMPLETED") {
                    OrderDetail(
                        orderOnDelivery.value!!,
                        model
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OrderMap(orderOnDelivery.value!!, initialRegion.value)
                } else {
                    NoOrderTrack()
                }
            } else {
                NoOrderTrack()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackTopBar() {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = "Order Track",
                    color = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF390073) // Colore di sfondo
        )
    )
}

@Composable
fun NoOrderTrack() {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Descrizione immagine",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp)
            )
            Text("Nessuna ordine in corso")
        }
    }
}


@Composable
fun OrderCompleteDialog(model: MainViewModel, navController: NavController) {
    val showDialog by model.showOrderCompleteDialog.collectAsState()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                model.setOrderCompleteShowDialog(false)
            },
            title = {
                Text(text = "Order completed")
            },
            text = {
                Text(text = "Your order is ready!")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        model.setOrderCompleteShowDialog(false)
                        navController.navigate("homeScreen")
                    }
                ) {
                    Text("Order other food")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        model.setOrderCompleteShowDialog(false)
                    }
                ) {
                    Text("Close")
                }
            }
        )
    }
}

