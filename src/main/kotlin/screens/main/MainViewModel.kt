package screens.main

import androidx.lifecycle.viewModelScope
import common.BaseViewModel
import domain.models.RobotState
import domain.socket.IMonitorStateConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    val monitorStateConnection: IMonitorStateConnection,
): BaseViewModel<MainState, MainEvent>() {

    override fun startState() = MainState()

    init {
        startCollecting()
    }

    fun startCollecting() {
        viewModelScope.launch(Dispatchers.IO) {
            monitorStateConnection
                .uiState
                .collect { data ->
                    withContext(Dispatchers.Main) {
                        _uiState.emit(_uiState.value.copy(monitorState = data))
                    }
                }
        }
    }

    fun onAction(action: MainAction) {
        when(action) {
            is MainAction.ChangeDestination -> changeDestination(action.destination)
            MainAction.Disconnect -> disconnect()
        }
    }

    private fun changeDestination(destination: Destinations) {
        launchUI {
            _uiState.emit(_uiState.value.copy(selectedDestination = destination))
        }
    }

    private fun disconnect() {
        launchUI {
            monitorStateConnection.disconnect()
            _uiEvents.emit(MainEvent.Navigation.Disconnect)
        }
    }
}