package br.me.vitorcsouza.train.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.me.vitorcsouza.train.ui.theme.DarkBlue

@Composable
fun TextButtomCustom(
    onClick: () -> Unit = {},
    text: String,
    modifier: Modifier = Modifier
) {

    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = DarkBlue,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextButtomCustomPreview() {
    TextButtomCustom(
        onClick = {},
        text = "Forgot Password?"
    )
}