package screens.main.dashboard

import common.BaseViewModel

class DashboardViewModel: BaseViewModel<DashboardState, DashboardEvent>() {

    override fun startState() = DashboardState

    fun onAction(action: DashboardAction) {

    }
}