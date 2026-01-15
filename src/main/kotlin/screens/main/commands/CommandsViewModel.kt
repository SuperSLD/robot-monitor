package screens.main.commands

import androidx.compose.ui.graphics.Color
import common.BaseViewModel
import domain.socket.IMonitorStateConnection
import theme.Colors

class CommandsViewModel(
    val monitorStateConnection: IMonitorStateConnection,
): BaseViewModel<CommandsState, CommandsEvent>() {

    override fun startState() = CommandsState()

    private var stack: List<Pair<Color, String>> = emptyList()
    private var command: String = ""

    init {
        monitorStateConnection.onCommandResponse { commandResponse ->
            launchUI {
                println("response received $commandResponse")
                stack += commandResponse.color() to commandResponse.text
                updateState()
            }
        }
    }

    fun onAction(action: CommandsAction) {
        when(action) {
            is CommandsAction.OnCommandChange -> onCommandChange(action.command)
            CommandsAction.OnExecuteCommand -> onExecuteCommand()
        }
    }

    private fun onCommandChange(command: String) {
        launchUI {
            this.command = command
            updateState()
        }
    }

    private suspend fun updateState() {
        _uiState.emit(
            _uiState.value.copy(
                command = command,
                stack = stack,
            )
        )
    }

    private fun onExecuteCommand() {
        launchUI(onError = { error ->
            stack += Colors.textError to (error.message ?: "error")
            updateState()
        }) {
            if (command.isNotBlank()) {
                println("send command $command")
                stack += Colors.textSecondary to command
                val commandToSend = command
                command = ""
                updateState()
                withIO { monitorStateConnection.sendCommand(commandToSend) }
            }
        }
    }
}