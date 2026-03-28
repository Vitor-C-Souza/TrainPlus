package br.me.vitorcsouza.train.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.me.vitorcsouza.train.ui.theme.backgroundColor
import br.me.vitorcsouza.train.ui.theme.Lime
import br.me.vitorcsouza.train.ui.theme.iconColor

@Composable
fun TextFieldCustom(
    modifier: Modifier = Modifier,
    placeholder: String,
    icon: ImageVector?,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        leadingIcon = if (icon != null) {
            {
                Icon(
                    imageVector = icon,
                    tint = iconColor,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else null,
        placeholder = {
            Text(
                text = placeholder,
                color = iconColor,
                fontSize = 16.sp
            )
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
        ),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            ),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        textStyle = TextStyle(fontSize = 16.sp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,

            focusedBorderColor = Lime,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,

            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = iconColor
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun TextFieldCustomPreview() {
    TextFieldCustom(
        icon = Icons.Outlined.Email,
        value = "",
        onValueChange = {},
        placeholder = "Email"
    )
}