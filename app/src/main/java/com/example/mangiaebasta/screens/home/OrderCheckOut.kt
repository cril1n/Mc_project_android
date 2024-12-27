package com.example.mangiaebasta.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mangiaebasta.R
import com.example.mangiaebasta.components.TopBarWithBackArrow
import com.example.mangiaebasta.model.MenuDetailed
import com.example.mangiaebasta.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
fun OrderCheckOut(menuString: String, navController: NavHostController, model: MainViewModel) {

    var menu by remember { mutableStateOf<MenuDetailed?>(null) }
    val address by model.address.collectAsState()


    LaunchedEffect(Unit) {
        menu = Json.decodeFromString<MenuDetailed>(menuString)
        model.loadAdress()
        Log.d("OrderCheckOut", "$address")
    }

    UserStatusDialog(model, navController)

    Column {
        TopBarWithBackArrow("Order check out", navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.cart),
                contentDescription = "",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)

            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    menu?.let {
                        Text(text = "Menu Details", style = MaterialTheme.typography.h6)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Name: ${it.name}")
                        Text(text = "Price: ${it.price}")
                        Text(text = "Delivery Time: ${it.deliveryTime}")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Address Details", style = MaterialTheme.typography.h6)
                    Log.d("OrderCheckOut", "$address")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Country: ${address?.countryCode}")
                    Text(text = "Region: ${address?.adminArea}")
                    Text(text = "City: ${address?.locality}")
                    Text(text = "Street: ${address?.featureName}")
                    Text(text = "Number: ${address?.subLocality}")

                }
            }
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {

                        model.sendOrder()
                        model.setShowDialog()
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Confirm Order")
            }
        }

    }


}


@Composable
fun UserStatusDialog(model: MainViewModel, navController: NavHostController) {
    val userStatus by model.userStatus.collectAsState()
    // Stato per mostrare o nascondere il dialogo
    val showDialog by model.showDialog.collectAsState()

    Log.d("UserStatusDialog", "UserStatus: $userStatus")
    Log.d("UserStatusDialog", "ShowDialog: $showDialog")

    // Contenuti dinamici del dialogo in base a `userStatus`
    val dialogData = when (userStatus) {
        "missingInfo" -> Pair(
            "Missing personal info",
            "You need to complete your personal info before ordering"
        )

        "missingBilling" -> Pair(
            "Missing billing info",
            "You need to complete your billing info before ordering"
        )
        "onDelivery" -> Pair(
            "Order already on delivery",
            "You already have an order on delivery, you can check the status in the order section"
        )
        else -> null
    }

    // Mostra il dialogo solo se `dialogData` non è null e `showDialog` è true
    dialogData?.let { (title, message) ->
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    model.setShowDialog(false)
                },
                title = {
                    Text(text = title)
                },
                text = {
                    Text(text = message)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            model.setShowDialog(false)
                            // Aggiungi azioni di conferma qui
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            model.setShowDialog(false)
                        }
                    ) {
                        Text("Close")
                    }
                }
            )
        }
    }
}


