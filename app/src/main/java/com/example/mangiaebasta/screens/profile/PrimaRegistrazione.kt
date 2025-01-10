package com.example.mangiaebasta.screens.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            Text(
                text = "First Registration",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Fill the following fields to get started",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 4.dp
                )
            )

            TextField(
                value = firstNameForm,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^[a-zA-Z]*$"))) {
                        firstNameForm = newValue
                    }
                },
                label = { Text("First name") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        color = if (firstNameForm.isNotEmpty()) Color(0xFFF99501) else Color.Gray,
                        RoundedCornerShape(8.dp)
                    ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            TextField(
                value = lastNameForm,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^[a-zA-Z]*$"))) {
                        lastNameForm = newValue
                    }
                },
                label = { Text("Last name") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        color = if (lastNameForm.isNotEmpty()) Color(0xFFF99501) else Color.Gray,
                        RoundedCornerShape(8.dp)
                    ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Button(
                onClick = {
                    model.setFirstNameForm(firstNameForm)
                    model.setLastNameForm(lastNameForm)
                    model.updateUserNameData()
                    navController.navigate("profileScreen")
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isSubmitEnabled,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF99501))
            ) {
                Text(text = "Submit", color = Color.White)
            }
        }
    }
}