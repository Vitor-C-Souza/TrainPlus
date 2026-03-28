package br.me.vitorcsouza.train.ui.presentation.edit_workout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.key.type
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.me.vitorcsouza.train.domain.model.Exercise
import br.me.vitorcsouza.train.domain.model.Workout
import br.me.vitorcsouza.train.domain.repository.WorkoutRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.util.UUID.randomUUID

@RequiresApi(Build.VERSION_CODES.O)
class EditWorkoutViewModel(
    private val workoutRepository: WorkoutRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    var state by mutableStateOf(EditWorkoutState())
        private set

    fun onEvent(event: EditWorkoutEvent) {
        when (event) {
            is EditWorkoutEvent.OnWorkoutNameChanged -> state = state.copy(workoutName = event.name)

            is EditWorkoutEvent.OnDayChanged -> {
                val day = try {
                    DayOfWeek.valueOf(event.day.uppercase())
                } catch (e: Exception) {
                    state.dayOfWeek
                }
                state = state.copy(dayOfWeek = day)
            }

            is EditWorkoutEvent.OnAddExercise -> {
                val newList = state.exercises.toMutableList().apply {
                    add(
                        Exercise(
                            id = randomUUID().toString(),
                            name = "",
                            sets = 0,
                            reps = 0
                        )
                    )
                }
                state = state.copy(exercises = newList)
            }

            is EditWorkoutEvent.OnRemoveExercise -> {
                val newList = state.exercises.filter { it.id != event.exerciseId }
                state = state.copy(exercises = newList)
            }

            is EditWorkoutEvent.OnExerciseNameChanged -> {
                val newList = state.exercises.toMutableList()
                if (event.index in newList.indices) {
                    newList[event.index] = newList[event.index].copy(name = event.name)
                    state = state.copy(exercises = newList)
                }
            }

            is EditWorkoutEvent.OnExerciseDetailsChanged -> {
                val newList = state.exercises.toMutableList()
                if (event.index in newList.indices) {
                    newList[event.index] = newList[event.index].copy(
                        sets = event.sets,
                        reps = event.reps
                    )
                    state = state.copy(exercises = newList)
                }
            }

            is EditWorkoutEvent.OnExerciseSetsChanged -> {
                val newList = state.exercises.toMutableList()
                if (event.index in newList.indices) {
                    val sets = event.sets.toIntOrNull() ?: 0
                    newList[event.index] = newList[event.index].copy(sets = sets)
                    state = state.copy(exercises = newList)
                }
            }

            is EditWorkoutEvent.OnExerciseRepsChanged -> {
                val newList = state.exercises.toMutableList()
                if (event.index in newList.indices) {
                    val reps = event.reps.toIntOrNull() ?: 0
                    newList[event.index] = newList[event.index].copy(reps = reps)
                    state = state.copy(exercises = newList)
                }
            }


            is EditWorkoutEvent.OnExerciseTypeChanged -> {
                val currentExercises = state.exercises.toMutableList()

                val updatedExercise = currentExercises[event.index].copy(type = event.type)

                currentExercises[event.index] = updatedExercise

                state = state.copy(exercises = currentExercises)
            }

            is EditWorkoutEvent.OnSaveWorkout -> saveWorkout()
        }
    }

    private fun saveWorkout() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val workout = Workout(
                id = state.id.ifEmpty { randomUUID().toString() },
                userId = userId,
                dayOfWeek = state.dayOfWeek,
                name = state.workoutName,
                exercises = state.exercises
            )

            val result = if (state.id.isEmpty()) {
                workoutRepository.addWorkout(workout)
            } else {
                workoutRepository.updateWorkout(workout)
            }

            state = if (result.isSuccess) {
                state.copy(isSaved = true, isLoading = false)
            } else {
                state.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to save"
                )
            }
        }
    }
}
