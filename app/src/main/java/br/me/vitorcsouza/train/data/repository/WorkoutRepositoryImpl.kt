package br.me.vitorcsouza.train.data.repository

import br.me.vitorcsouza.train.domain.model.Exercise
import br.me.vitorcsouza.train.domain.model.Workout
import br.me.vitorcsouza.train.domain.repository.WorkoutRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : WorkoutRepository {

    private val workoutCollection = firebaseFirestore.collection("workouts")


    override suspend fun getWorkouts(userId: String): Result<List<Workout>> {
        return try {
            val snapshot = workoutCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
            val workouts = snapshot.toObjects(Workout::class.java)
            Result.success(workouts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addWorkout(workout: Workout): Result<Unit> {
        return try {
            val docRef =
                if (workout.id.isEmpty()) workoutCollection.document() else workoutCollection.document(
                    workout.id
                )
            val workoutWithId = workout.copy(id = docRef.id)

            docRef.set(workoutWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteWorkout(workoutId: String): Result<Unit> {
        return try {
            workoutCollection.document(workoutId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateWorkout(workout: Workout): Result<Unit> {
        return try {
            workoutCollection.document(workout.id).set(workout).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addExerciseToWorkout(
        workoutId: String,
        exercise: Exercise
    ): Result<Unit> {
        return try {
            val docRef = workoutCollection.document(workoutId)
            val workout = docRef.get().await().toObject(Workout::class.java)

            workout?.let {
                val updatedExercises = it.exercises.toMutableList().apply { add(exercise) }
                docRef.update("exercises", updatedExercises).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun deleteExerciseFromWorkout(
        workoutId: String,
        exerciseId: String
    ): Result<Unit> {
        return try {
            val docRef = workoutCollection.document(workoutId)
            val workout = docRef.get().await().toObject(Workout::class.java)

            workout?.let {
                val updatedExercises = it.exercises.filter { ex -> ex.id != exerciseId }
                docRef.update("exercises", updatedExercises).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateExerciseInWorkout(
        workoutId: String,
        exercise: Exercise
    ): Result<Unit> {
        return try {
            val docRef = workoutCollection.document(workoutId)
            val workout = docRef.get().await().toObject(Workout::class.java)

            workout?.let {
                val updatedExercises = it.exercises.map { ex ->
                    if (ex.id == exercise.id) exercise else ex
                }
                docRef.update("exercises", updatedExercises).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleExerciseStatus(
        workoutId: String,
        exerciseId: String,
        isCompleted: Boolean
    ): Result<Unit> {
        return try {
            val docRef = workoutCollection.document(workoutId)
            val workout = docRef.get().await().toObject(Workout::class.java)

            workout?.let {
                val updatedExercises = it.exercises.map { ex ->
                    if (ex.id == exerciseId) ex.copy(isCompleted = isCompleted) else ex
                }
                docRef.update("exercises", updatedExercises).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

