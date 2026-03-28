package br.me.vitorcsouza.train.ui.presentation.home

import br.me.vitorcsouza.train.domain.model.Workout

data class HomeState(
    val workouts: List<Workout> = emptyList(),
    val selectedWorkout: Workout? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
