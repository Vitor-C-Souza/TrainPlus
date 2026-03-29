package br.me.vitorcsouza.train.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.me.vitorcsouza.train.data.local.converters.ExerciseConverter
import br.me.vitorcsouza.train.data.local.dao.WorkoutDao
import br.me.vitorcsouza.train.data.local.entity.WorkoutEntity

@Database(entities = [WorkoutEntity::class], version = 1, exportSchema = false)
@TypeConverters(ExerciseConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}
