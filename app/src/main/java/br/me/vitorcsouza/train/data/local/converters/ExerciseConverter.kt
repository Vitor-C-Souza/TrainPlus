package br.me.vitorcsouza.train.data.local.converters

import androidx.room.TypeConverter
import br.me.vitorcsouza.train.domain.model.Exercise
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExerciseConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromExerciseList(exercises: List<Exercise>): String {
        return gson.toJson(exercises)
    }

    @TypeConverter
    fun toExerciseList(exercisesString: String): List<Exercise> {
        val type = object : TypeToken<List<Exercise>>() {}.type
        return gson.fromJson(exercisesString, type)
    }
}
