package br.me.vitorcsouza.train.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.me.vitorcsouza.train.data.local.dao.WorkoutDao
import br.me.vitorcsouza.train.data.mapper.toDomain
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Worker responsável por sincronizar as alterações locais com o Firebase em segundo plano.
 * 
 * O offline-first funciona assim:
 * 1. O app salva no Room (banco local) e marca como 'isSynced = false'.
 * 2. Este Worker é chamado pelo WorkManager quando houver internet.
 * 3. Ele pega tudo que não está sincronizado e envia para o Firebase.
 */
class WorkoutSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    // Injetamos as dependências via KoinComponent
    private val workoutDao: WorkoutDao by inject()
    private val firestore: FirebaseFirestore by inject()
    private val workoutCollection = firestore.collection("workouts")

    override suspend fun doWork(): Result {
        return try {
            // 1. Busca no Room treinos que foram criados/editados ou marcados para deletar
            val unsyncedWorkouts = workoutDao.getUnsyncedWorkouts()

            for (entity in unsyncedWorkouts) {
                if (entity.isPendingDelete) {
                    // Se o usuário deletou o treino enquanto estava offline:
                    // Deletamos do Firebase
                    workoutCollection.document(entity.id).delete().await()
                    // Se deu certo, deletamos definitivamente do Room
                    workoutDao.deleteWorkout(entity.id)
                } else {
                    // Se o usuário criou ou editou o treino:
                    val workout = entity.toDomain()
                    // Enviamos (ou sobrescrevemos) no Firebase
                    workoutCollection.document(workout.id).set(workout).await()
                    // Se deu certo, marcamos no Room que agora está sincronizado
                    workoutDao.markAsSynced(entity.id)
                }
            }

            Result.success()
        } catch (e: Exception) {
            // Se falhar (ex: internet caiu no meio do processo), 
            // o WorkManager vai tentar novamente mais tarde seguindo a política de backoff.
            Result.retry()
        }
    }
}
