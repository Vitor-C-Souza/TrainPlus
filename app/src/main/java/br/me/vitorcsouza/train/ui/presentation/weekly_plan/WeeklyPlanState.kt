package br.me.vitorcsouza.train.ui.presentation.weekly_plan

import br.me.vitorcsouza.train.domain.model.Workout

data class WeeklyPlanState(
    val workouts: List<Workout> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
