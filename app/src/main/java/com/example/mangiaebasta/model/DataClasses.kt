package com.example.mangiaebasta.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import android.graphics.Bitmap


data class User(
    val firstName: String,
    val lastName: String,
    val cardFullName: String,
    val cardNumber: String,
    val cardExpireMonth: Int,
    val cardExpireYear: Int,
    val cardCVV: String
)

@Serializable
data class CreateUserResponse(
    val sid: String,
    val uid: Int
)

@Serializable
data class GetUserResponse(
    val firstName: String?,
    val lastName: String?,
    val cardFullName: String?,
    val cardNumber: String?,
    val cardExpireMonth: Int?,
    val cardExpireYear: Int?,
    val cardCVV: String?,
    val uid: Int?,
    val lastOid: Int?,
    val orderStatus: String?
)

@Serializable
data class UpdateUserRequest(
    val firstName: String?,
    val lastName: String?,
    val cardFullName: String?,
    val cardNumber: String?,
    val cardExpireMonth: Int?,
    val cardExpireYear: Int?,
    val cardCVV: String?,
    val sid: String?
)

@Serializable
data class LocationData(
    val lat: Double,
    val lng: Double
)

@Serializable
data class SendOrderRequest(
    val sid: String,
    val deliveryLocation: LocationData
)

@Serializable
data class OrderResponse(
    val status: String,
)

@Serializable
data class OrderResponseOnDelivery(
    val oid: Int,
    val mid: Int,
    val uid: Int,
    val creationTimestamp: String,
    val expectedDeliveryTimestamp: String,
    val status: String,
    val deliveryLocation: LocationData,
    val currentPosition: LocationData
)

@Serializable
data class OrderResponseCompleted(
    val oid: Int,
    val mid: Int,
    val uid: Int,
    val creationTimestamp: String,
    val deliveryTimestamp: String,
    val status: String,
    val deliveryLocation: LocationData,
    val currentPosition: LocationData
)

@Serializable
data class Menu(
    val mid: Int,
    val name: String,
    val price: Double,
    val location: LocationData,
    val imageVersion: Int,
    val deliveryTime: Int,
    val shortDescription: String,
)

data class MenuWImage(
    val menu: Menu,
    val image: Bitmap
)

typealias NearMenuResponse = List<Menu>

@Serializable
data class ImageCodeResponse(
    val base64: String
)

@Serializable
data class MenuDetailed(
    val mid: Int,
    val name: String,
    val price: Double,
    val location: LocationData,
    val imageVersion: Int,
    val deliveryTime: Int,
    val shortDescription: String,
    val longDescription: String,
)

@Entity
data class MenuImage(
    @PrimaryKey val mid: Int,
    val base64: String,
    val version: Int
)


