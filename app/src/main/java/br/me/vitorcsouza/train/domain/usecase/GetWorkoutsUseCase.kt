package br.me.vitorcsouza.train.domain.usecase

import br.me.vitorcsouza.train.domain.model.Workout
import br.me.vitorcsouza.train.domain.repository.WorkoutRepository
import javax.inject.Inject

class GetWorkoutsUseCase @Inject constructor(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(userId: String): Result<List<Workout>> {
        return repository.getWorkouts(userId)
    }
}
