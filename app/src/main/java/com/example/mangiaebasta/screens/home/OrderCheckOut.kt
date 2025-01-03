package com.example.mangiaebasta.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

    // Definisci i colori personalizzati
    val orangeColor = Color(0xFFFF9800)
    val darkTextColor = Color(0xFF000000)
    val labelTextColor = Color(0xFF666666)

    LaunchedEffect(Unit) {
        menu = Json.decodeFromString<MenuDetailed>(menuString)
        model.loadAdress()
    }

    UserStatusDialog(model, navController)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        TopBarWithBackArrow("Order check out","menuDetail", navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.cart),
                contentDescription = "Shopping Cart",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp),
                colorFilter = ColorFilter.tint(orangeColor)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Order Section
                    Column {
                        Text(
                            text = "Order:",
                            style = MaterialTheme.typography.h6.copy(
                                color = darkTextColor,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        menu?.let {
                            it.name?.let { it1 -> DetailRow("Name:", it1) }
                            DetailRow("Delivery time:", "${it.deliveryTime} min")
                            DetailRow("Price:", "${it.price} €")
                        }
                    }

                    // Address Section
                    Log.d("ADDRESS", "$address")
                    Column {
                        Text(
                            text = "Address:",
                            style = MaterialTheme.typography.h6.copy(
                                color = darkTextColor,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        address?.let {
                            DetailRow("Region:", it.adminArea ?: "")
                            DetailRow("City:", it.locality ?: "")
                            DetailRow("Street:", it.thoroughfare ?: "")
                            DetailRow("n°:", it.featureName ?: "")
                        }
                    }
                }
            }

            Text(
                text = "Are you ready to confirm your order? Make sure your data are correct!",
                style = MaterialTheme.typography.body1.copy(
                    color = labelTextColor,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
            )

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        menu?.mid?.let { model.sendOrder(it, navController) }
                        model.setShowDialog()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(
                        0xFFF99501
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CONFIRM ORDER",
                        color = Color.White,
                        style = MaterialTheme.typography.button.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.body1.copy(
                color = Color(0xFF666666),
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1.copy(
                color = Color(0xFF000000)
            )
        )
    }
}

@Composable
fun UserStatusDialog(model: MainViewModel, navController: NavHostController) {
    Log.d("UserStatusDialog", "nav graph ${navController.visibleEntries}")
    val userStatus by model.userStatus.collectAsState()
    val showDialog by model.showDialog.collectAsState()

    // Struttura per mappare lo stato dell'utente ai dati del dialogo e alla rotta
    val dialogData = when (userStatus) {
        "missingInfo" -> Triple(
            "Missing personal info",
            "You need to complete your personal info before ordering",
            "profileScreen"
        )
        "missingBilling" -> Triple(
            "Missing billing info",
            "You need to complete your billing info before ordering",
            "billingEdit"
        )
        "onDelivery" -> Triple(
            "Order already on delivery",
            "You already have an order on delivery, you can check the status in the order section",
            "orderTrack"
        )
        else -> null
    }

    dialogData?.let { (title, message, route) ->
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
                            navController.navigate(route)
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
