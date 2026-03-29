package br.me.vitorcsouza.train.ui.presentation.weekly_plan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.me.vitorcsouza.train.domain.model.Workout
import br.me.vitorcsouza.train.ui.presentation.weekly_plan.components.WeeklyDayCard
import br.me.vitorcsouza.train.ui.presentation.weekly_plan.components.WeeklyPlanHeader
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyPlanScreen(
    userId: String,
    onBack: () -> Unit,
    viewModel: WeeklyPlanViewModel = koinViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(userId) {
        viewModel.loadWorkouts(userId)
    }

    WeeklyPlanContent(
        workouts = state.workouts,
        onBack = onBack
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyPlanContent(
    workouts: List<Workout>,
    onBack: () -> Unit
) {
    val daysOfWeek = listOf(
        "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"
    )
    val today = LocalDate.now().dayOfWeek.name

    Scaffold(
        topBar = {
            WeeklyPlanHeader(onBack = onBack)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(daysOfWeek) { day ->
                val workout = workouts.find { it.dayOfWeek.uppercase() == day }
                val isToday = day == today

                WeeklyDayCard(
                    day = day.take(3),
                    workoutName = workout?.name ?: "Rest Day",
                    isToday = isToday
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun WeeklyPlanPreview() {
    WeeklyPlanContent(
        workouts = listOf(
            Workout(id = "1", dayOfWeek = "MONDAY", name = "Chest & Triceps", userId = ""),
            Workout(id = "2", dayOfWeek = "TUESDAY", name = "Back & Biceps", userId = ""),
            Workout(id = "3", dayOfWeek = "WEDNESDAY", name = "Leg Day", userId = "")
        ),
        onBack = {}
    )
}
