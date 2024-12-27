package com.example.mangiaebasta.screens.profile

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mangiaebasta.components.TopBarWithBackArrow
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun EditProfile(model: MainViewModel, navController: NavController) {
    val user = model.user.collectAsState().value
    val isEditProfile by model.isEditProfile.collectAsState()

    // Usa remember per mantenere lo stato locale dei campi
    var firstNameForm by remember { mutableStateOf(user.firstName) }
    var lastNameForm by remember { mutableStateOf(user.lastName) }

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
                // Campo per il primo nome
                Text("FIRST NAME:", softWrap = true, modifier = Modifier.padding(bottom = 10.dp))
                if (isEditProfile) {
                    firstNameForm?.let {
                        TextField(
                            value = it,
                            onValueChange = { newValue ->
                                // Validazione: accetta solo lettere senza spazi
                                if (newValue.matches(Regex("^[a-zA-Z]*$"))) {
                                    firstNameForm = newValue
                                    model.setFirstNameForm(newValue)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true // Impedisce l'inserimento di nuove linee
                        )
                    }
                } else {
                    user.firstName?.let { Text(it, modifier = Modifier.padding(bottom = 15.dp)) }
                }

                // Campo per il cognome
                Text("LAST NAME:", softWrap = true, modifier = Modifier.padding(bottom = 10.dp))
                if (isEditProfile) {
                    lastNameForm?.let {
                        TextField(
                            value = it,
                            onValueChange = { newValue ->
                                // Validazione: accetta solo lettere senza spazi
                                if (newValue.matches(Regex("^[a-zA-Z]*$"))) {
                                    lastNameForm = newValue
                                    model.setLastNameForm(newValue)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true // Impedisce l'inserimento di nuove linee
                        )
                    }
                } else {
                    user.lastName?.let { Text(it, modifier = Modifier.padding(bottom = 15.dp)) }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 200.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier.width(300.dp),
                    onClick = {
                        if (isEditProfile) {
                            // Aggiorna i dati solo quando si preme Save
                            firstNameForm?.let { model.setFirstNameForm(it) }
                            lastNameForm?.let { model.setLastNameForm(it) }
                            model.updateUserNameData()
                        }
                        model.switchEditMode()
                        Log.d("FORM", "New firstname: $firstNameForm, new lastname: $lastNameForm")
                    },
                    // Disabilita il pulsante Save se i campi sono vuoti
                    enabled = !isEditProfile || (firstNameForm!!.isNotBlank() && lastNameForm!!.isNotBlank())
                ) {
                    if (isEditProfile) {
                        Text("SAVE")
                    } else {
                        Text("EDIT")
                    }
                }
            }
        }
    }
}

