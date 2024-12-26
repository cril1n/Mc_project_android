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
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mangiaebasta.R
import com.example.mangiaebasta.model.LocationData
import com.example.mangiaebasta.model.MenuDetailed
import com.example.mangiaebasta.viewmodel.MainViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun OrderTrack(model: MainViewModel, menuDetailed: String) {

    model.setInitialRegion()

    val orderOnDelivery = model.orderOnDelivery.collectAsState()
    val initialRegion = model.initialRegion.collectAsState()

    val jsonString: String = Json.encodeToString(
        MenuDetailed(
            1,
            "Pizza",
            2.4,
            LocationData(45.56, 9.23),
            1,
            10,
            "Pizza",
            "Pizza"
        )
    )

    Column(

    ) {
        OrderTrackTopBar()
        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            if (orderOnDelivery.value != null) {

                OrderDetail(orderOnDelivery.value!!, jsonString)
                Spacer(modifier = Modifier.height(12.dp))
                OrderMap(orderOnDelivery.value!!, initialRegion.value)

            } else {

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Descrizione immagine",
                    modifier = Modifier
                        .size(250.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = "No order in progress",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
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
