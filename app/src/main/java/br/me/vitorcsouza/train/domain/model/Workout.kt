package br.me.vitorcsouza.train.domain.model

data class Workout(
    val id: String = "",
    val userId: String,
    val dayOfWeek: String,
    val name: String,
    val exercises: List<Exercise> = emptyList()
)
