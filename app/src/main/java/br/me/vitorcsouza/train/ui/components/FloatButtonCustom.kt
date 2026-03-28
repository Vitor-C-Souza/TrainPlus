package br.me.vitorcsouza.train.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.me.vitorcsouza.train.ui.theme.DarkBlue
import br.me.vitorcsouza.train.ui.theme.Lime

@Composable
fun FloatButtonCustom(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    icon: ImageVector = Icons.Default.Add
    ) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = Lime,
        contentColor = DarkBlue,
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 12.dp
        ),
        modifier = modifier.size(64.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Add",
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FloatButtonCustomPreview() {
    FloatButtonCustom(
        onClick = {},
        icon = Icons.Default.Add
    )
}