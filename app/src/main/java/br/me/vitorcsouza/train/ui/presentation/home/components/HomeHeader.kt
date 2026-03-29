package br.me.vitorcsouza.train.ui.presentation.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.me.vitorcsouza.train.ui.theme.DarkBlue
import br.me.vitorcsouza.train.ui.theme.Lime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    dayOfWeek: String = "WEDNESDAY",
    workoutName: String = "Leg Day",
    completedWorkouts: Int = 2,
    totalWorkouts: Int = 5,
    totalMinutes: Int = 45,
    onClickDate: () -> Unit = {}
) {
    val headerShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 24.dp,
        bottomEnd = 24.dp
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, headerShape)
            .background(DarkBlue, headerShape)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = dayOfWeek,
                    color = Lime,
                    style = MaterialTheme.typography.labelSmall
                )

                Text(
                    text = workoutName,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            IconButton(
                onClick = onClickDate,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.3f))
            ) {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = "Select Date",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "$completedWorkouts/$totalWorkouts completed • $totalMinutes min",
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun HomeHeaderPreview() {
    HomeHeader(
        dayOfWeek = "WEDNESDAY",
        workoutName = "Leg Day",
        completedWorkouts = 2,
        totalMinutes = 45,
        onClickDate = {}
    )
}