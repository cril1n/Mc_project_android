package com.example.mangiaebasta.viewmodel

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Base64
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.mangiaebasta.datasource.CommunicationManager
import com.example.mangiaebasta.datasource.DatabaseManager
import com.example.mangiaebasta.datasource.DatastoreManager
import com.example.mangiaebasta.datasource.LocationManager
import com.example.mangiaebasta.model.CreateUserResponse
import com.example.mangiaebasta.model.GetUserResponse
import com.example.mangiaebasta.model.InitialRegion
import com.example.mangiaebasta.model.LocationData
import com.example.mangiaebasta.model.Menu
import com.example.mangiaebasta.model.MenuDetailed
import com.example.mangiaebasta.model.MenuWImage
import com.example.mangiaebasta.model.OrderResponse
import com.example.mangiaebasta.model.OrderResponseCompleted
import com.example.mangiaebasta.model.OrderResponseOnDelivery
import com.example.mangiaebasta.repositories.ImageRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.abs


class MainViewModel(
    private val databaseManager: DatabaseManager,
    private val dataStoreManager: DatastoreManager,
    private val locationManager: LocationManager,
    private val geocoder: Geocoder
) : ViewModel() {
    private val imageRepo = ImageRepo(databaseManager)
    private val TAG = MainViewModel::class.simpleName
    private var sid: String? = null
    private var uid: Int? = null


    // APP

    //RECUPERO USER DAL DATASTORE
    init {
        viewModelScope.launch {
            dataStoreManager.getUser().collect {
                Log.d("TAG", "GetUserResponse from datastore: $it")
                _user.value = it
            }
        }
    }

    private val _initialized = MutableStateFlow(false)
    val initialized: StateFlow<Boolean> = _initialized

    private val _firstRun = MutableStateFlow(true)
    val firstRun: StateFlow<Boolean> = _firstRun

    suspend fun checkFirstRun(): Boolean {
        return try {
            Log.d(TAG, "Checking first run")
            sid = dataStoreManager.getSidFromDataStore()
            uid = dataStoreManager.getUidFromDataStore()
            if (sid == null || sid == "null") {
                Log.d(TAG, "First run")
                _firstRun.value = true
                _initialized.value = true
                true
            } else {
                Log.d(TAG, "Not first run sid: $sid, uid: $uid")
                CommunicationManager.setSidUid(sid!!, uid!!)
                _firstRun.value = false
                _initialized.value = true
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in checkFirstRun: $e")
            false
        }
    }


    //LOCATION
    private var _locationPermissionGranted = MutableStateFlow(false)
    val locationPermissionGranted: StateFlow<Boolean> = _locationPermissionGranted

    private var _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    suspend fun checkLocationPermission(permissionLauncher: ManagedActivityResultLauncher<String, Boolean>) {
        _locationPermissionGranted.value = locationManager.hasLocationPermission()
        if (_locationPermissionGranted.value) {
            calculateCurrentLocation()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    suspend fun calculateCurrentLocation() {
        _locationPermissionGranted.value = locationManager.hasLocationPermission()
        if (_locationPermissionGranted.value) {
            _location.value = locationManager.getCurrentLocation()
        }
    }

    private var _address = MutableStateFlow<Address?>(null)
    val address: StateFlow<Address?> = _address

    fun loadAdress() {
        val addresses = location.value?.let {
            geocoder.getFromLocation(
                it.latitude,
                it.longitude,
                1
            )
        }
        if (addresses != null) {
            _address.value = addresses[0]
        }
    }

    //HOMESCREEN

    private val _selectedSection = MutableStateFlow(1)
    val selectedSection: StateFlow<Int> = _selectedSection

    fun switchScreen() {
        if (_selectedSection.value == 1) {
            _selectedSection.value = 2
        } else {
            _selectedSection.value = 1
        }
    }

    private val _menuList = MutableStateFlow<List<MenuWImage>>(emptyList())
    val menuList: StateFlow<List<MenuWImage>> = _menuList

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun loadMenuList() {
        Log.d("MainViewModel", "Loading menu list")
        try {
            val rawList = CommunicationManager.getNearMenu(
                location.value!!.latitude,
                location.value!!.longitude
            )
            Log.d("MainViewModel", "Menu list loaded")
            val updatedList = rawList?.map { menu ->
                val base64 = imageRepo.getImage(menu)
                val byteArray = Base64.decode(base64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                MenuWImage(
                    menu = menu,
                    image = bitmap
                )
            } ?: emptyList()
            _menuList.value = updatedList
        } catch (e: Exception) {
            Log.e("MainViewModel", "Error in loadMenuList: $e")
        }
    }


    //MENU DETAIL

    private var _menuDetailed =
        MutableStateFlow(MenuDetailed(null, null, null, null, null, null, null, null))
    val menuDetailed: StateFlow<MenuDetailed> = _menuDetailed

    private var _imageMenuDetailed = MutableStateFlow<Bitmap?>(null)
    val imageMenuDetailed: StateFlow<Bitmap?> = _imageMenuDetailed

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun loadMenuDetailed(mid: Int) {
        Log.d("MainViewModel", "Loading menu detailed")
        try {
            val menu = CommunicationManager.getMenuDetail(
                mid,
                location.value!!.latitude,
                location.value!!.longitude
            )
            if (menu != null) {
                _menuDetailed.value = menu

            }
        } catch (e: Exception) {
            Log.e("MainViewModel", "Error in loadMenuDetail: $e")
        }
    }

    fun setImageForDetail(image: Bitmap) {
        _imageMenuDetailed.value = image
    }


    //USER

    private val _user = MutableStateFlow<GetUserResponse>(GetUserResponse(null, null, null, null, null, null, null, null, null, null))
    val user: StateFlow<GetUserResponse> = _user

    fun setUser(user: GetUserResponse) {
        _user.value = user
    }

    private val _sid = MutableStateFlow("")
    private val _uid = MutableStateFlow(0)

    suspend fun createNewUser() {
        Log.d(TAG, "Creating new user")
        try {
            val response: CreateUserResponse = CommunicationManager.createUser()
            _sid.value = response.sid
            _uid.value = response.uid
            dataStoreManager.setSidInDataStore(_sid.value)
            dataStoreManager.setUidInDataStore(_uid.value)
            Log.d(TAG, "New user create, sid: ${_sid.value}, uid: ${_uid.value}")
            _firstRun.value = false
            setUser(GetUserResponse(null, null, null, null, null, null, null, uid = _uid.value , null, null))
            Log.d(TAG, "New user: ${_user.value}")
            dataStoreManager.saveUser(_user.value)
        } catch (e: Exception) {
            Log.e(TAG, "Error in createNewUser: $e")
        }
    }

    //ORDERTRACK

    private val _menuOrdered = MutableStateFlow<MenuDetailed?>(null)
    val menuOrdered: StateFlow<MenuDetailed?> = _menuOrdered

    suspend fun setMenuOrdered() {

            val mid = dataStoreManager.getMenuOrdered()
            Log.d("MainViewModel", "Menu ordered: $mid")
            _menuOrdered.value = mid?.let { CommunicationManager.getMenuDetail(it, location.value!!.latitude, location.value!!.longitude) }

    }

    private val _orderOnDelivery = MutableStateFlow<OrderResponseOnDelivery?>(null)
    val orderOnDelivery: StateFlow<OrderResponseOnDelivery?> = _orderOnDelivery

    suspend fun setOrderOnDelivery() {
        try {

                val result = _user.value.lastOid?.let { CommunicationManager.getOrderInfo(it) }
                Log.d(TAG, "Order info: $result")

                when (result) {
                    is OrderResponseOnDelivery -> {
                        _orderOnDelivery.value = result
                        _user.value.orderStatus = "ON_DELIVERY"
                        dataStoreManager.saveUser(_user.value)
                        setInitialRegion(_orderOnDelivery.value!!)
                        Log.d(TAG, "Order on delivery: ${_orderOnDelivery.value}")
                    }
                    is OrderResponseCompleted -> {
                        Log.d(TAG, "Order completed: $result")
                        _user.value.orderStatus = "COMPLETED"
                        dataStoreManager.saveUser(_user.value)
                        _orderOnDelivery.value = null
                    }
                    else -> {
                        Log.d(TAG, "Unexpected order type or null result")
                    }
                }

        } catch (e: Exception) {
            Log.e(TAG, "Error in setOrderOnDelivery: $e")
        }
    }

    private val _orderOnFocus = MutableStateFlow(false)
    val orderOnFocus: StateFlow<Boolean> = _orderOnFocus

    fun setOrderOnFocus(value: Boolean) {
        _orderOnFocus.value = value
    }

    private val _initialRegion =
        MutableStateFlow(InitialRegion(LocationData(null, null), null, null))
    val initialRegion: StateFlow<InitialRegion> = _initialRegion

    fun setInitialRegion(orderData: OrderResponseOnDelivery) {
        initialRegion.value.center.lat =
            (orderData.currentPosition.lat?.plus(orderData.deliveryLocation.lat!!))?.div(2)
        initialRegion.value.center.lng =
            (orderData.currentPosition.lng?.plus(orderData.deliveryLocation.lng!!))?.div(2)
        initialRegion.value.deltaX =
            abs(orderData.currentPosition.lat!! - orderData.deliveryLocation.lat!!)
        initialRegion.value.deltaY =
            abs(orderData.currentPosition.lng!! - orderData.deliveryLocation.lng!!)
    }


    //ORDERCHECKOUT

    private var _userStatus = MutableStateFlow("")
    var userStatus: MutableStateFlow<String> = _userStatus

    private var _showDialog = MutableStateFlow(false)
    var showDialog: MutableStateFlow<Boolean> = _showDialog

    fun setShowDialog() {
        if (_userStatus.value != "") _showDialog.value = true
        else _showDialog.value = false
    }

    fun setShowDialog(value: Boolean) {
        _showDialog.value = value
    }

    fun sendOrder(mid: Int, navController: NavHostController, menuString: String) {
        Log.d("MainViewModel", "Sending order")
        Log.d("MainViewModel", "GetUserResponse: ${Json.encodeToString(_user.value)}")
        if (_user.value.firstName == "" || user.value.lastName == "") {
            _userStatus.value = "missingInfo"
            return
        }
        if (user.value.cardNumber == "" || _user.value.cardExpireMonth == 0 || user.value.cardExpireYear == 0 || _user.value.cardCVV == "" || _user.value.cardFullName == "") {
            Log.d("UserValue", "user value: ${_user.value}")
            _userStatus.value = "missingBilling"
            return
        }
        if (_user.value.orderStatus == "ON_DELIVERY") {
            _userStatus.value = "onDelivery"
            return
        }

        try {
            var orderResponse: OrderResponseOnDelivery? = null
            CoroutineScope(Dispatchers.Main).launch {
                orderResponse = CommunicationManager.sendOrder(
                    mid,
                    location.value!!.latitude,
                    location.value!!.longitude
                )
                orderResponse?.let {
                    _user.value.lastOid = it.oid
                    _user.value.orderStatus = it.status
                    setInitialRegion(it)
                }
                dataStoreManager.saveUser(_user.value)
                setOrderOnDelivery()
                _userStatus.value = ""
                dataStoreManager.saveMenuOrdered(mid)
                setMenuOrdered()
                _user.value.orderStatus = "ON_DELIVERY"
                Log.d("MainViewModel", "Order sent")
                navController.navigate("orderTrack")
            }
        } catch (e: Exception) {
            Log.e("MainViewModel", "Error in sendOrder: $e")
        }
    }

    //PROFILE

    fun isUserRegistered(): Boolean {
        if (_user.value.firstName?.isNotEmpty() == true || _user.value.lastName?.isNotEmpty() == true) {
            Log.d("MainViewModel", "GetUserResponse name is not empty")
            return true
        } else {
            Log.d("MainViewModel", "GetUserResponse name is empty")
            return false
        }
    }


    // EDIT PROFILE

    private val _isEditProfile = MutableStateFlow(false)
    val isEditProfile: StateFlow<Boolean> = _isEditProfile
    private val _firstNameForm = MutableStateFlow(_user.value.firstName)
    val firstNameForm: MutableStateFlow<String?> = _firstNameForm
    private val _lastNameForm = MutableStateFlow(_user.value.lastName)
    val lastNameForm: MutableStateFlow<String?> = _lastNameForm

    fun setFirstNameForm(value: String) {
        _firstNameForm.value = value
    }

    fun setLastNameForm(value: String) {
        _lastNameForm.value = value
    }

    fun switchEditMode() {
        _isEditProfile.value = !_isEditProfile.value
    }

    fun updateUserNameData() {
        //val user = dataStoreManager.getUser()
        //Log.d("updateUserNameData", "Updating user name data with values: ${user}")
        val updatedUser = _user.value.copy(
            firstName = _firstNameForm.value,
            lastName = _lastNameForm.value
        )
        setUser(updatedUser)
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("updateUserNameData", "Updating user: $updatedUser")
            try {
                CommunicationManager.updateUser(updatedUser)
                dataStoreManager.saveUser(updatedUser)
            } catch (e: Exception) {
                Log.e("updateUserNameData", "Error updating user: ${e.message}")
            }
        }

        _userStatus.value = ""
    }



    // EDIT BILLING

    private val _isEditBilling = MutableStateFlow(false)
    val isEditBilling: StateFlow<Boolean> = _isEditBilling
    private val _cardNumberForm = MutableStateFlow(_user.value.cardNumber)
    val cardNumberForm: MutableStateFlow<String?> = _cardNumberForm
    private val _expireMonthForm = MutableStateFlow(_user.value.cardExpireMonth)
    val expireMonthForm: MutableStateFlow<Int?> = _expireMonthForm
    private val _expireYearForm = MutableStateFlow(_user.value.cardExpireYear)
    val expireYearForm: MutableStateFlow<Int?> = _expireYearForm
    private val _cvvForm = MutableStateFlow(_user.value.cardCVV)
    val cvvForm: MutableStateFlow<String?> = _cvvForm
    private val _cardFullNameForm = MutableStateFlow(_user.value.cardFullName)
    val cardFullNameForm: MutableStateFlow<String?> = _cardFullNameForm

    fun switchEditBillingMode() {
        _isEditBilling.value = !_isEditBilling.value
    }

    fun setCardNumberForm(value: String) {
        _cardNumberForm.value = value
    }

    fun setExpireMonthForm(value: Int) {
        _expireMonthForm.value = value
    }

    fun setExpireYearForm(value: Int) {
        _expireYearForm.value = value
    }

    fun setCVVForm(value: String) {
        _cvvForm.value = value
    }

    fun setCardFullNameForm(value: String) {
        _cardFullNameForm.value = value
    }

    fun updateUserCardData() {
        Log.d(TAG, "Updating user card data with values: ${_user.value}")
        if (_cardFullNameForm.value?.let { validateCardField("cardFullName", it) } == true) {
            _user.value.cardFullName = _cardFullNameForm.value
        }
        if (_cardNumberForm.value?.let { validateCardField("cardNumber", it) } == true) {
            _user.value.cardNumber = _cardNumberForm.value
        }
        if (validateCardField("expireMonth", _expireMonthForm.value.toString())) {
            _user.value.cardExpireMonth = _expireMonthForm.value
        }

        if (validateCardField("expireYear", _expireYearForm.value.toString())) {
            _user.value.cardExpireYear = _expireYearForm.value
        }
        if (_cvvForm.value?.let { validateCardField("CVV", it) } == true) {
            _user.value.cardCVV = _cvvForm.value
        }
        Log.d(TAG, "GetUserResponse card data updated with new values: ${_user.value}")
        CoroutineScope(Dispatchers.Main).launch {
            CommunicationManager.updateUser(_user.value)
            dataStoreManager.saveUser(_user.value)
        }
        _userStatus.value = ""
    }


    fun validateCardField(field: String, value: String): Boolean {
        when (field) {
            "cardFullName" -> {
                // Controlla che ci siano solo lettere e un singolo spazio
                if (!value.matches(Regex("^[a-zA-Z]+ [a-zA-Z]+$"))) {
                    return false
                } else return true
            }
            "cardNumber" -> {
                // Permette l'input incrementale di numeri fino a 16 cifre
                if (!value.matches(Regex("^\\d{16}$"))) {
                    return false
                } else return true
            }
            "expireMonth" -> {
                // Permette l'input incrementale di numeri fino a 2 cifre
                if (!value.matches(Regex("^\\d{2}$"))) {
                    return false
                } else return true
            }
            "expireYear" -> {
                // Permette l'input incrementale di numeri fino a 2 cifre
                if (!value.matches(Regex("^\\d{2}$"))) {
                    return false
                } else return true
            }
            "CVV" -> {
                // Permette l'input incrementale di numeri fino a 3 cifre
                if (!value.matches(Regex("^\\d{3}$"))) {
                    return false
                } else return true
            }
            else -> return false
        }
    }

}



