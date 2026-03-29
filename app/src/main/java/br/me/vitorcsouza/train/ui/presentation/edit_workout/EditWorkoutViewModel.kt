package br.me.vitorcsouza.train.ui.presentation.edit_workout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.me.vitorcsouza.train.domain.model.Exercise
import br.me.vitorcsouza.train.domain.model.Workout
import br.me.vitorcsouza.train.domain.repository.WorkoutRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.Collections
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
            is EditWorkoutEvent.OnLoadWorkouts -> loadWorkouts(event.userId)

            is EditWorkoutEvent.OnWorkoutNameChanged -> state = state.copy(workoutName = event.name)

            is EditWorkoutEvent.OnDayChanged -> {
                val selectedDay = event.day.uppercase().trim()

                val existingWorkout = state.workouts.find {
                    it.dayOfWeek.uppercase().trim() == selectedDay
                }

                state = state.copy(
                    dayOfWeek = selectedDay,
                    id = existingWorkout?.id ?: "",
                    workoutName = existingWorkout?.name ?: "",
                    exercises = existingWorkout?.exercises ?: emptyList()
                )
            }

            is EditWorkoutEvent.OnAddExercise -> {
                val newList = state.exercises.toMutableList().apply {
                    add(Exercise(id = randomUUID().toString()))
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
                if (event.index in currentExercises.indices) {
                    val updatedExercise = currentExercises[event.index].copy(type = event.type)
                    currentExercises[event.index] = updatedExercise
                    state = state.copy(exercises = currentExercises)
                }
            }

            is EditWorkoutEvent.OnMoveExercise -> {
                val newList = state.exercises.toMutableList()
                if (event.from in newList.indices && event.to in newList.indices) {
                    Collections.swap(newList, event.from, event.to)
                    state = state.copy(exercises = newList)
                }
            }

            is EditWorkoutEvent.OnSaveWorkout -> saveWorkout()
            else -> {}
        }
    }

    private fun loadWorkouts(userId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            workoutRepository.getWorkouts(userId).onSuccess { workouts ->
                val currentDay = state.dayOfWeek.uppercase().trim()
                val currentDayWorkout = workouts.find {
                    it.dayOfWeek.uppercase().trim() == currentDay
                }

                state = state.copy(
                    workouts = workouts,
                    id = currentDayWorkout?.id ?: "",
                    workoutName = currentDayWorkout?.name ?: "",
                    exercises = currentDayWorkout?.exercises ?: emptyList(),
                    isLoading = false
                )
            }.onFailure {
                state = state.copy(isLoading = false, error = it.message)
            }
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

            if (result.isSuccess) {
                loadWorkouts(userId)
                state = state.copy(isSaved = true, isLoading = false)
            } else {
                state = state.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Falha ao salvar"
                )
            }
        }
    }
}
