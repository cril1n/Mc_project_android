package com.example.mangiaebasta.screens.home

import android.location.Location
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mangiaebasta.model.MenuWImage
import com.example.mangiaebasta.viewmodel.MainViewModel
import kotlinx.coroutines.launch


@Composable
fun SwipeableMenus(menuList: List<MenuWImage>, navController: NavController, model: MainViewModel) {
    var currentIndex by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        menuList.forEachIndexed { index, menu ->
            key(menu.menu.mid) {
                SwipeableMenuItem(
                    menu = menu,
                    index = index,
                    currentIndex = currentIndex,
                    onSwipeUp = {
                        if (currentIndex < menuList.size - 1) {
                            scope.launch { currentIndex++ }
                        }
                    },
                    onSwipeDown = {
                        if (currentIndex > 0) {
                            scope.launch { currentIndex-- }
                        }
                    }
                )
            }
        }

        // Navigation indicators
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            if (currentIndex > 0) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Previous menu",
                    modifier = Modifier.size(24.dp)
                )
            }
            if (currentIndex < menuList.size - 1) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Next menu",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun SwipeableMenuItem(
    menu: MenuWImage,
    index: Int,
    currentIndex: Int,
    onSwipeUp: () -> Unit,
    onSwipeDown: () -> Unit
) {
    val offset by animateFloatAsState(targetValue = (index - currentIndex).toFloat())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                translationY = offset * size.height
            }
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    when {
                        dragAmount < -50 -> onSwipeUp()
                        dragAmount > 50 -> onSwipeDown()
                    }
                }
            }
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Image(
                    bitmap = menu.image.asImageBitmap(),
                    contentDescription = "Menu Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = menu.menu.name ?: "N/A",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Price: €${menu.menu.price ?: "N/A"}",
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Delivery Time: ${menu.menu.deliveryTime ?: "N/A"} minutes",
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Description:",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = menu.menu.shortDescription ?: "No description available",
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { /* TODO: Implement order functionality */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Order Now")
                }
            }
        }
    }
}
