package br.me.vitorcsouza.train.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.me.vitorcsouza.train.data.local.entity.WorkoutEntity

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts WHERE userId = :userId")
    suspend fun getWorkoutsByUserId(userId: String): List<WorkoutEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkouts(workouts: List<WorkoutEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity)

    @Query("DELETE FROM workouts WHERE id = :workoutId")
    suspend fun deleteWorkout(workoutId: String)

    @Query("DELETE FROM workouts WHERE userId = :userId")
    suspend fun clearWorkoutsByUserId(userId: String)
}
