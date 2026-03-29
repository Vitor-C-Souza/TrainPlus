package br.me.vitorcsouza.train.ui.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.me.vitorcsouza.train.domain.model.Workout
import br.me.vitorcsouza.train.domain.repository.WorkoutRepository
import br.me.vitorcsouza.train.domain.usecase.GetWorkoutsUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel @Inject constructor(
    private val getWorkoutsUseCase: GetWorkoutsUseCase,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    fun loadWorkouts(userId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = getWorkoutsUseCase(userId)

            result.onSuccess { workouts ->
                val today = LocalDate.now().dayOfWeek.name
                val todayWorkouts = workouts.find { it.dayOfWeek == today }

                state = state.copy(
                    workouts = workouts,
                    selectedWorkout = todayWorkouts,
                    isLoading = false
                )
            }.onFailure { error ->
                state = state.copy(
                    errorMessage = error.message ?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }

    fun toggleExerciseStatus(workoutId: String, exerciseId: String, isCompleted: Boolean) {
        viewModelScope.launch {
            val result = workoutRepository.toggleExerciseStatus(workoutId, exerciseId, isCompleted)
            if (result.isSuccess) {
                state.selectedWorkout?.let { workout ->
                    if (workout.id == workoutId) {
                        val updatedExercises = workout.exercises.map {
                            if (it.id == exerciseId) it.copy(isCompleted = isCompleted) else it
                        }
                        state = state.copy(
                            selectedWorkout = workout.copy(exercises = updatedExercises)
                        )
                    }
                }
            }
        }
    }

    suspend fun getWorkouts(userId: String): Result<List<Workout>> {
        return getWorkoutsUseCase(userId)
    }
}
