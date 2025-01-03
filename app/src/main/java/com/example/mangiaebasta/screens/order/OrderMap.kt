package com.example.mangiaebasta.screens.order


import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mangiaebasta.R
import com.example.mangiaebasta.model.InitialRegion
import com.example.mangiaebasta.model.LocationData
import com.example.mangiaebasta.model.OrderResponseOnDelivery
import com.example.mangiaebasta.viewmodel.MainViewModel
import com.mapbox.geojson.BoundingBox
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraBoundsOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotationState
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import kotlin.math.max
import kotlin.math.log

@Composable
fun OrderMap(orderData: OrderResponseOnDelivery, initialRegion: InitialRegion) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp, 0.dp, 10.dp, 10.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {


        val mapViewportState = rememberMapViewportState {
            // Calcola il centro geografico
            val centerLat = initialRegion.center.lat!!
            val centerLng = initialRegion.center.lng!!

            // Calcola i limiti geografici
            val minLat = initialRegion.center.lat!! - initialRegion.deltaY!! / 2
            val maxLat = initialRegion.center.lat!! + initialRegion.deltaY!! / 2
            val minLng = initialRegion.center.lng!! - initialRegion.deltaX!! / 2
            val maxLng = initialRegion.center.lng!! + initialRegion.deltaX!! / 2

            // Configura le opzioni della camera
            setCameraOptions {
                center(Point.fromLngLat(centerLng, centerLat)) // Centro della mappa
                zoom(
                    calculateZoomLevel(
                        minLat,
                        maxLat,
                        minLng,
                        maxLng
                    )
                ) // Calcolo del livello di zoom
                pitch(0.0)
            }
        }



        MapboxMap(
            modifier = Modifier
                .fillMaxWidth(),
            mapViewportState = mapViewportState
        ) {
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
            val userLocation = Point.fromLngLat(
                orderData.deliveryLocation.lng!!,
                orderData.deliveryLocation.lat!!
            )
            val droneLocation = Point.fromLngLat(
                orderData.currentPosition.lng!!,
                orderData.currentPosition.lat!!
            )
            PolylineAnnotation(
                points = listOf(userLocation, droneLocation),
                polylineAnnotationState = remember {
                    PolylineAnnotationState().apply {
                        this.lineColor = Color(0xFFFFA500) // Arancione
                        this.lineWidth = 4.0      // Spessore della linea
                    }
                }
            )
            // Aggiungi l'icona personalizzata nella posizione corrente
            val userMarker = rememberIconImage(
                key = R.drawable.homeicon,
                painter = painterResource(R.drawable.homeicon)
            )

            PointAnnotation(point = userLocation) {
                iconImage = userMarker
                iconSize = 0.2
                iconOpacity = 3.0
            }

            val droneMarker = rememberIconImage(
                key = R.drawable.drone,
                painter = painterResource(R.drawable.drone)
            )

            PointAnnotation(point = droneLocation) {
                iconImage = droneMarker
                iconSize = 0.2
                iconOpacity = 3.0
            }
        }
    }
}

// Funzione per calcolare il livello di zoom basato sui limiti
fun calculateZoomLevel(minLat: Double, maxLat: Double, minLng: Double, maxLng: Double): Double {
    val latDelta = maxLat - minLat
    val lngDelta = maxLng - minLng

    // Calcolo semplice per derivare uno zoom di base (può essere personalizzato)
    return 7.0 - log(max(latDelta, lngDelta), 2.0)
}
