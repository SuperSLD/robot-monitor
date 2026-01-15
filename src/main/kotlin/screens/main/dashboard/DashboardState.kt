package screens.main.dashboard

data object DashboardState

data object DashboardAction

sealed class DashboardEvent {

    sealed class Navigation: DashboardEvent() {

    }
}