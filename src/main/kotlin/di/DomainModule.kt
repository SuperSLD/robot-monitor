package di

import domain.socket.IMonitorStateConnection
import domain.socket.MonitorStateConnection
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val provideDomainModule = module {
    singleOf(::MonitorStateConnection).bind(IMonitorStateConnection::class)
}