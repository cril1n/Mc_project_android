package com.example.mangiaebasta.screens.home

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mangiaebasta.R
import com.example.mangiaebasta.components.TopBarWithBackArrow
import com.example.mangiaebasta.viewmodel.MainViewModel
import kotlin.io.encoding.ExperimentalEncodingApi

@Composable
fun MenuDetail(mid: Int, image: String, navController: NavController, model: MainViewModel) {

    val menuDetailed by model.menuDetailed.collectAsState()
    val imageMenu by model.imageMenu.collectAsState()

    LaunchedEffect(menuDetailed) {
        model.loadMenuDetailed(mid)
        model.setImageMenu(mid)
    }

    Log.d("MenuDetail", "MenuDetailed: $menuDetailed")
    Log.d("MenuDetail", "ImageMenu: ${imageMenu?.base64?.slice(0..10)}")

    Column(
        Modifier.fillMaxSize(),
    ) {
        TopBarWithBackArrow("Menu Detail", navController)
        if (menuDetailed.name != null) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Column(){
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Immagine Menu",
                    )

                }
                Column(){
                    Row{
                    }
                    Row{
                        Text(menuDetailed.name!!)
                    }
                    Row{
                        Text(menuDetailed.shortDescription!!)
                    }
                }
            }
        } else {
            Text(text = "Loading...")
        }
    }
}

