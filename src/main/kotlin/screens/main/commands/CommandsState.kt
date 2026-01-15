package screens.main.commands

import androidx.compose.ui.graphics.Color

data class CommandsState(
    val command: String = "",
    val stack: List<Pair<Color, String>> = emptyList(),
)

sealed class CommandsAction {

    data object OnExecuteCommand: CommandsAction()

    data class OnCommandChange(
        val command: String,
    ): CommandsAction()
}

sealed class CommandsEvent {

    sealed class Navigation: CommandsEvent() {

    }
}