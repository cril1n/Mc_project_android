package com.example.mangiaebasta.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun PrimaRegistrazione(model: MainViewModel, navController: NavHostController) {
    var firstNameForm by remember { mutableStateOf("") }
    var lastNameForm by remember { mutableStateOf("") }
    val isSubmitEnabled = firstNameForm.isNotBlank() && lastNameForm.isNotBlank()

    Column(
    modifier = Modifier
    .fillMaxSize()
    .padding(16.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            androidx.compose.material.Text(
                text = "First Registration",
                textAlign = TextAlign.Center
            )
            androidx.compose.material.Text(
                text = "Fill the following fields to get started",
                textAlign = TextAlign.Center
            )

            TextField(
                value = firstNameForm,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^[a-zA-Z]*$"))) {
                        firstNameForm = newValue
                    }
                },
                label = { androidx.compose.material.Text("First name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = lastNameForm,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^[a-zA-Z]*$"))) {
                        lastNameForm = newValue
                    }
                },
                label = { androidx.compose.material.Text("Last name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            androidx.compose.material.Button(
                onClick = {
                    model.setFirstNameForm(firstNameForm)
                    model.setLastNameForm(lastNameForm)
                    model.updateUserNameData()
                    navController.navigate("profile")
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isSubmitEnabled, // Abilita solo se entrambi i campi sono pieni
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA500))
            ) {
                androidx.compose.material.Text(text = "Submit", color = Color.White)
            }
        }
    }
}