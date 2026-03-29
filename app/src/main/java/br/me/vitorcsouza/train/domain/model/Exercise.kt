package br.me.vitorcsouza.train.domain.model

import com.google.firebase.firestore.PropertyName

data class Exercise(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",
    
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String = "",
    
    @get:PropertyName("sets")
    @set:PropertyName("sets")
    var sets: Int = 0,
    
    @get:PropertyName("reps")
    @set:PropertyName("reps")
    var reps: Int = 0,
    
    @get:PropertyName("completed")
    @set:PropertyName("completed")
    var isCompleted: Boolean = false,
    
    @get:PropertyName("type")
    @set:PropertyName("type")
    var type: ExerciseType = ExerciseType.Normal
)

enum class ExerciseType {
    Normal,
    Progression,
    @PropertyName("Drop_Set")
    Drop_Set
}
