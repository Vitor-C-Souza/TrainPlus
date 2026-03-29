package br.me.vitorcsouza.train.domain.usecase

import br.me.vitorcsouza.train.domain.model.Workout
import br.me.vitorcsouza.train.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWorkoutsUseCase @Inject constructor(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(userId: String): Result<List<Workout>> {
        return repository.getWorkouts(userId)
    }

    fun observe(userId: String): Flow<List<Workout>> {
        return repository.getWorkoutsFlow(userId)
    }

    // 2. Para forçar uma atualização do servidor (Sincronização)
    suspend fun refresh(userId: String): Result<Unit> {
        return repository.fetchWorkoutsFromRemote(userId)
    }
}
