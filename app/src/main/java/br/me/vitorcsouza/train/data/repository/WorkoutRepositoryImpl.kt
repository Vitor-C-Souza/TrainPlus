package br.me.vitorcsouza.train.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import br.me.vitorcsouza.train.data.local.dao.WorkoutDao
import br.me.vitorcsouza.train.data.mapper.toDomain
import br.me.vitorcsouza.train.data.mapper.toDomainList
import br.me.vitorcsouza.train.data.mapper.toEntity
import br.me.vitorcsouza.train.data.mapper.toEntityList
import br.me.vitorcsouza.train.data.sync.WorkoutSyncWorker
import br.me.vitorcsouza.train.domain.model.Exercise
import br.me.vitorcsouza.train.domain.model.Workout
import br.me.vitorcsouza.train.domain.repository.WorkoutRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

/**
 * Implementação Offline-First do Repositório.
 */
class WorkoutRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val workoutDao: WorkoutDao,
    private val context: Context
) : WorkoutRepository {

    private val workoutCollection = firebaseFirestore.collection("workouts")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getWorkoutsFlow(userId: String): Flow<List<Workout>> {
        return workoutDao.getWorkoutsFlow(userId).map { entities ->
            entities.map { entity ->
                val workout = entity.toDomain()

                if (!isSameDay(workout.lastUpdated)) {
                    resetWorkoutExercises(workout)
                } else {
                    workout
                }
            }
        }
    }

    private suspend fun resetWorkoutExercises(workout: Workout): Workout {
        val resetExercises = workout.exercises.map { it.copy(isCompleted = false) }
        val updatedWorkout = workout.copy(
            exercises = resetExercises,
            lastUpdated = System.currentTimeMillis()
        )

        workoutDao.insertWorkout(updatedWorkout.toEntity())

        return updatedWorkout
    }

    override suspend fun fetchWorkoutsFromRemote(userId: String): Result<Unit> {
        return try {
            val snapshot = workoutCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()

            // Mapeia os documentos para objetos Workout, garantindo que o ID do documento seja usado
            val workouts = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Workout::class.java)?.copy(id = doc.id)
            }

            // Atualiza o cache local sem apagar o que ainda não foi sincronizado
            // O OnConflictStrategy.REPLACE no DAO cuidará de atualizar os existentes
            workoutDao.insertWorkouts(workouts.toEntityList(isSynced = true))

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorkouts(userId: String): Result<List<Workout>> {
        return try {
            val localWorkouts = workoutDao.getWorkoutsByUserId(userId).toDomainList()
            Result.success(localWorkouts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addWorkout(workout: Workout): Result<Unit> {
        return try {
            val id = workout.id.ifEmpty { UUID.randomUUID().toString() }
            val workoutWithId = workout.copy(id = id)

            workoutDao.insertWorkout(workoutWithId.toEntity(isSynced = false))
            scheduleSync()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateWorkout(workout: Workout): Result<Unit> {
        return try {
            workoutDao.insertWorkout(workout.toEntity(isSynced = false))
            scheduleSync()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteWorkout(workoutId: String): Result<Unit> {
        return try {
            workoutDao.markAsDeleted(workoutId)
            scheduleSync()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addExerciseToWorkout(workoutId: String, exercise: Exercise): Result<Unit> {
        return try {
            val workout =
                workoutDao.getWorkoutsFlow("").first().find { it.id == workoutId }?.toDomain()

            workout?.let {
                val updatedExercises = it.exercises.toMutableList().apply { add(exercise) }
                val updatedWorkout = it.copy(exercises = updatedExercises)

                workoutDao.insertWorkout(updatedWorkout.toEntity(isSynced = false))
                scheduleSync()
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
            val workout =
                workoutDao.getWorkoutsFlow("").first().find { it.id == workoutId }?.toDomain()
            workout?.let {
                val updatedExercises = it.exercises.filter { it.id != exerciseId }
                val updatedWorkout = it.copy(exercises = updatedExercises)
                workoutDao.insertWorkout(updatedWorkout.toEntity(isSynced = false))
                scheduleSync()
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
            val workout =
                workoutDao.getWorkoutsFlow("").first().find { it.id == workoutId }?.toDomain()
            workout?.let {
                val updatedExercises =
                    it.exercises.map { if (it.id == exercise.id) exercise else it }
                val updatedWorkout = it.copy(exercises = updatedExercises)
                workoutDao.insertWorkout(updatedWorkout.toEntity(isSynced = false))
                scheduleSync()
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
            val workout =
                workoutDao.getWorkoutsFlow("").first().find { it.id == workoutId }?.toDomain()
            workout?.let {
                val updatedExercises =
                    it.exercises.map { if (it.id == exerciseId) it.copy(isCompleted = isCompleted) else it }
                val updatedWorkout = it.copy(exercises = updatedExercises)
                workoutDao.insertWorkout(updatedWorkout.toEntity(isSynced = false))
                scheduleSync()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun scheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<WorkoutSyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                java.util.concurrent.TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "workout_sync",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isSameDay(timestamp: Long): Boolean {
        val lastUpdateDate = java.time.Instant.ofEpochMilli(timestamp)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate()
        val today = java.time.LocalDate.now()
        return lastUpdateDate.isEqual(today)
    }
}
