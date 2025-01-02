package com.example.mangiaebasta.screens.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mangiaebasta.components.TopBarWithBackArrow
import com.example.mangiaebasta.viewmodel.MainViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import com.example.mangiaebasta.LoadingScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Composable
fun MenuDetail(navController: NavController, model: MainViewModel) {


    val menuDetailed = model.menuDetailed.collectAsState().value
    val imageMenuDetailed = model.imageMenuDetailed.collectAsState().value

    // Colori personalizzati
    val orangeColor = Color(0xFFFF9800)
    val darkTextColor = Color(0xFF000000)
    val labelTextColor = Color(0xFF666666)

    LaunchedEffect(Unit) {
        model.loadMenuDetailed()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        TopBarWithBackArrow("Menu Detail","homeScreen", navController, model)

        if (menuDetailed.mid != null && imageMenuDetailed != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Immagine
                imageMenuDetailed?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Menu Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp,
                    backgroundColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                    ) {
                        // Titolo
                        Text(
                            text = menuDetailed.name ?: "N/A",
                            style = MaterialTheme.typography.h5.copy(
                                fontWeight = FontWeight.Bold,
                                color = darkTextColor
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Prezzo e Tempo di Consegna
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Price:",
                                    style = MaterialTheme.typography.body1.copy(
                                        color = labelTextColor
                                    )
                                )
                                Text(
                                    text = "€${menuDetailed.price ?: "N/A"}",
                                    style = MaterialTheme.typography.h6.copy(
                                        color = darkTextColor,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }

                            Column {
                                Text(
                                    text = "Delivery time:",
                                    style = MaterialTheme.typography.body1.copy(
                                        color = labelTextColor
                                    )
                                )
                                Text(
                                    text = "${menuDetailed.deliveryTime ?: "N/A"} minutes",
                                    style = MaterialTheme.typography.h6.copy(
                                        color = darkTextColor,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Descrizione
                        Text(
                            text = "Description:",
                            style = MaterialTheme.typography.h6.copy(
                                color = darkTextColor,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = menuDetailed.longDescription ?: "No description available",
                            style = MaterialTheme.typography.body1.copy(
                                color = labelTextColor
                            )
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Pulsante Order Now
                        Button(
                            onClick = {
                                val jsonString = Json.encodeToString(menuDetailed)
                                navController.navigate("orderCheckOut/${jsonString}")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = orangeColor),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "Order Now!",
                                color = Color.White,
                                style = MaterialTheme.typography.button.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        } else {
            LoadingScreen("Loading menu details...")
        }
    }
}