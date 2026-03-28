package br.me.vitorcsouza.train.domain.model

data class Exercise(
    val id: String = "",
    val name: String,
    val sets: Int,
    val reps: Int,
    val isCompleted: Boolean = false,
    val type: ExerciseType = ExerciseType.Normal
)

enum class ExerciseType {
    Normal,
    Progression,
    Drop_Set
}
