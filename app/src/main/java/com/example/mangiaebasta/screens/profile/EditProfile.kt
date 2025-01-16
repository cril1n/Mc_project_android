package com.example.mangiaebasta.screens.profile

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.mangiaebasta.R
import com.example.mangiaebasta.components.ProfileField
import com.example.mangiaebasta.components.EditSaveButton
import com.example.mangiaebasta.components.ProfileHeader
import com.example.mangiaebasta.components.TopBarWithBackArrow
import com.example.mangiaebasta.model.GetUserResponse
import com.example.mangiaebasta.viewmodel.MainViewModel


@Composable
fun EditProfile(model: MainViewModel, navController: NavController) {
    val user = model.user.collectAsState().value
    val isEditProfile by model.isEditProfile.collectAsState()
    // Usa remember per mantenere lo stato locale dei campi
    var firstNameForm by remember { mutableStateOf(user.firstName) }
    var lastNameForm by remember { mutableStateOf(user.lastName) }

    Column {
        TopBarWithBackArrow("Profile edit","profileScreen", navController)

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeader(user, isEditProfile)

            Spacer(modifier = Modifier.height(40.dp))

            firstNameForm?.let {
                lastNameForm?.let { it1 ->
                    ProfileForm(
                        isEditProfile = isEditProfile,
                        firstNameForm = it,
                        lastNameForm = it1,
                        onFirstNameChange = {
                            if (it.matches(Regex("^[a-zA-Z]*$"))) {
                                firstNameForm = it
                                model.setFirstNameForm(it)
                            }
                        },
                        onLastNameChange = {
                            if (it.matches(Regex("^[a-zA-Z]*$"))) {
                                lastNameForm = it
                                model.setLastNameForm(it)
                            }
                        },
                        model = model
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                EditSaveButton(
                    isEditing = isEditProfile,
                    isEnabled = !isEditProfile || (firstNameForm?.isNotBlank() == true && lastNameForm?.isNotBlank() == true),
                    onClick = {
                        if (isEditProfile) {
                            firstNameForm?.let { model.setFirstNameForm(it) }
                            lastNameForm?.let { model.setLastNameForm(it) }
                            model.updateUserNameData()
                        }
                        model.switchEditMode()
                    }
                )
            }
        }
    }
}


@Composable
fun ProfileForm(
    isEditProfile: Boolean,
    firstNameForm: String,
    lastNameForm: String,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    model: MainViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ProfileField(
            label = "FIRST NAME",
            value = firstNameForm,
            isEditing = isEditProfile,
            onValueChange = onFirstNameChange,
            model = model
        )

        Spacer(modifier = Modifier.height(24.dp))

        ProfileField(
            label = "LAST NAME",
            value = lastNameForm,
            isEditing = isEditProfile,
            onValueChange = onLastNameChange,
            model = model
        )
    }
}



