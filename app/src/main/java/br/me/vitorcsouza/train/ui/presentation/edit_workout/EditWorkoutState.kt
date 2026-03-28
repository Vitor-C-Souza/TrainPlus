package br.me.vitorcsouza.train.ui.presentation.edit_workout

import android.os.Build
import androidx.annotation.RequiresApi
import br.me.vitorcsouza.train.domain.model.Exercise
import br.me.vitorcsouza.train.domain.model.ExerciseType
import java.time.DayOfWeek

@RequiresApi(Build.VERSION_CODES.O)
data class EditWorkoutState(
    val id: String = "",
    val workoutName: String = "",
    val dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val exercises: List<Exercise> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

sealed class EditWorkoutEvent {
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

    object OnSaveWorkout : EditWorkoutEvent()
}
