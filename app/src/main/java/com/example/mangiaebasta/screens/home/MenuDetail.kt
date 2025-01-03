package com.example.mangiaebasta.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.mangiaebasta.components.TopBarWithBackArrow
import com.example.mangiaebasta.viewmodel.MainViewModel
import com.example.mangiaebasta.LoadingScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.res.painterResource
import com.example.mangiaebasta.R


@Composable
fun MenuDetail(navController: NavController, model: MainViewModel) {


    val menuDetailed = model.menuDetailed.collectAsState().value
    val imageMenuDetailed = model.imageMenuDetailed.collectAsState().value



    LaunchedEffect(Unit) {
        model.loadMenuDetailed()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        TopBarWithBackArrow("Menu Detail", "homeScreen", navController, model)

        if (menuDetailed.mid != null && imageMenuDetailed != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {



                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .animateContentSize(),
                    backgroundColor = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column {
                        // Image
                        Image(
                            bitmap = imageMenuDetailed.asImageBitmap(),
                            contentDescription = "Menu Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                        ) {
                            // Title
                            Text(
                                text = menuDetailed.name ?: "N/A",
                                style = MaterialTheme.typography.h5.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.onSurface
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Price and Delivery Time
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                PriceTag(price = menuDetailed.price)
                                DeliveryTimeTag(time = menuDetailed.deliveryTime)
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Description
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.h6.copy(
                                    color = MaterialTheme.colors.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = menuDetailed.longDescription ?: "No description available",
                                style = MaterialTheme.typography.body1.copy(
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                ),
                            )


                            Spacer(modifier = Modifier.height(24.dp))

                            // Order Now Button
                            Button(
                                onClick = {
                                    val jsonString = Json.encodeToString(menuDetailed)
                                    navController.navigate("orderCheckOut/${jsonString}")
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
                                Text(
                                    "Order Now",
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
        } else {
            LoadingScreen("Loading menu details...")
        }
    }
}

@Composable
fun PriceTag(price: Double?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFf5f5f5))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.euro),
            contentDescription = "Price",
            tint = Color(0xFFF99501),
            modifier = Modifier.size(21.dp).padding(end = 4.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "${price ?: "N/A"}",
            style = MaterialTheme.typography.subtitle1.copy(
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun DeliveryTimeTag(time: Int?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFf5f5f5))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.clock),
            contentDescription = "Delivery Time",
            tint = Color(0xFFF99501),
            modifier = Modifier.size(21.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "${time ?: "N/A"} min",
            style = MaterialTheme.typography.subtitle1.copy(
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        )
    }
}