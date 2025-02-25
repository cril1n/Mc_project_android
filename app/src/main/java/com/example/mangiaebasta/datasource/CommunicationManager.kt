package com.example.mangiaebasta.datasource

import android.net.Uri
import android.util.Log
import com.example.mangiaebasta.model.CreateUserResponse
import com.example.mangiaebasta.model.GetUserResponse
import com.example.mangiaebasta.model.ImageCodeResponse
import com.example.mangiaebasta.model.LocationData
import com.example.mangiaebasta.model.MenuDetailed
import com.example.mangiaebasta.model.NearMenuResponse
import com.example.mangiaebasta.model.OrderResponse
import com.example.mangiaebasta.model.OrderResponseCompleted
import com.example.mangiaebasta.model.OrderResponseOnDelivery
import com.example.mangiaebasta.model.SendOrderRequest
import com.example.mangiaebasta.model.UpdateUserRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


object CommunicationManager {
    private val BASE_URL = "https://develop.ewlab.di.unimi.it/mc/2425"
    var sid: String? = null
    var uid: Int? = null
    private val TAG = CommunicationManager::class.simpleName

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    enum class HttpMethod {
        GET,
        POST,
        DELETE,
        PUT
    }

    suspend fun genericRequest(
        url: String,
        method: HttpMethod,
        queryParameters: Map<String, String?> = emptyMap(),
        requestBody: Any? = null

    ): HttpResponse {
        Log.d(TAG, "Method: $method")
        val urlUri = Uri.parse(url)
        Log.d(TAG, "urlUri: $urlUri")
        val urlBuilder = urlUri.buildUpon()
        Log.d(TAG, "urlBuilder: $urlBuilder")

        queryParameters.forEach { (key, value) ->
            urlBuilder.appendQueryParameter(key, value.toString())
        }
        val completeUrlString = urlBuilder.build().toString()
        Log.d(TAG, "completeUrlString: $completeUrlString")

        try {

        } catch (e: Exception) {
            Log.e(TAG, "Error in genericRequest: $e")
        }


        val result: HttpResponse
        try {
            val request: HttpRequestBuilder.() -> Unit = {
                requestBody?.let {
                    contentType(ContentType.Application.Json)
                    setBody(requestBody)
                }
            }
            result = when (method) {
                HttpMethod.GET -> client.get(completeUrlString, request)
                HttpMethod.POST -> client.post(completeUrlString, request)
                HttpMethod.DELETE -> client.delete(completeUrlString, request)
                HttpMethod.PUT -> client.put(completeUrlString, request)
            }
            if (result.status.value in 200..299) {
                Log.d(TAG, "Request successful")
            } else {
                Log.e(TAG, "Request failed: $result")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in genericRequest: $e")
            throw e
        }
        return result
    }


    fun setSidUid(s: String, u: Int) {
        sid = s
        uid = u
    }

    fun resetSidUid() {
        sid = null
        uid = null
    }

    suspend fun createUser(): CreateUserResponse {
        Log.d(TAG, "createUser")

        val url = "$BASE_URL/user"

        val httpResponse = genericRequest(url, HttpMethod.POST)
        val result: CreateUserResponse = httpResponse.body()
        Log.d(TAG, "Deserialized response: $result")
        setSidUid(result.sid, result.uid)
        //Log.d(TAG, "sid: $sid")
        //Log.d(TAG, "uid: $uid")
        return result
    }

    suspend fun getUser(): GetUserResponse? {

        Log.d(TAG, "getUser")
        if (sid != null && uid != null) {
            val url = "$BASE_URL/user/$uid"
            val queryParameters = mapOf("sid" to sid)
            val httpResponse = genericRequest(url, HttpMethod.GET, queryParameters)
            val result: GetUserResponse = httpResponse.body()
            Log.d(TAG, "Deserialized response: $result")
            return result
        } else Log.d(TAG, "sid or uid are null, create a user before")
        return null
    }

    suspend fun updateUser(user: GetUserResponse): String {
        Log.d(TAG, "updateUser")
        if (sid != null && uid != null) {
            val url = "$BASE_URL/user/$uid"
            Log.d(TAG, "url: $url")
            Log.d(TAG, "user: $user & sid: $sid")
            val requestBody = UpdateUserRequest(
                firstName = user.firstName,
                lastName = user.lastName,
                cardFullName = user.cardFullName,
                cardNumber = user.cardNumber,
                cardExpireMonth = user.cardExpireMonth,
                cardExpireYear = user.cardExpireYear,
                cardCVV = user.cardCVV,
                sid = sid
            )
            Log.d(TAG, "Request body: $requestBody")
            val httpResponse = genericRequest(url, HttpMethod.PUT, requestBody = requestBody)
            if (httpResponse.status.value in 200..299) {
                Log.d(TAG, "User updated")
                return "User updated"
            }
        } else Log.d(TAG, "sid or uid are null, create a user before")
        return "Something went wrong"
    }

    suspend fun sendOrder(mid: Int, latitude: Double, longitude: Double): OrderResponseOnDelivery? {
        Log.d(TAG, "sendOrder")
        Log.d(TAG, "Mid: $mid, Sid: $sid, Uid: $uid")
        try {
            if (sid != null && uid != null) {
                val url = "$BASE_URL/menu/${mid}/buy"
                val locationData = LocationData(latitude, longitude)
                val requestBody = SendOrderRequest(
                    sid = sid!!,
                    deliveryLocation = locationData
                )
                Log.d(TAG, "Request body: ${Json.encodeToString(requestBody)}")
                val httpResponse = genericRequest(url, HttpMethod.POST, requestBody = requestBody)
                Log.d(TAG, "Response: $httpResponse")
                if (httpResponse.status.value !in 200..299) {
                    Log.d(TAG, "Error during order creation")
                    return null
                }
                val result: OrderResponseOnDelivery = httpResponse.body()
                Log.d(TAG, "Deserialized response: $result")
                return result

            }
            Log.d(TAG, "sid or uid are null, create a user before")
            return null
        } catch (e: Exception) {
            Log.e(TAG, "Error in sendOrder: $e")
        }
        return null
    }

    suspend fun getOrderInfo(oid: Int): Any? {
        Log.d(TAG, "getOrderInfo")
        if (sid != null && uid != null) {
            val url = "$BASE_URL/order/$oid"
            val queryParameters = mapOf("sid" to sid)
            val httpResponse = genericRequest(url, HttpMethod.GET, queryParameters)
            Log.d(TAG, "Deserialized response ${httpResponse.body<Any>()}")
            val partialResult: OrderResponse = httpResponse.body()
            if (partialResult.status == "COMPLETED") {
                val result: OrderResponseCompleted = httpResponse.body()
                Log.d(TAG, "Deserialized response: $result")
                return result
            } else if (partialResult.status == "ON_DELIVERY") {
                val result: OrderResponseOnDelivery = httpResponse.body()
                Log.d(TAG, "Deserialized response: $result")
                return result
            }
        } else {
            Log.d(TAG, "sid or uid are null, create a user before")
        }
        return null
    }

    suspend fun getNearMenu(lat: Double, lng: Double): NearMenuResponse? {
        Log.d(TAG, "getNearMenu")

        if (sid != null && uid != null) {
            val url = "$BASE_URL/menu"
            val queryParameters = mapOf(
                "sid" to sid,
                "lat" to lat.toString(),
                "lng" to lng.toString()
            )
            val httpResponse = genericRequest(url, HttpMethod.GET, queryParameters)
            val result: NearMenuResponse = httpResponse.body()
            Log.d(TAG, "Deserialized response (menus): $result")
            return result
        }
        Log.d(TAG, "sid or uid are null, create a user before")
        return null
    }

    suspend fun getMenuImage(mid: Int): ImageCodeResponse? {
        Log.d(TAG, "getMenuImage")

        if (sid != null && uid != null) {
            val url = "$BASE_URL/menu/$mid/image"
            val queryParameters = mapOf("sid" to sid)
            val httpResponse = genericRequest(url, HttpMethod.GET, queryParameters)
            val result: ImageCodeResponse = httpResponse.body()
            Log.d(TAG, "Deserialized response: $result")
            return result
        }
        Log.d(TAG, "sid or uid are null, create a user before")
        return null
    }

    suspend fun getMenuDetail(mid: Int, lat: Double, lng: Double): MenuDetailed? {
        Log.d(TAG, "getMenuDetail")

        if (sid != null && uid != null) {
            val url = "$BASE_URL/menu/$mid"
            val queryParameters = mapOf(
                "sid" to sid,
                "lat" to lat.toString(),
                "lng" to lng.toString()
            )
            try {
                val httpResponse = genericRequest(url, HttpMethod.GET, queryParameters)
                val result: MenuDetailed = httpResponse.body()
                Log.d(TAG, "Deserialized response (menuDetail): $result")
                return result
            } catch (e: Exception) {
                Log.e(TAG, "Error in getMenuDetail: $e")
            }
        }
        Log.d(TAG, "sid or uid are null, create a user before")
        return null
    }


}

