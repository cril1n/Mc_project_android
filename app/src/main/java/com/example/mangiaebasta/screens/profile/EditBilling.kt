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
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.mangiaebasta.model.User
import com.example.mangiaebasta.components.TopBarWithBackArrow
import com.example.mangiaebasta.viewmodel.MainViewModel
@Composable
fun EditBilling(model: MainViewModel, user: User, navController: NavController) {
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
        TopBarWithBackArrow("Profile edit", navController)

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
                Text("CARD NUMBER:", softWrap = true, modifier = Modifier.padding(bottom = 10.dp))
                if (isEditBilling) {
                    TextField(
                        value = cardNumberForm,
                        onValueChange = { newValue ->
                            if (newValue.matches(Regex("^\\d{0,16}$"))) {
                                cardNumberForm = newValue
                                if (newValue.length == 16) {
                                    model.setCardNumberForm(newValue)
                                    isCardNumberValid = true
                                }
                                else isCardNumberValid = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = !isCardNumberValid
                    )

                    if (!isCardNumberValid) {
                        Text("Invalid card number", color = Color.Red)
                    }
                } else {
                    Text(user.cardNumber, modifier = Modifier.padding(bottom = 15.dp))
                }

                Text("EXPIRE MONTH:", softWrap = true, modifier = Modifier.padding(bottom = 10.dp))
                if (isEditBilling) {
                    TextField(
                        value = expireMonthForm,
                        onValueChange = { newValue ->
                            // Accetta solo numeri e controlla che sia un mese valido
                            if (newValue.matches(Regex("^\\d{0,2}$"))) {
                                val month = newValue.toIntOrNull() ?: 0
                                if (month in 0..12) {  // Permette anche 0 durante la digitazione
                                    expireMonthForm = newValue
                                    model.setExpireMonthForm(newValue)
                                    isExpireMonthValid = true
                                }else{
                                    isExpireMonthValid = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    if (!isExpireMonthValid) {
                        Text("Invalid Month", color = Color.Red)
                    }
                } else {
                    Text(user.cardExpireMonth.toString(), modifier = Modifier.padding(bottom = 15.dp))
                }

                Text("EXPIRE YEAR:", softWrap = true, modifier = Modifier.padding(bottom = 10.dp))
                if (isEditBilling) {
                    TextField(
                        value = expireYearForm,
                        onValueChange = { newValue ->
                            if (newValue.matches(Regex("^\\d{0,2}$"))) {
                                expireYearForm = newValue
                                if(newValue.length == 2) {
                                    model.setExpireYearForm(newValue)
                                    isExpireYearValid = true
                                }else isExpireYearValid = false

                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    if (!isExpireYearValid) {
                        Text("Invalid Year", color = Color.Red)
                    }
                } else {
                    Text(user.cardExpireYear.toString(), modifier = Modifier.padding(bottom = 15.dp))
                }

                Text("CVV:", softWrap = true, modifier = Modifier.padding(bottom = 10.dp))
                if (isEditBilling) {
                    TextField(
                        value = cvvForm,
                        onValueChange = { newValue ->
                            // Accetta solo numeri fino a 3 cifre
                            if (newValue.matches(Regex("^\\d{0,3}$"))) {
                                cvvForm = newValue
                                if(newValue.length == 3) {
                                    model.setCVVForm(newValue)
                                    isCVVValid = true
                                }else isCVVValid = false

                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    if (!isCVVValid) {
                        Text("Invalid CVV", color = Color.Red)
                    }
                } else {
                    Text(user.cardCVV, modifier = Modifier.padding(bottom = 15.dp))
                }

                Text("CARDFULLNAME:", softWrap = true, modifier = Modifier.padding(bottom = 10.dp))
                if (isEditBilling) {
                    TextField(
                        value = cardFullNameForm,
                        onValueChange = { newValue ->
                            // Accetta solo lettere e al massimo uno spazio
                            if (newValue.matches(Regex("^[a-zA-Z]*(\\s[a-zA-Z]*)?$"))) {
                                cardFullNameForm = newValue
                                if(newValue.matches(Regex("^[a-zA-Z]+ [a-zA-Z]+$"))){
                                    isCardFullNameValid = true
                                    model.setCardFullNameForm(newValue)
                                }else isCardFullNameValid = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    if (!isCardFullNameValid) {
                        Text("Invalid Card Full Name", color = Color.Red)
                    }
                } else {
                    Text(user.cardFullName, modifier = Modifier.padding(bottom = 15.dp))
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
                    }
                ) {
                    if (isEditBilling) {
                        Text("SAVE")
                    } else {
                        Text("EDIT")
                    }
                }
            }
        }
    }
}