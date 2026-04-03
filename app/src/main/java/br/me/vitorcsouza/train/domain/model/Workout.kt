package br.me.vitorcsouza.train.domain.model

import com.google.firebase.firestore.PropertyName

data class Workout(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("userId")
    @set:PropertyName("userId")
    var userId: String = "",

    @get:PropertyName("dayOfWeek")
    @set:PropertyName("dayOfWeek")
    var dayOfWeek: String = "MONDAY",

    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String = "",

    @get:PropertyName("exercises")
    @set:PropertyName("exercises")
    var exercises: List<Exercise> = emptyList(),

    @get:PropertyName("lastUpdated")
    @set:PropertyName("lastUpdated")
    var lastUpdated: Long = System.currentTimeMillis()
)
