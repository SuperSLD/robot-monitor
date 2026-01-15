package screens.main.gyroscope

data object GyroscopeState

data object GyroscopeAction

sealed class GyroscopeEvent {

    sealed class Navigation: GyroscopeEvent() {

    }
}