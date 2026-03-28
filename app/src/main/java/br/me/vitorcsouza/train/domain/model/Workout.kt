package br.me.vitorcsouza.train.domain.model

import java.time.DayOfWeek

data class Workout(
    val id: String = "",
    val userId: String,
    val dayOfWeek: DayOfWeek,
    val name: String,
    val exercises: List<Exercise> = emptyList()
)
