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

fun Workout.toEntity(isSynced: Boolean = true, isPendingDelete: Boolean = false): WorkoutEntity {
    return WorkoutEntity(
        id = id,
        userId = userId,
        dayOfWeek = dayOfWeek,
        name = name,
        exercises = exercises,
        isSynced = isSynced,
        isPendingDelete = isPendingDelete
    )
}

fun List<WorkoutEntity>.toDomainList(): List<Workout> {
    return filter { !it.isPendingDelete }.map { it.toDomain() }
}

fun List<Workout>.toEntityList(isSynced: Boolean = true): List<WorkoutEntity> {
    return map { it.toEntity(isSynced) }
}
