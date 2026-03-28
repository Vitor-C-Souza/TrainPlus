package br.me.vitorcsouza.train.di

import android.os.Build
import androidx.annotation.RequiresApi
import br.me.vitorcsouza.train.data.repository.AuthRepositoryImpl
import br.me.vitorcsouza.train.data.repository.WorkoutRepositoryImpl
import br.me.vitorcsouza.train.domain.repository.AuthRepository
import br.me.vitorcsouza.train.domain.repository.WorkoutRepository
import br.me.vitorcsouza.train.domain.usecase.GetWorkoutsUseCase
import br.me.vitorcsouza.train.domain.usecase.LoginUseCase
import br.me.vitorcsouza.train.domain.usecase.RegisterUseCase
import br.me.vitorcsouza.train.ui.presentation.home.HomeViewModel
import br.me.vitorcsouza.train.ui.presentation.login.LoginViewModel
import br.me.vitorcsouza.train.ui.presentation.signup.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.O)
val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<WorkoutRepository> { WorkoutRepositoryImpl(get()) }

    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { GetWorkoutsUseCase(get()) }


    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}
