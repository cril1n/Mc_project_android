package com.example.mangiaebasta.screens.order

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mangiaebasta.model.MenuDetailed
import com.example.mangiaebasta.model.OrderResponseOnDelivery
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun OrderDetail(orderData: OrderResponseOnDelivery, menuString: String, model: MainViewModel) {
    //var menu by remember { mutableStateOf<MenuDetailed?>(null) }
    val menu by model.menuOrdered.collectAsState()

    // Definisci i colori personalizzati
    val orangeColor = Color(0xFFFF9800)
    val darkTextColor = Color(0xFF000000)
    val labelTextColor = Color(0xFF666666)

    /*LaunchedEffect(Unit) {
        try {
            menu = Json.decodeFromString<MenuDetailed>(menuString)
            Log.d("OrderDetail", "Menu decoded: ${menu?.price}")
        } catch (e: Exception) {
            Log.e("OrderDetail", "Error decoding menu: ${e.message}")
        }
    }*/
    Log.d("OrderDetail", "Menu decoded: $menu")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f / 3f),
        elevation = 4.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Your order is on delivery",
                style = MaterialTheme.typography.h5.copy(
                    color = orangeColor,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Delivery Time Section
                Column {
                    Text(
                        text = "Delivery expected by:",
                        style = MaterialTheme.typography.body1.copy(
                            color = labelTextColor
                        )
                    )
                    Text(
                        text = formatDateTime(orderData.expectedDeliveryTimestamp),
                        style = MaterialTheme.typography.body1.copy(
                            color = darkTextColor,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                // Menu Section
                Column {
                    Text(
                        text = "Menu:",
                        style = MaterialTheme.typography.body1.copy(
                            color = labelTextColor
                        )
                    )
                    Text(
                        text = menu?.name ?: "",
                        style = MaterialTheme.typography.body1.copy(
                            color = darkTextColor,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                // Price Section
                Column {
                    Text(
                        text = "Price:",
                        style = MaterialTheme.typography.body1.copy(
                            color = labelTextColor
                        )
                    )
                    Log.d("PREZZO", "Price: ${menu?.price}")
                    /*menu?.price?.let { price ->
                        Text(
                            text = "%.2f €".format(price),
                            style = MaterialTheme.typography.body1.copy(
                                color = darkTextColor,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }*/
                    Text(
                        text = "${menu?.price}",
                        style = MaterialTheme.typography.body1.copy(
                            color = darkTextColor,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

// Funzione di utilità per formattare la data
private fun formatDateTime(timestamp: String): String {
    return try {
        val instant = Instant.parse(timestamp)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a")
        localDateTime.format(formatter)
    } catch (e: Exception) {
        timestamp
    }
}