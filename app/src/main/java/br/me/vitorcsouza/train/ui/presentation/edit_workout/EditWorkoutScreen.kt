package br.me.vitorcsouza.train.ui.presentation.edit_workout

import android.content.ClipData
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.me.vitorcsouza.train.ui.presentation.edit_workout.components.DayOfWeekSelector
import br.me.vitorcsouza.train.ui.presentation.edit_workout.components.EditHeader
import br.me.vitorcsouza.train.ui.presentation.edit_workout.components.ExerciseEditCard
import br.me.vitorcsouza.train.ui.presentation.edit_workout.components.InputWorkoutName
import br.me.vitorcsouza.train.ui.theme.DarkBlue
import br.me.vitorcsouza.train.ui.theme.TrainTheme
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditWorkoutScreen(
    viewModel: EditWorkoutViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    EditWorkoutContent(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        onSave = onSave
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditWorkoutContent(
    state: EditWorkoutState,
    onEvent: (EditWorkoutEvent) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    Scaffold(
        topBar = {
            EditHeader(
                onBack = onBack,
                onSave = onSave
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            item {
                InputWorkoutName(
                    value = state.workoutName,
                    onValueChange = { onEvent(EditWorkoutEvent.OnWorkoutNameChanged(it)) }
                )

                DayOfWeekSelector(
                    selectedDay = state.dayOfWeek.name,
                    onDaySelected = { onEvent(EditWorkoutEvent.OnDayChanged(it)) }
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Exercises",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )

                    TextButton(onClick = { onEvent(EditWorkoutEvent.OnAddExercise) }) {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Add Exercise",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }


            itemsIndexed(
                items = state.exercises,
                key = { _, exercise -> exercise.id }
            ) { index, exercise ->
                ExerciseEditCard(
                    exercise = exercise,
                    onNameChange = {
                        onEvent(
                            EditWorkoutEvent.OnExerciseNameChanged(
                                index,
                                it
                            )
                        )
                    },
                    onSetsChange = {
                        onEvent(
                            EditWorkoutEvent.OnExerciseSetsChanged(
                                index,
                                it
                            )
                        )
                    },
                    onRepsChange = {
                        onEvent(
                            EditWorkoutEvent.OnExerciseRepsChanged(
                                index,
                                it
                            )
                        )
                    },
                    onTypeChange = { newType ->
                        onEvent(EditWorkoutEvent.OnExerciseTypeChanged(index, newType))
                    },
                    onRemove = { onEvent(EditWorkoutEvent.OnRemoveExercise(index.toString())) }
                )
            }


            item { Spacer(modifier = Modifier.height(32.dp)) }

        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun EditWorkoutScreenPreview() {
    TrainTheme {
        EditWorkoutContent(
            state = EditWorkoutState(workoutName = "Leg Day"),
            onEvent = {},
            onBack = {},
            onSave = {}
        )
    }
}
