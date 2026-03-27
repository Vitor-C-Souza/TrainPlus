package br.me.vitorcsouza.train.di

import br.me.vitorcsouza.train.data.repository.AuthRepositoryImpl
import br.me.vitorcsouza.train.domain.repository.AuthRepository
import br.me.vitorcsouza.train.domain.usecase.LoginUseCase
import br.me.vitorcsouza.train.domain.usecase.RegisterUseCase
import br.me.vitorcsouza.train.ui.presentation.login.LoginViewModel
import br.me.vitorcsouza.train.ui.presentation.signup.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }


    single<AuthRepository> { AuthRepositoryImpl(get()) }


    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }


    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
}
