package com.example.mangiaebasta.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.NavController
import com.example.mangiaebasta.model.MenuWImage
import com.example.mangiaebasta.viewmodel.MainViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuCard(menu: MenuWImage, navController: NavController, model: MainViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            model.setSelectedMid(menu.menu.mid)
            model.setImageForDetail(menu.image)
            navController.navigate("menuDetail")
        },
        elevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(8.dp)
        ) {
            // Immagine del menu
            Box(
                modifier = Modifier
                    .size(104.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Image(
                    modifier = Modifier
                        .width(300.dp)
                        .height(300.dp),
                    bitmap = menu.image.asImageBitmap(),
                    contentDescription = "Menu Image"
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Informazioni del menu
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = menu.menu.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = menu.menu.shortDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "€${menu.menu.price}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFF99501)
                    )

                    Text(
                        text = "Delivery time: ${menu.menu.deliveryTime} min",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

