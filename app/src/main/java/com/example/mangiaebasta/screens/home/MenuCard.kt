package com.example.mangiaebasta.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mangiaebasta.model.MenuWImage

@Composable
fun MenuCard(menu: MenuWImage, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = { navController.navigate("menuDetail/${menu.menu.mid}") }
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
                        color = MaterialTheme.colorScheme.primary
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

