package screens.main.commands

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import common.SingleEventEffect
import common.components.TextInputLine
import domain.models.MonitorState
import kotlinx.coroutines.flow.SharedFlow
import org.koin.compose.viewmodel.koinViewModel
import screens.connect.ConnectAction
import screens.connect.DEFAULT_IP
import screens.main.dashboard.DashboardAction
import screens.main.dashboard.DashboardEvent
import screens.main.dashboard.DashboardState
import screens.main.dashboard.DashboardViewModel
import screens.main.gyroscope.GyroscopeSummary
import theme.Colors

@Composable
fun CommandsScreen(
    monitorState: MonitorState?,
    viewModel: CommandsViewModel = koinViewModel()
) {
    CommandsScreenContent(
        monitorState = monitorState,
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        events = viewModel.uiEvents,
        onAction = viewModel::onAction,
        onNavigation = {
            when(it) {
                else -> {}
            }
        }
    )
}

@Composable
fun CommandsScreenContent(
    monitorState: MonitorState?,
    state: CommandsState,
    events: SharedFlow<CommandsEvent>,
    onAction: (CommandsAction) -> Unit,
    onNavigation: (CommandsEvent.Navigation) -> Unit,
    isPreview: Boolean = false,
) {

    val listState = rememberLazyListState()
    val consolasFamily = FontFamily(
        androidx.compose.ui.text.platform.Font("Consolas", FontWeight.Normal)
    )

    SingleEventEffect(events) { event ->
        when (event) {
            is CommandsEvent.Navigation -> onNavigation(event)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Colors.textPrimary,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                items((state.stack + (monitorState?.logs ?: emptyList()))) { item ->
                    Text(
                        text = item.second,
                        color = item.first,
                        fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth(),
                        fontFamily = consolasFamily,
                    )
                }

            }

            TextInputLine(
                placeholder = "Write command here",
                text = state.command,
                keyboardType = KeyboardType.Text,
                onDoneAction = { onAction(CommandsAction.OnExecuteCommand) },
                clearAfterEnter = true
            ) {
                onAction(CommandsAction.OnCommandChange(it))
            }
        }
    }

    LaunchedEffect((state.stack + (monitorState?.logs ?: emptyList())).size) {
        if ((state.stack + (monitorState?.logs ?: emptyList())).isNotEmpty()) {
            listState.animateScrollToItem((state.stack + (monitorState?.logs ?: emptyList())).lastIndex)
        }
    }
}