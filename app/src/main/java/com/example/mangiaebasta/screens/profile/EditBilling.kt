package com.example.mangiaebasta.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mangiaebasta.R
import com.example.mangiaebasta.components.BillingField
import com.example.mangiaebasta.components.EditSaveButton
import com.example.mangiaebasta.components.ProfileHeader
import com.example.mangiaebasta.components.TopBarWithBackArrow
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun EditBilling(model: MainViewModel, navController: NavController) {
    val user by model.user.collectAsState()
    val isEditBilling by model.isEditBilling.collectAsState()

    var cardNumberForm by remember { mutableStateOf(user.cardNumber ?: "") }
    var expireMonthForm by remember { mutableStateOf(user.cardExpireMonth?.toString() ?: "") }
    var expireYearForm by remember { mutableStateOf(user.cardExpireYear?.toString() ?: "") }
    var cvvForm by remember { mutableStateOf(user.cardCVV ?: "") }
    var cardFullNameForm by remember { mutableStateOf(user.cardFullName ?: "") }

    var isCardNumberValid by remember { mutableStateOf(true) }
    var isExpireMonthValid by remember { mutableStateOf(true) }
    var isExpireYearValid by remember { mutableStateOf(true) }
    var isCVVValid by remember { mutableStateOf(true) }
    var isCardFullNameValid by remember { mutableStateOf(true) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarWithBackArrow("Billing edit", "profileScreen", navController)

            Spacer(modifier = Modifier.height(25.dp))

            ProfileHeader(user, isEditBilling)

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                Modifier
                    .verticalScroll(scrollState)
                    .padding(25.dp)
            ) {
                BillingForm(
                    isEditBilling = isEditBilling,
                    cardNumberForm = cardNumberForm,
                    expireMonthForm = expireMonthForm,
                    expireYearForm = expireYearForm,
                    cvvForm = cvvForm,
                    cardFullNameForm = cardFullNameForm,
                    isCardNumberValid = isCardNumberValid,
                    isExpireMonthValid = isExpireMonthValid,
                    isExpireYearValid = isExpireYearValid,
                    isCVVValid = isCVVValid,
                    isCardFullNameValid = isCardFullNameValid,
                    onCardNumberChange = {
                        cardNumberForm = it
                        isCardNumberValid = it.matches(Regex("^\\d{16}$"))
                        if (isCardNumberValid) model.setCardNumberForm(it)
                    },
                    onExpireMonthChange = {
                        expireMonthForm = it
                        isExpireMonthValid = it.toIntOrNull() in 1..12
                        if (isExpireMonthValid) model.setExpireMonthForm(it.toInt())
                    },
                    onExpireYearChange = {
                        expireYearForm = it
                        isExpireYearValid = it.toIntOrNull() in 24..99
                        if (isExpireYearValid) model.setExpireYearForm(it.toInt())
                    },
                    onCVVChange = {
                        cvvForm = it
                        isCVVValid = it.matches(Regex("^\\d{3}$"))
                        if (isCVVValid) model.setCVVForm(it)
                    },
                    onCardFullNameChange = {
                        cardFullNameForm = it
                        isCardFullNameValid = it.matches(Regex("^[a-zA-Z]+(\\s[a-zA-Z]+)+$"))
                        if (isCardFullNameValid) model.setCardFullNameForm(it)
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp) // Spaziatura dal bordo inferiore
        ) {
            EditSaveButton(
                isEditing = isEditBilling,
                isEnabled = isCardNumberValid && isExpireMonthValid && isExpireYearValid && isCVVValid && isCardFullNameValid,
                onClick = {
                    if (isEditBilling) {
                        model.updateUserCardData()
                    }
                    model.switchEditBillingMode()
                }
            )
        }
    }
}

@Composable
fun BillingForm(
    isEditBilling: Boolean,
    cardNumberForm: String,
    expireMonthForm: String,
    expireYearForm: String,
    cvvForm: String,
    cardFullNameForm: String,
    isCardNumberValid: Boolean,
    isExpireMonthValid: Boolean,
    isExpireYearValid: Boolean,
    isCVVValid: Boolean,
    isCardFullNameValid: Boolean,
    onCardNumberChange: (String) -> Unit,
    onExpireMonthChange: (String) -> Unit,
    onExpireYearChange: (String) -> Unit,
    onCVVChange: (String) -> Unit,
    onCardFullNameChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        BillingField(label = "CARD NUMBER",
            value = cardNumberForm,
            isEditing = isEditBilling,
            isValid = isCardNumberValid,
            keyboardType = KeyboardType.Number,
            onValueChange = onCardNumberChange,
            errorMessage = "Invalid card number (must be 16 digits)",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.card),
                    modifier = Modifier.size(21.dp),
                    contentDescription = null
                )
            })

        Row(modifier = Modifier.fillMaxWidth()) {
            BillingField(label = "EXPIRE MONTH",
                value = expireMonthForm,
                isEditing = isEditBilling,
                isValid = isExpireMonthValid,
                keyboardType = KeyboardType.Number,
                onValueChange = onExpireMonthChange,
                errorMessage = "Invalid month",
                modifier = Modifier.weight(1f),
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) })
            Spacer(modifier = Modifier.width(16.dp))
            BillingField(label = "EXPIRE YEAR",
                value = expireYearForm,
                isEditing = isEditBilling,
                isValid = isExpireYearValid,
                keyboardType = KeyboardType.Number,
                onValueChange = onExpireYearChange,
                errorMessage = "Invalid year",
                modifier = Modifier.weight(1f),
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) })
        }

        BillingField(label = "CVV",
            value = cvvForm,
            isEditing = isEditBilling,
            isValid = isCVVValid,
            keyboardType = KeyboardType.Number,
            onValueChange = onCVVChange,
            errorMessage = "Invalid CVV",
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) })

        BillingField(label = "CARDHOLDER NAME",
            value = cardFullNameForm,
            isEditing = isEditBilling,
            isValid = isCardFullNameValid,
            keyboardType = KeyboardType.Text,
            onValueChange = onCardFullNameChange,
            errorMessage = "Invalid name (First and Last name required)",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) })
    }
}






