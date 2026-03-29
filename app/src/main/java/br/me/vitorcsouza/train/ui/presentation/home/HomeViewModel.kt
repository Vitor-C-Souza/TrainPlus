package br.me.vitorcsouza.train.ui.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.me.vitorcsouza.train.domain.repository.WorkoutRepository
import br.me.vitorcsouza.train.domain.usecase.GetWorkoutsUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    private val getWorkoutsUseCase: GetWorkoutsUseCase,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    fun loadWorkouts(userId: String) {
        viewModelScope.launch {
            // 1. Inicia a observação do banco local (Offline-first)
            // Se já houver dados no Room, eles aparecem instantaneamente.
            launch {
                getWorkoutsUseCase.observe(userId).collect { workouts ->
                    val today = LocalDate.now().dayOfWeek.name
                    val todayWorkout =
                        workouts.find { it.dayOfWeek.uppercase().trim() == today.uppercase() }

                    state = state.copy(
                        workouts = workouts,
                        selectedWorkout = todayWorkout,
                        isLoading = false
                    )
                }
            }

            // 2. Tenta buscar novidades do Firebase em background
            // Se houver dados no Firebase (ex: novo celular), eles serão baixados e salvos no Room.
            // O 'collect' acima vai detectar a mudança e atualizar a tela automaticamente.
            try {
                getWorkoutsUseCase.refresh(userId)
            } catch (e: Exception) {
                // Falha silenciosa: se estiver sem internet, o usuário continua vendo os dados locais.
            }
        }
    }

    fun refreshWorkouts(userId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            getWorkoutsUseCase.refresh(userId)
            state = state.copy(isLoading = false)
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

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnToggleExercise -> {
                toggleExerciseStatus(event.workoutId, event.exerciseId, event.isCompleted)
            }
        }
    }
}

sealed class HomeEvent {
    data class OnToggleExercise(
        val workoutId: String,
        val exerciseId: String,
        val isCompleted: Boolean
    ) : HomeEvent()
}