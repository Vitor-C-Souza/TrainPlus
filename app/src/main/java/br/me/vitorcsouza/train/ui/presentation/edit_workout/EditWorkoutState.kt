package br.me.vitorcsouza.train.ui.presentation.edit_workout

import br.me.vitorcsouza.train.domain.model.Exercise
import br.me.vitorcsouza.train.domain.model.ExerciseType
import br.me.vitorcsouza.train.domain.model.Workout

data class EditWorkoutState(
    val id: String = "",
    val workoutName: String = "",
    val dayOfWeek: String = "MONDAY",
    val exercises: List<Exercise> = emptyList(),
    val workouts: List<Workout> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

sealed class EditWorkoutEvent {
    data class OnLoadWorkouts(val userId: String) : EditWorkoutEvent()
    data class OnDayChanged(val day: String) : EditWorkoutEvent()

    data class OnWorkoutNameChanged(val name: String) : EditWorkoutEvent()

    object OnAddExercise : EditWorkoutEvent()
    data class OnRemoveExercise(val exerciseId: String) : EditWorkoutEvent()
    data class OnExerciseNameChanged(val index: Int, val name: String) : EditWorkoutEvent()
    data class OnExerciseDetailsChanged(val index: Int, val sets: Int, val reps: Int) :
        EditWorkoutEvent()

    data class OnExerciseSetsChanged(val index: Int, val sets: String) : EditWorkoutEvent()

    data class OnExerciseRepsChanged(val index: Int, val reps: String) : EditWorkoutEvent()

    data class OnExerciseTypeChanged(val index: Int, val type: ExerciseType) : EditWorkoutEvent()

    data class OnMoveExercise(val from: Int, val to: Int) : EditWorkoutEvent()

    object OnSaveWorkout : EditWorkoutEvent()
}
