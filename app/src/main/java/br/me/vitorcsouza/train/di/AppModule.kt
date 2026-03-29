package br.me.vitorcsouza.train.di

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import br.me.vitorcsouza.train.data.local.database.AppDatabase
import br.me.vitorcsouza.train.data.repository.AuthRepositoryImpl
import br.me.vitorcsouza.train.data.repository.WorkoutRepositoryImpl
import br.me.vitorcsouza.train.domain.repository.AuthRepository
import br.me.vitorcsouza.train.domain.repository.WorkoutRepository
import br.me.vitorcsouza.train.domain.usecase.GetWorkoutsUseCase
import br.me.vitorcsouza.train.domain.usecase.LoginUseCase
import br.me.vitorcsouza.train.domain.usecase.RegisterUseCase
import br.me.vitorcsouza.train.ui.presentation.edit_workout.EditWorkoutViewModel
import br.me.vitorcsouza.train.ui.presentation.home.HomeViewModel
import br.me.vitorcsouza.train.ui.presentation.login.LoginViewModel
import br.me.vitorcsouza.train.ui.presentation.signup.SignUpViewModel
import br.me.vitorcsouza.train.ui.presentation.weekly_plan.WeeklyPlanViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.O)
val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    // Room Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "train_database"
        ).build()
    }

    // DAOs
    single { get<AppDatabase>().workoutDao() }

    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<WorkoutRepository> {
        WorkoutRepositoryImpl(
            get(),
            get()
        )
    }

    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { GetWorkoutsUseCase(get()) }

    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::EditWorkoutViewModel)
    viewModelOf(::WeeklyPlanViewModel)
}
