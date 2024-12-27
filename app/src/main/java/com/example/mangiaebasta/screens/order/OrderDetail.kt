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
import androidx.compose.ui.unit.dp
import com.example.mangiaebasta.model.MenuDetailed
import com.example.mangiaebasta.model.OrderResponseOnDelivery
import kotlinx.serialization.json.Json

@Composable
fun OrderDetail(orderData: OrderResponseOnDelivery, menuString: String) {

    var menu by remember { mutableStateOf<MenuDetailed?>(null) }

    LaunchedEffect(Unit) {
        menu = Json.decodeFromString<MenuDetailed>(menuString)
    }

    Card(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(1f / 3f),
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(text = "Your order is on delivery", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Delivery expected by: ${orderData.expectedDeliveryTimestamp}")
            Text(text = "Menu: ${menu?.name}")
            Text(text = "Price: ${menu?.price} €")
        }
    }
}