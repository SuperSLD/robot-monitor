package screens.main

import domain.models.MonitorState

data class MainState(
    val monitorState: MonitorState? = null,
    val selectedDestination: Destinations = Destinations.DASHBOARD,
)

sealed class MainAction {

    data class ChangeDestination(
        val destination: Destinations,
    ): MainAction()

    object Disconnect: MainAction()
}

sealed class MainEvent {

    sealed class Navigation: MainEvent() {

        object Disconnect: Navigation()
    }
}