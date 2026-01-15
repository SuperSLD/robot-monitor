package di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import screens.connect.ConnectViewModel
import screens.main.MainViewModel
import screens.main.commands.CommandsViewModel
import screens.main.dashboard.DashboardViewModel
import screens.main.gyroscope.GyroscopeViewModel
import screens.splash.SplashViewModel

val provideViewModelModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::ConnectViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::GyroscopeViewModel)
    viewModelOf(::CommandsViewModel)
}