package br.me.vitorcsouza.train.ui.presentation.weekly_plan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.me.vitorcsouza.train.ui.theme.DarkBlue
import br.me.vitorcsouza.train.ui.theme.Lime

@Composable
fun WeeklyDayCard(
    day: String,
    workoutName: String,
    isToday: Boolean
) {
    val backgroundColor = if (isToday) Lime else Color.White
    val contentColor = DarkBlue
    val dayLabel = if (isToday) "$day • TODAY" else day

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = if (isToday) Color.Transparent else Color.LightGray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = dayLabel,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.6f),
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = workoutName,
                style = MaterialTheme.typography.titleLarge,
                color = contentColor,
                fontWeight = FontWeight.Bold
            )
        }

        if (isToday) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(DarkBlue, CircleShape)
            )
        }
    }
}

@Preview(showBackground = true, name = "Workout - Not Today")
@Composable
private fun WeeklyDayCardPreview() {
    WeeklyDayCard(
        day = "MON",
        workoutName = "Chest & Triceps",
        isToday = false
    )
}

@Preview(showBackground = true, name = "Workout - Today")
@Composable
private fun WeeklyDayCardTodayPreview() {
    WeeklyDayCard(
        day = "WED",
        workoutName = "Leg Day",
        isToday = true
    )
}

@Preview(showBackground = true, name = "Rest Day")
@Composable
private fun WeeklyDayCardRestPreview() {
    WeeklyDayCard(
        day = "SUN",
        workoutName = "Rest Day",
        isToday = false
    )
}
