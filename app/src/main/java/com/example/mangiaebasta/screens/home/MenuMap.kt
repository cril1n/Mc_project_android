package com.example.mangiaebasta.screens.home

import android.graphics.Bitmap
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mangiaebasta.R
import com.example.mangiaebasta.model.MenuWImage
import com.example.mangiaebasta.viewmodel.MainViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationState
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun MenuMap(
    menuList: List<MenuWImage>,
    location: Location?,
    navController: NavController,
) {

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            if (location != null) {
                center(Point.fromLngLat(location.longitude, location.latitude))
            }
            zoom(15.5)
            pitch(0.0)
        }
    }
    MapboxMap(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        mapViewportState = mapViewportState
    ) {

        // Centra la mappa manualmente ogni volta che la posizione cambia
        LaunchedEffect(location) {
            mapViewportState.setCameraOptions {
                if (location != null) {
                    center(Point.fromLngLat(location.longitude, location.latitude))
                }
                zoom(14.0)  // Zoom a livello strada
                pitch(0.0)  // Vista dall'alto perpendicolare
            }
        }

        // Disabilita il puck di default
        MapEffect(Unit) { mapView ->
            mapView.mapboxMap.loadStyle("mapbox://styles/guerine/cm4sjsro8000201s7hg2t66un")
            mapView.location.updateSettings {

                locationPuck = createDefault2DPuck(withBearing = false).apply {
                    bearingImage = null
                    shadowImage = null
                    topImage = null
                }
                enabled = false
            }

        }


        // Aggiungi l'icona personalizzata nella posizione corrente
        val userMarker = rememberIconImage(
            key = R.drawable.homeicon,
            painter = painterResource(R.drawable.homeicon)
        )
        if (location != null) {
            PointAnnotation(
                point = Point.fromLngLat(location.longitude, location.latitude)
            ) {
                iconImage = userMarker
                iconSize = 0.2
                iconOpacity = 3.0
            }
        }
        // Funzione di estensione per ridimensionare un Bitmap
        fun Bitmap.resize(newWidth: Int, newHeight: Int): Bitmap {
            return Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
        }

        menuList.forEach { menu ->

            val roundedBitmap = RoundedMarkerWithBorder(
                bitmap = menu.image.resize(120, 120),
            )

            val menuMarker = rememberIconImage(
                key = menu.image,
                painter = BitmapPainter(roundedBitmap.asImageBitmap())
            )

            PointAnnotation(
                point = Point.fromLngLat(menu.menu.location.lng, menu.menu.location.lat),
                init = fun PointAnnotationState.() {
                    iconImage = menuMarker
                    textField = menu.menu.name
                    textOffset = listOf(0.0, 1.9)
                    iconSize = 1.0
                    iconOpacity = 3.0
                },
                onClick = {
                    // Azione al clic, ad esempio mostrare un Toast o navigare
                    Log.d("MenuMap", "Cliccato su ${menu.menu.name}")

                    // Navigazione (se necessario)
                    navController.navigate("menuDetail/${menu.menu.mid}/${menu.image}")

                    true // Return true per indicare che il clic è stato gestito
                }
            )
        }
    }
}
