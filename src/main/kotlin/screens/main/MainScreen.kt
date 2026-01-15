package screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import common.SingleEventEffect
import common.components.MCard
import kotlinx.coroutines.flow.SharedFlow
import navigation.ConnectDestination
import org.koin.compose.viewmodel.koinViewModel
import screens.main.commands.CommandsScreen
import screens.main.dashboard.DashboardScreen
import screens.main.gyroscope.GyroscopeScreen
import theme.Colors
import theme.MonitorText

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = koinViewModel()
) {
    MainScreenContent(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        events = viewModel.uiEvents,
        onAction = viewModel::onAction,
        onNavigation = {
            when(it) {
                MainEvent.Navigation.Disconnect -> navController.navigate(ConnectDestination)
            }
        }
    )
}

@Composable
fun MainScreenContent(
    state: MainState,
    events: SharedFlow<MainEvent>,
    onAction: (MainAction) -> Unit,
    onNavigation: (MainEvent.Navigation) -> Unit,
    isPreview: Boolean = false,
) {
    SingleEventEffect(events) { event ->
        when (event) {
            is MainEvent.Navigation -> onNavigation(event)
        }
    }

    Scaffold { paddingValues ->
        Row {
            Column(
                modifier = Modifier.width(400.dp).fillMaxHeight().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("ROBOT", style = MonitorText.Bold.Sp32.White.style())
                        Spacer(Modifier.height(24.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Connection:", style = MonitorText.Bold.Sp18.White.style())
                            if (state.monitorState?.connection?.isOpen == true) {
                                Text("Online", style = MonitorText.Regular.Sp18.Green.style())
                            } else {
                                Text("Ofline", style = MonitorText.Regular.Sp18.Red.style())
                            }
                        }
                        Text("${state.monitorState?.connection?.ip}:${state.monitorState?.connection?.port}", style = MonitorText.Regular.Sp16.Gray.style())
                    }
                    //Text(state.monitorState?.robotState?.time.toString(), color = theme.Colors.textPrimary)
                }

                Destinations.entries.forEach { destination ->
                    MCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onAction(MainAction.ChangeDestination(destination)) },
                        color = if (destination == state.selectedDestination) Colors.primary else Colors.backgroundSecondary,
                    ) {
                        Text(destination.label, style = MonitorText.Regular.Sp24.White.style())
                    }
                }

                MCard(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                ) {}

                MCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onAction(MainAction.Disconnect) },
                    color = Colors.errorBackground,
                ) {
                    Text("Disconnect", style = MonitorText.Regular.Sp24.White.style(), color = Colors.textError)
                }
            }
            Box(
                modifier =
                    Modifier.padding(0.dp, 16.dp, 16.dp, 16.dp).fillMaxSize()
            ) {
                when (state.selectedDestination) {
                    Destinations.DASHBOARD -> {
                        DashboardScreen(monitorState = state.monitorState)
                    }

                    Destinations.GYROSCOPE -> {
                        GyroscopeScreen(monitorState = state.monitorState)
                    }

                    Destinations.SERVO -> {
                        //LibraryTabNavHost()
                    }

                    Destinations.COMMANDS -> {
                        CommandsScreen(monitorState = state.monitorState)
                    }
                }
            }
        }
    }
}

enum class Destinations(
    val label: String,
) {
    DASHBOARD("Dashboard"),
    GYROSCOPE("Gyroscope"),
    SERVO("Servo"),
    COMMANDS("Commands"),
}