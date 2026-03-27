package br.me.vitorcsouza.train.domain.usecase

import br.me.vitorcsouza.train.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, email: String, password: String): Result<Unit> {
        return repository.register(username, email, password)
    }
}