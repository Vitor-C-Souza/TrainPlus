package br.me.vitorcsouza.train.ui.presentation.weekly_plan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.me.vitorcsouza.train.domain.usecase.GetWorkoutsUseCase
import kotlinx.coroutines.launch

class WeeklyPlanViewModel(
    private val getWorkoutsUseCase: GetWorkoutsUseCase
) : ViewModel() {

    var state by mutableStateOf(WeeklyPlanState())
        private set

    fun loadWorkouts(userId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            getWorkoutsUseCase.refresh(userId).onSuccess {
                getWorkoutsUseCase.observe(userId).collect { workouts ->
                    state = state.copy(
                        workouts = workouts,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                state = state.copy(
                    errorMessage = error.message ?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }
}
