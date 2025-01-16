package com.example.mangiaebasta.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults as ButtonDefaults
import androidx.compose.material3.ButtonDefaults as ButtonDefaults3
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController


@Composable
fun CustomAlertDialog(
    dismissFunction: (Boolean) -> Unit,
    confirmFunction: (() -> Unit)?,
    navController: NavHostController?,
    route: String?,
    title: String,
    message: String,
    confirmButton: String,
    dismissButton: String
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Dialog(
            onDismissRequest = { dismissFunction(false) },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFFF99501), RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { dismissFunction(false) },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.Black),
                        ) {
                            Text(dismissButton)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                confirmFunction?.invoke()
                                route?.let { navController?.navigate(it) }
                                dismissFunction(false)
                            },
                            colors = ButtonDefaults3.buttonColors(containerColor = Color(0xFFF99501))
                        ) {
                            Text(confirmButton, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}




