package br.me.vitorcsouza.train.ui.presentation.edit_workout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.me.vitorcsouza.train.ui.theme.DarkBlue

@Composable
fun DayOfWeekSelector(
    selectedDay: String = "Mon",
    onDaySelected: (String) -> Unit = {}
    ) {

    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Day of Week",
            style = MaterialTheme.typography.titleMedium,
            color = DarkBlue,
            fontWeight = FontWeight.Bold
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 4
        ) {
            days.forEach { day ->
                DayOfWeekCard(
                    dayOfWeek = day,
                    isSelected = day == selectedDay,
                    onClick = { onDaySelected(day) }
                )
            }
        }
    }
}

@Composable
fun DayOfWeekCard(
    modifier: Modifier = Modifier,
    dayOfWeek: String = "Mon",
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
    ) {
    val backgroundColor = if (isSelected) DarkBlue else Color(0xFFF3F3F5)
    val textColor = if (isSelected) Color.White else DarkBlue


    Text(
        text = dayOfWeek,
        style = MaterialTheme.typography.bodyMedium,
        color = textColor,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        modifier = modifier
            .then(
                if (isSelected) Modifier.shadow(4.dp, RoundedCornerShape(12.dp))
                else Modifier
            )
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick() } // Torna clicável
            .padding(horizontal = 20.dp, vertical = 10.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun DayOfWeekPreview() {
    DayOfWeekSelector(selectedDay = "Wed")
}

@Preview(showBackground = true)
@Composable
private fun DayOfWeekCardPreview() {
    DayOfWeekCard(dayOfWeek = "Mon")
}