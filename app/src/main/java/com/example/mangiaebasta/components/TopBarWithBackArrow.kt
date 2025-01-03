package com.example.mangiaebasta.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mangiaebasta.screens.home.bottomBorder
import com.example.mangiaebasta.viewmodel.MainViewModel

fun Modifier.bottomBorder(color: Color, width: Dp) = this.drawBehind {
    drawLine(
        color = color,
        start = Offset(0f, size.height),  // Inizia dalla parte inferiore della TopAppBar
        end = Offset(size.width, size.height),  // Finisce su tutta la larghezza
        strokeWidth = width.toPx()  // Imposta lo spessore del bordo
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithBackArrow(
    screenName: String,
    screenToGoBackTo: String,
    navController: NavController,
    model: MainViewModel? = null
) {



    fun NavController.navigateBackTo(destination: String) {
        // Ottieni la destinazione attuale
        val currentDestination = this.currentBackStackEntry?.destination?.route

        // Controlla se la destinazione è diversa da quella desiderata
        if (currentDestination == destination) {
            // Se siamo già sulla destinazione desiderata, non fare nulla
            return
        }

        // Naviga alla destinazione desiderata
        this.navigate(destination) {
            // Configura il comportamento di navigazione
            popUpTo(destination) { inclusive = false }
            launchSingleTop = true // Evita duplicazioni
        }
    }

    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = screenName,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(end = 65.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White//Color(0xFFF99501) // Requested background color
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    model?.setSelectedMid(-1)
                    navController.navigateBackTo(screenToGoBackTo)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        },
        modifier = Modifier.bottomBorder(Color(0xFFF99501), 4.dp).shadow(elevation = 8.dp),
    )
}