package screens.main.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import common.SingleEventEffect
import domain.models.MonitorState
import kotlinx.coroutines.flow.SharedFlow
import org.koin.compose.viewmodel.koinViewModel
import screens.main.gyroscope.GyroscopeSummary
import screens.main.servo.ServoSummary

@Composable
fun DashboardScreen(
    monitorState: MonitorState?,
    viewModel: DashboardViewModel = koinViewModel()
) {
    DashboardScreenContent(
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
fun DashboardScreenContent(
    monitorState: MonitorState?,
    state: DashboardState,
    events: SharedFlow<DashboardEvent>,
    onAction: (DashboardAction) -> Unit,
    onNavigation: (DashboardEvent.Navigation) -> Unit,
    isPreview: Boolean = false,
) {

    val scrollState = rememberScrollState()

    SingleEventEffect(events) { event ->
        when (event) {
            is DashboardEvent.Navigation -> onNavigation(event)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            GyroscopeSummary(monitorState, showPlot = true)
            ServoSummary(monitorState)
        }
    }
}