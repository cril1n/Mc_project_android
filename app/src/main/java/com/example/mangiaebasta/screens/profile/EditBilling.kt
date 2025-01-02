package com.example.mangiaebasta.screens.profile

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mangiaebasta.components.TopBarWithBackArrow
import com.example.mangiaebasta.viewmodel.MainViewModel
import androidx.compose.material.*

@Composable
fun EditBilling(model: MainViewModel, navController: NavController) {
    val user = model.user.collectAsState().value
    val isEditBilling by model.isEditBilling.collectAsState()

    var cardNumberForm by remember { mutableStateOf(user.cardNumber) }
    var expireMonthForm by remember { mutableStateOf(user.cardExpireMonth.toString()) }
    var expireYearForm by remember { mutableStateOf(user.cardExpireYear.toString()) }
    var cvvForm by remember { mutableStateOf(user.cardCVV) }
    var cardFullNameForm by remember { mutableStateOf(user.cardFullName) }

    var isCardNumberValid by remember { mutableStateOf(true) }
    var isExpireMonthValid by remember { mutableStateOf(true) }
    var isExpireYearValid by remember { mutableStateOf(true) }
    var isCVVValid by remember { mutableStateOf(true) }
    var isCardFullNameValid by remember { mutableStateOf(true) }


    Column {
        TopBarWithBackArrow("Billing edit", "profileScreen",  navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${user.firstName} ${user.lastName}",
                modifier = Modifier.padding(top = 50.dp),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 40.dp)
                    .fillMaxWidth()
            ) {
                Text("Card Number:", softWrap = true, modifier = Modifier.padding(bottom = 10.dp))
                if (isEditBilling) {
                    TextField(
                        value = cardNumberForm ?: "",
                        onValueChange = { newValue ->
                            cardNumberForm = newValue
                            // Controlla che il valore sia numerico, lungo al massimo 16 caratteri, e inizi con "1"
                            if (newValue.matches(Regex("^1\\d{0,15}$"))) {
                                // Se è lungo 16 caratteri, lo consideriamo valido
                                if (newValue.length == 16) {
                                    model.setCardNumberForm(newValue)
                                    isCardNumberValid = true
                                } else {
                                    isCardNumberValid = false
                                }
                            } else {
                                isCardNumberValid = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = !isCardNumberValid
                    )
                    if (!isCardNumberValid) {
                        Text("Invalid card number (must be 16 digits)", color = Color.Red)
                    }
                } else {
                    user.cardNumber?.let { Text(it, modifier = Modifier.padding(bottom = 15.dp)) }
                }


                Text(
                    "Expire Month:",
                    softWrap = true,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                if (isEditBilling) {
                    TextField(
                        value = if (expireMonthForm == "null") "" else expireMonthForm,
                        onValueChange = { newValue ->
                            // Verifica se l'input è un numero con al massimo 2 cifre
                            isExpireMonthValid =
                                if (newValue.length in 1..2 && newValue.all { it.isDigit() }) {
                                    when (newValue.toInt()) {
                                        in 0..9 -> {
                                            expireMonthForm = newValue
                                            model.setExpireMonthForm("0$expireMonthForm".toInt())
                                            true
                                        }
                                        in 10..12 -> {
                                            expireMonthForm = newValue // Month is already valid (10, 11, or 12)
                                            model.setExpireMonthForm(expireMonthForm.toInt())
                                            true
                                        }
                                        else -> {
                                            expireMonthForm = ""
                                            false // Invalid month (not between 01 and 12)
                                        }
                                    }
                                } else {
                                    expireMonthForm = ""
                                    false
                                }

                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        isError = !isExpireMonthValid,
                        modifier = Modifier.fillMaxWidth()
                    )


                } else {
                    user.cardExpireMonth?.let {
                        Text(
                            text = "$it",
                            modifier = Modifier.padding(bottom = 15.dp)
                        )
                    }
                }
                Text(
                    "Expire Year:",
                    softWrap = true,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                if (isEditBilling) {
                    TextField(
                        value = if (expireYearForm == "null") "" else expireYearForm,
                        onValueChange = { newValue ->
                            // Verifica se l'input è un numero con al massimo 2 cifre
                            isExpireYearValid =
                                if (newValue.length in 1..2 && newValue.all { it.isDigit() }) {
                                    if (newValue.toInt() in 24..99) {
                                        expireYearForm = newValue
                                        model.setExpireYearForm(expireYearForm.toInt())
                                        true
                                    } else if (newValue.toInt() < 24) {
                                        expireYearForm = newValue
                                        false
                                    } else {
                                        expireYearForm = ""
                                        false
                                    }
                                } else {
                                    expireYearForm = ""
                                    false
                                }

                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        isError = !isExpireYearValid,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (!isExpireYearValid) {
                        Text("Invalid Year", color = Color.Red)
                    }
                } else {
                    user.cardExpireYear?.let {
                        Text(
                            text = "$it",
                            modifier = Modifier.padding(bottom = 15.dp)
                        )
                    }
                }

                Text("CVV:", softWrap = true, modifier = Modifier.padding(bottom = 10.dp))
                if (isEditBilling) {

                    TextField(
                        value = if (cvvForm == null) "" else cvvForm!!,
                        onValueChange = { newValue ->
                            // Accetta solo numeri fino a 3 cifre
                            if (newValue.matches(Regex("^\\d{0,3}$"))) {
                                cvvForm = newValue
                                if (newValue.length == 3) {
                                    model.setCVVForm(newValue)
                                    isCVVValid = true
                                } else isCVVValid = false

                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    if (!isCVVValid) {
                        Text("Invalid CVV", color = Color.Red)
                    }
                } else {
                    user.cardCVV?.let { Text(it, modifier = Modifier.padding(bottom = 15.dp)) }
                }

                Text(
                    "Card Full Name:",
                    softWrap = true,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                if (isEditBilling) {
                    TextField(
                        value = cardFullNameForm ?: "",
                        onValueChange = { newValue ->
                            // Accetta stringhe con lettere e un singolo spazio tra le parole
                            if (newValue.matches(Regex("^[a-zA-Z]*(\\s[a-zA-Z]*)*$"))) {
                                cardFullNameForm = newValue
                                isCardFullNameValid = newValue.trim()
                                    .contains(" ") // Valido se contiene almeno uno spazio
                                model.setCardFullNameForm(newValue)
                            } else {
                                isCardFullNameValid = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    if (!isCardFullNameValid) {
                        Text("Invalid Card Full Name", color = Color.Red)
                    }
                } else {
                    user.cardFullName?.let {
                        Text(
                            it,
                            modifier = Modifier.padding(bottom = 15.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier.width(300.dp),
                    onClick = {
                        if (isEditBilling) {
                            model.updateUserCardData()
                        }
                        model.switchEditBillingMode()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF9800)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isEditBilling) {
                        Text(
                            "SAVE",
                            color = Color.White,
                            style = MaterialTheme.typography.button.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    } else {
                        Text(
                            "EDIT",
                            color = Color.White,
                            style = MaterialTheme.typography.button.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}


fun formatToTwoDigits(value: String?): String {
    return value?.let {
        if (it.length == 1) "0$it" else it
    } ?: ""
}

