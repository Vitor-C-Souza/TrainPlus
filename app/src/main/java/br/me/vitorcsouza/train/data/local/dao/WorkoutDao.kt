package br.me.vitorcsouza.train.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.me.vitorcsouza.train.data.local.entity.WorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts WHERE userId = :userId AND isPendingDelete = 0")
    suspend fun getWorkoutsByUserId(userId: String): List<WorkoutEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkouts(workouts: List<WorkoutEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity)

    // Marca como pendente de deleção em vez de deletar imediatamente (para o SyncWorker saber o que deletar no Firebase)
    @Query("UPDATE workouts SET isPendingDelete = 1, isSynced = 0 WHERE id = :workoutId")
    suspend fun markAsDeleted(workoutId: String)

    @Query("DELETE FROM workouts WHERE id = :workoutId")
    suspend fun deleteWorkout(workoutId: String)

    @Query("DELETE FROM workouts WHERE userId = :userId")
    suspend fun clearWorkoutsByUserId(userId: String)

    @Query("SELECT * FROM workouts WHERE userId = :userId AND isPendingDelete = 0")
    fun getWorkoutsFlow(userId: String): Flow<List<WorkoutEntity>>

    // Queries para o SyncWorker
    @Query("SELECT * FROM workouts WHERE isSynced = 0")
    suspend fun getUnsyncedWorkouts(): List<WorkoutEntity>

    @Query("UPDATE workouts SET isSynced = 1 WHERE id = :workoutId")
    suspend fun markAsSynced(workoutId: String)
}
