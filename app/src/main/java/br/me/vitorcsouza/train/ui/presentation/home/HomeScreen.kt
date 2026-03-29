package br.me.vitorcsouza.train.ui.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.me.vitorcsouza.train.ui.components.FloatButtonCustom
import br.me.vitorcsouza.train.ui.presentation.home.components.ExerciseCard
import br.me.vitorcsouza.train.ui.presentation.home.components.HomeHeader
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    userId: String,
    onClick: () -> Unit,
    onClickDate: () -> Unit = {},
    viewModel: HomeViewModel = koinViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(userId) {
        viewModel.loadWorkouts(userId)
    }

    HomeScreenContent(
        state = state,
        onClick = onClick,
        onClickDate = onClickDate,
        onToggleExerciseStatus = { exerciseId, isCompleted ->
            state.selectedWorkout?.id?.let { workoutId ->
                viewModel.toggleExerciseStatus(workoutId, exerciseId, isCompleted)
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenContent(
    state: HomeState,
    onClick: () -> Unit,
    onClickDate: () -> Unit = {},
    onToggleExerciseStatus: (String, Boolean) -> Unit = { _, _ -> }
) {
    Scaffold(
        floatingActionButton = {
            FloatButtonCustom(
                onClick = onClick,
                icon = Icons.Outlined.Add
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val selectedWorkout = state.selectedWorkout
            val dayOfWeek = selectedWorkout?.dayOfWeek ?: DayOfWeek.WEDNESDAY.name

            val completedExercises = selectedWorkout?.exercises?.count { it.isCompleted } ?: 0
            val totalExercises = selectedWorkout?.exercises?.size ?: 0
            val totalMinutes = totalExercises * 10

            HomeHeader(
                dayOfWeek = dayOfWeek,
                workoutName = selectedWorkout?.name ?: "No Workout Today",
                completedWorkouts = completedExercises,
                totalWorkouts = totalExercises,
                totalMinutes = totalMinutes,
                onClickDate = onClickDate
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                selectedWorkout?.exercises?.let { exercises ->
                    items(exercises) { exercise ->
                        ExerciseCard(
                            exerciseName = exercise.name,
                            series = exercise.sets,
                            repeat = exercise.reps,
                            isCheckedExercise = exercise.isCompleted,
                            exerciseType = exercise.type,
                            checkButton = {
                                onToggleExerciseStatus(exercise.id, !exercise.isCompleted)
                            }
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreenContent(
        state = HomeState(),
        onClick = {}
    )
}
