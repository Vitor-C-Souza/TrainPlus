package br.me.vitorcsouza.train.ui.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.me.vitorcsouza.train.domain.model.Workout
import br.me.vitorcsouza.train.domain.usecase.GetWorkoutsUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel @Inject constructor(
    private val getWorkoutsUseCase: GetWorkoutsUseCase
) : ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    fun loadWorkouts(userId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = getWorkoutsUseCase(userId)

            result.onSuccess { workouts ->
                val today = LocalDate.now().dayOfWeek
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
    suspend fun getWorkouts(userId: String): Result<List<Workout>> {
        return getWorkoutsUseCase(userId)
    }
}