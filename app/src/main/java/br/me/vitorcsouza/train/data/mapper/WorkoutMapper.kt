package br.me.vitorcsouza.train.data.mapper

import br.me.vitorcsouza.train.data.local.entity.WorkoutEntity
import br.me.vitorcsouza.train.domain.model.Workout

fun WorkoutEntity.toDomain(): Workout {
    return Workout(
        id = id,
        userId = userId,
        dayOfWeek = dayOfWeek,
        name = name,
        exercises = exercises
    )
}

fun Workout.toEntity(): WorkoutEntity {
    return WorkoutEntity(
        id = id,
        userId = userId,
        dayOfWeek = dayOfWeek,
        name = name,
        exercises = exercises
    )
}

fun List<WorkoutEntity>.toDomainList(): List<Workout> {
    return map { it.toDomain() }
}

fun List<Workout>.toEntityList(): List<WorkoutEntity> {
    return map { it.toEntity() }
}
