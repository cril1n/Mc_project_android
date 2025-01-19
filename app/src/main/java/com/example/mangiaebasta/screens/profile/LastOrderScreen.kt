package com.example.mangiaebasta.screens.profile

import android.util.Log
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mangiaebasta.LoadingScreen
import com.example.mangiaebasta.R
import com.example.mangiaebasta.components.TopBarWithBackArrow
import com.example.mangiaebasta.screens.home.DeliveryTimeTag
import com.example.mangiaebasta.screens.home.PriceTag
import com.example.mangiaebasta.viewmodel.MainViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun LastOrderScreen(model: MainViewModel, navController: NavController) {

    val lastMenuOrdered = model.lastMenuOrdered.collectAsState().value
    val lastOrder = model.lastOrder.collectAsState().value
    val imageLastMenu = model.imageLastMenu.collectAsState()

    LaunchedEffect(Unit) {
        model.setLastMenu()

        model.setLastOrder()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        TopBarWithBackArrow("Last Order Detail","profileScreen", navController)
        Log.d("LastOrderScreen", "Last order: $lastOrder")
        Log.d("LastOrderScreen", "Last menu ordered: $lastMenuOrdered")
        Log.d("LastOrderScreen", "Image last menu: $imageLastMenu")
        if (lastMenuOrdered != null && lastOrder != null && imageLastMenu.value != null) {
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
                        if (imageLastMenu.value != null) {
                            Log.d("LastOrderScreen", "Image last menu: $imageLastMenu")
                            Image(
                                bitmap = imageLastMenu.value!!.asImageBitmap(),
                                contentDescription = "Menu Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                        ) {
                            // Title
                            Text(
                                text = lastMenuOrdered.name ?: "N/A",
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
                                PriceTag(price = lastMenuOrdered.price)
                                DeliveryTimeTag(time = lastMenuOrdered.deliveryTime)
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
                                text = lastMenuOrdered.longDescription ?: "No description available",
                                style = MaterialTheme.typography.body1.copy(
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                ),
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Ordered",
                                style = MaterialTheme.typography.h6.copy(
                                    color = MaterialTheme.colors.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = formatDate(lastOrder.creationTimestamp ) ?: "No description available",
                                style = MaterialTheme.typography.body1.copy(
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                ),
                            )
                        }
                    }
                }
            }
        } else {
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
                Text(
                    text = "No order completed yet",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun formatDate(dateString: String): String {
    // Parsing della stringa in un oggetto ZonedDateTime
    val zonedDateTime = ZonedDateTime.parse(dateString)

    // Formattazione in un formato leggibile
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss")
    return zonedDateTime.format(formatter)
}
