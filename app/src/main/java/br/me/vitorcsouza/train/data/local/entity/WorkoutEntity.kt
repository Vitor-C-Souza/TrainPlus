package br.me.vitorcsouza.train.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.me.vitorcsouza.train.domain.model.Exercise

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val dayOfWeek: String,
    val name: String,
    val exercises: List<Exercise>,
    // Flag para controle de sincronização com o Firebase
    val isSynced: Boolean = true,
    // Flag para "soft delete" - permite deletar localmente e sincronizar o delete depois
    val isPendingDelete: Boolean = false
)
