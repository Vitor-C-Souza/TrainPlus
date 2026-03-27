package br.me.vitorcsouza.train.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.me.vitorcsouza.train.ui.theme.DarkBlue

@Composable
fun ButtonDefaultCustom(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String,
) {

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(44.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = DarkBlue,
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonDefaultPreview() {
    ButtonDefaultCustom(
        text = "Sign in",
        onClick = {}
    )
}