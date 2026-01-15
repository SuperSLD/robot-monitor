package screens.main.gyroscope

import common.BaseViewModel
import screens.main.dashboard.DashboardAction
import screens.main.dashboard.DashboardEvent
import screens.main.dashboard.DashboardState

class GyroscopeViewModel: BaseViewModel<GyroscopeState, GyroscopeEvent>() {

    override fun startState() = GyroscopeState

    fun onAction(action: GyroscopeAction) {

    }
}