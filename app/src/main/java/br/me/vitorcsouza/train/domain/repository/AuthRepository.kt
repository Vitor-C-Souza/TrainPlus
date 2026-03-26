package br.me.vitorcsouza.train.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
}