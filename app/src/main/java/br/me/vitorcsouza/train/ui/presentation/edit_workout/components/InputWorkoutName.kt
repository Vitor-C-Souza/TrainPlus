package br.me.vitorcsouza.train.ui.presentation.edit_workout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.me.vitorcsouza.train.ui.components.TextFieldCustom
import br.me.vitorcsouza.train.ui.theme.DarkBlue

@Composable
fun InputWorkoutName(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {}
    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Workout Name",
            style = MaterialTheme.typography.titleMedium,
            color = DarkBlue
        )

        TextFieldCustom(
            value = value,
            onValueChange = onValueChange,
            placeholder = "Type your workout name here...",
            icon = null,
            isPassword = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InputWorkoutNamePreview() {
    InputWorkoutName()
}