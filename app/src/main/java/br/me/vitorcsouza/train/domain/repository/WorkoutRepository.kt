package br.me.vitorcsouza.train.domain.repository

import br.me.vitorcsouza.train.domain.model.Exercise
import br.me.vitorcsouza.train.domain.model.Workout

interface WorkoutRepository {
    suspend fun getWorkouts(userId: String): Result<List<Workout>>
    suspend fun addWorkout(workout: Workout): Result<Unit>
    suspend fun deleteWorkout(workoutId: String): Result<Unit>
    suspend fun updateWorkout(workout: Workout): Result<Unit>

    suspend fun addExerciseToWorkout(workoutId: String, exercise: Exercise): Result<Unit>
    suspend fun deleteExerciseFromWorkout(workoutId: String, exerciseId: String): Result<Unit>
    suspend fun updateExerciseInWorkout(workoutId: String, exercise: Exercise): Result<Unit>

    suspend fun toggleExerciseStatus(workoutId: String, exerciseId: String, isCompleted: Boolean): Result<Unit>
}