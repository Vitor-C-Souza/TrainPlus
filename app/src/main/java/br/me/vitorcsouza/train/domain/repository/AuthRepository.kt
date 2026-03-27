package br.me.vitorcsouza.train.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>

    suspend fun register(username: String, email: String, password: String): Result<Unit>
}