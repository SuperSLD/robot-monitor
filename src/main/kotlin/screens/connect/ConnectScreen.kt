package screens.connect

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import common.SingleEventEffect
import common.components.PrimaryButton
import common.components.TextInputLine
import kotlinx.coroutines.flow.SharedFlow
import navigation.MainDestination
import org.koin.compose.viewmodel.koinViewModel
import theme.Colors
import theme.MonitorText

@Composable
fun ConnectScreen(
    navController: NavController,
    viewModel: ConnectViewModel = koinViewModel()
) {
    ConnectScreenContent(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        events = viewModel.uiEvents,
        onAction = viewModel::onAction,
        onNavigation = { navigation ->
            when(navigation) {
                ConnectEvent.Navigation.Main -> {
                    navController.popBackStack()
                    navController.navigate(MainDestination)
                }
            }
        }
    )
}

@Composable
fun ConnectScreenContent(
    state: ConnectState,
    events: SharedFlow<ConnectEvent>,
    onAction: (ConnectAction) -> Unit,
    onNavigation: (ConnectEvent.Navigation) -> Unit,
) {

    SingleEventEffect(events) { event ->
        when(event) {
            is ConnectEvent.Navigation -> onNavigation(event)
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .width(500.dp)
                    .height(700.dp)
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding(),
                    )
            ) {
                Row(
                    modifier = Modifier
                        .weight(1F),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Column {
                            Text(
                                text = "/monitor",
                                style = MonitorText.Bold.Sp64.White.style(),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = "Robot state monitoring",
                                style = MonitorText.Regular.Sp20.White.style(),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Connect to robot",
                            style = MonitorText.Bold.Sp24.White.style(),
                        )
                        TextInputLine(
                            label = "IP Address",
                            text = DEFAULT_IP,
                            keyboardType = KeyboardType.Text,
                        ) {
                            onAction(ConnectAction.IpChanged(it))
                        }
                        TextInputLine(
                            label = "Port",
                            text = DEFAULT_PORT,
                            keyboardType = KeyboardType.Decimal,
                        ) {
                            onAction(ConnectAction.PortChanged(it))
                        }
                        AnimatedVisibility(state.connectError != null) {
                            Box(
                                Modifier.background(Colors.errorBackground, RoundedCornerShape(16.dp))
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    text = state.connectError ?: "Ошибка",
                                    style = MonitorText.Regular.Sp16.Red.style(),
                                    maxLines = 10,
                                )
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        PrimaryButton(
                            text = "Connect",
                            enabled = state.buttonEnabled,
                            loaderVisible = state.progress,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            onAction(ConnectAction.ConnectButtonClicked)
                        }
                    }
                }
            }
        }
    }
}