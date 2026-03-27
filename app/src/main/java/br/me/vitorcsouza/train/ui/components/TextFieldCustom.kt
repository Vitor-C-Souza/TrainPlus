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
import br.me.vitorcsouza.train.ui.theme.focusRingColor
import br.me.vitorcsouza.train.ui.theme.iconColor

@Composable
fun TextFieldCustom(
    modifier: Modifier = Modifier,
    placeholder: String,
    icon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = icon,
                tint = iconColor,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        },
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            ),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        textStyle = TextStyle(fontSize = 16.sp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,

            focusedBorderColor = focusRingColor,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,

            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = focusRingColor
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