package screens.splash

import common.BaseViewModel
import kotlinx.coroutines.delay

class SplashViewModel: BaseViewModel<SplashState, SplashEvent>() {

    override fun startState() = SplashState

    init {
        launchIO {
            delay(3_000L)
            withIO {
                _uiEvents.emit(
                    SplashEvent.SplashNavigation.NavigateToConnect
                )
            }
        }
    }

    fun onAction(action: SplashAction) {

    }
}