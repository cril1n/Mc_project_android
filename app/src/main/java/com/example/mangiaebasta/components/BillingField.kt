package com.example.mangiaebasta.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mangiaebasta.viewmodel.MainViewModel

@Composable
fun BillingField(
    label: String,
    value: String,
    isEditing: Boolean,
    isValid: Boolean,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit,
    errorMessage: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    model: MainViewModel
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        if (isEditing) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = {
                    androidx.compose.material3.Text(
                        text = label,
                        style = MaterialTheme.typography.caption,
                        color = Color.Black,
                    )
                },
                isError = !isValid,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = leadingIcon,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFF99501),
                    unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                )

            )
            if (!isValid) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.caption,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .background(Color(0xFFf5f5f5))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .clickable {
                            model.switchEditBillingMode()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    leadingIcon?.invoke()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = value.ifEmpty { "Not set" },
                        style = MaterialTheme.typography.body1,
                        color = if (value.isNotEmpty()) Color.Black
                        else Color(0xFFf5f5f5),
                    )
                }
            }
        }
    }
}
