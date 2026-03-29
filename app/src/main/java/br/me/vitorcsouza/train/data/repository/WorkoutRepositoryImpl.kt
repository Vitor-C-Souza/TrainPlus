package br.me.vitorcsouza.train.data.repository

import br.me.vitorcsouza.train.data.local.dao.WorkoutDao
import br.me.vitorcsouza.train.data.mapper.toDomainList
import br.me.vitorcsouza.train.data.mapper.toEntity
import br.me.vitorcsouza.train.data.mapper.toEntityList
import br.me.vitorcsouza.train.domain.model.Exercise
import br.me.vitorcsouza.train.domain.model.Workout
import br.me.vitorcsouza.train.domain.repository.WorkoutRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val workoutDao: WorkoutDao
) : WorkoutRepository {

    private val workoutCollection = firebaseFirestore.collection("workouts")

    override suspend fun getWorkouts(userId: String): Result<List<Workout>> {
        return try {
            // Tenta buscar do Firestore primeiro
            val snapshot = workoutCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
            val workouts = snapshot.toObjects(Workout::class.java)

            // Salva no Room para uso offline
            workoutDao.clearWorkoutsByUserId(userId)
            workoutDao.insertWorkouts(workouts.toEntityList())

            Result.success(workouts)
        } catch (e: Exception) {
            // Se falhar (ex: sem internet), busca do Room
            val localWorkouts = workoutDao.getWorkoutsByUserId(userId)
            if (localWorkouts.isNotEmpty()) {
                Result.success(localWorkouts.toDomainList())
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun addWorkout(workout: Workout): Result<Unit> {
        return try {
            val docRef =
                if (workout.id.isEmpty()) workoutCollection.document() else workoutCollection.document(
                    workout.id
                )
            val workoutWithId = workout.copy(id = docRef.id)

            // Salva no Firestore
            docRef.set(workoutWithId).await()

            // Salva no Room
            workoutDao.insertWorkout(workoutWithId.toEntity())

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteWorkout(workoutId: String): Result<Unit> {
        return try {
            // Deleta do Firestore
            workoutCollection.document(workoutId).delete().await()

            // Deleta do Room
            workoutDao.deleteWorkout(workoutId)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateWorkout(workout: Workout): Result<Unit> {
        return try {
            // Atualiza Firestore
            workoutCollection.document(workout.id).set(workout).await()

            // Atualiza Room
            workoutDao.insertWorkout(workout.toEntity())

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addExerciseToWorkout(workoutId: String, exercise: Exercise): Result<Unit> {
        return try {
            val docRef = workoutCollection.document(workoutId)
            val snapshot = docRef.get().await()
            val workout = snapshot.toObject(Workout::class.java)

            workout?.let {
                val updatedExercises = it.exercises.toMutableList().apply { add(exercise) }
                val updatedWorkout = it.copy(exercises = updatedExercises)

                docRef.update("exercises", updatedExercises).await()
                workoutDao.insertWorkout(updatedWorkout.toEntity())
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
                val updatedWorkout = it.copy(exercises = updatedExercises)

                docRef.update("exercises", updatedExercises).await()
                workoutDao.insertWorkout(updatedWorkout.toEntity())
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
                val updatedExercises =
                    it.exercises.map { ex -> if (ex.id == exercise.id) exercise else ex }
                val updatedWorkout = it.copy(exercises = updatedExercises)

                docRef.update("exercises", updatedExercises).await()
                workoutDao.insertWorkout(updatedWorkout.toEntity())
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
                val updatedExercises =
                    it.exercises.map { ex -> if (ex.id == exerciseId) ex.copy(isCompleted = isCompleted) else ex }
                val updatedWorkout = it.copy(exercises = updatedExercises)

                docRef.update("exercises", updatedExercises).await()
                workoutDao.insertWorkout(updatedWorkout.toEntity())
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
