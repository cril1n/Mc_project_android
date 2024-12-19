package com.example.mangiaebasta.viewmodel

import android.Manifest
import android.graphics.BitmapFactory
import android.location.Location
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.example.mangiaebasta.datasource.CommunicationManager
import com.example.mangiaebasta.datasource.DatabaseManager
import com.example.mangiaebasta.datasource.DatastoreManager
import com.example.mangiaebasta.datasource.LocationManager
import com.example.mangiaebasta.model.MenuDetailed
import com.example.mangiaebasta.model.User
import com.example.mangiaebasta.model.MenuWImage
import com.example.mangiaebasta.repositories.ImageRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class MainViewModel(
    private val databaseManager: DatabaseManager,
    private val dataStoreManager: DatastoreManager,
    private val locationManager: LocationManager
) : ViewModel() {
    private val imageRepo = ImageRepo(databaseManager)
    private var sid: String? = null
    private var uid: Int? = null


    // APP

    private val _initialized = MutableStateFlow(false)
    val initialized: StateFlow<Boolean> = _initialized

    private val _firstRun = MutableStateFlow(true)
    val firstRun: StateFlow<Boolean> = _firstRun

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

    //USER

    private val _user = MutableStateFlow<User>(User("", "", "", "", 0, 0, ""))
    val user: StateFlow<User> = _user

    fun setUserData(user: User) {
        _user.value = user
    }

    fun setUserDataField(field: String, value: String) {
        when (field) {
            "firstName" -> _user.value.firstName = value
            "lastName" -> _user.value.lastName = value
            "cardFullName" -> _user.value.cardFullName = value
            "cardNumber" -> _user.value.cardNumber = value
            "cardExpireMonth" -> _user.value.cardExpireMonth = value.toInt()
            "cardExpireYear" -> _user.value.cardExpireYear = value.toInt()
            "cardCVV" -> _user.value.cardCVV = value
        }
    }

    suspend fun checkFirstRun(): Boolean {
        return try {
            Log.d("MainViewModel", "Checking first run")
            sid = dataStoreManager.getSidFromDataStore().toString()
            uid = dataStoreManager.getUidFromDataStore()
            if (sid == null || sid == "null") {
                Log.d("MainViewModel", "First run")
                _firstRun.value = true
                _initialized.value = true
                true
            } else {
                Log.d("MainViewModel", "Not first run sid: $sid, uid: $uid")
                CommunicationManager.setSidUid(sid!!, uid!!)
                _firstRun.value = false
                _initialized.value = true
                false
            }
        } catch (e: Exception) {
            Log.e("MainViewModel", "Error in checkFirstRun: $e")
            false
        }
    }

    suspend fun createNewUser() {
        Log.d("MainViewModel", "Creating new user")
        try {
            sid = CommunicationManager.createUser().sid
            uid = CommunicationManager.createUser().uid
            dataStoreManager.setSidInDataStore(sid)
            dataStoreManager.setUidInDataStore(uid)
            Log.d("MainViewModel", "New user create, sid: $sid")
            _firstRun.value = false
        } catch (e: Exception) {
            Log.e("MainViewModel", "Error in createNewUser: $e")
        }

    }


    //HOME

    private val _selectedSection = MutableStateFlow(1)
    val selectedSection: StateFlow<Int> = _selectedSection

    fun switchScreen() {
        if (_selectedSection.value == 1) {
            _selectedSection.value = 2
        } else {
            _selectedSection.value = 1
        }
    }

    //HOMESCREEN

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
                val byteArray = Base64.decode(base64)
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

    private var _menuDetailed = MutableStateFlow(MenuDetailed(null, null, null, null,null, null, null, null))
    val menuDetailed: StateFlow<MenuDetailed> = _menuDetailed

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
            Log.e("MainViewModel", "Error in loadMenuList: $e")
        }
    }

    //PROFILE

    // EDIT PROFILE

    private val _isEditProfile = MutableStateFlow(false)
    val isEditProfile: StateFlow<Boolean> = _isEditProfile
    private val _firstNameForm = MutableStateFlow(_user.value.firstName)
    val firstNameForm: StateFlow<String> = _firstNameForm
    private val _lastNameForm = MutableStateFlow(_user.value.lastName)
    val lastNameForm: StateFlow<String> = _lastNameForm

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
        _user.value.firstName = _firstNameForm.value
        _user.value.lastName = _lastNameForm.value
        /* AGGIORNARE UTENTE SUL SERVER */
    }

    // EDIT BILLING

    //cardNumber, expireMonth, expireYear, CVV, CardFullName
    private val _isEditBilling = MutableStateFlow(false)
    val isEditBilling: StateFlow<Boolean> = _isEditBilling
    private val _cardNumberForm = MutableStateFlow(_user.value.cardNumber)
    val cardNumberForm: StateFlow<String> = _cardNumberForm
    private val _expireMonthForm = MutableStateFlow(_user.value.cardExpireMonth.toString())
    val expireMonthForm: StateFlow<String> = _expireMonthForm
    private val _expireYearForm = MutableStateFlow(_user.value.cardExpireYear.toString())
    val expireYearForm: StateFlow<String> = _expireYearForm
    private val _cvvForm = MutableStateFlow(_user.value.cardCVV)
    val cvvForm: StateFlow<String> = _cvvForm
    private val _cardFullNameForm = MutableStateFlow(_user.value.cardFullName)
    val cardFullNameForm: StateFlow<String> = _cardFullNameForm

    fun switchEditBillingMode() {
        _isEditBilling.value = !_isEditBilling.value
    }

    fun setCardNumberForm(value: String) {
        _cardNumberForm.value = value
    }

    fun setExpireMonthForm(value: String) {
        _expireMonthForm.value = value
    }

    fun setExpireYearForm(value: String) {
        _expireYearForm.value = value
    }

    fun setCVVForm(value: String) {
        _cvvForm.value = value
    }

    fun setCardFullNameForm(value: String) {
        _cardFullNameForm.value = value
    }

    fun updateUserCardData() {
        Log.d("MainViewModel", "Updating user card data with values: ${_user.value}")
        if (validateCardField("cardFullName", _cardFullNameForm.value)) {
            _user.value.cardFullName = _cardFullNameForm.value
        }
        if (validateCardField("cardNumber", _cardNumberForm.value)) {
            _user.value.cardNumber = _cardNumberForm.value
        }
        if (validateCardField("expireMonth", _expireMonthForm.value)) {
            _user.value.cardExpireMonth = _expireMonthForm.value.toInt()
        }

        if (validateCardField("expireYear", _expireYearForm.value)) {
            _user.value.cardExpireYear = _expireYearForm.value.toInt()
        }
        if (validateCardField("CVV", _cvvForm.value)) {
            _user.value.cardCVV = _cvvForm.value
        }
        Log.d("MainViewModel", "User card data updated with new values: ${_user.value}")
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



