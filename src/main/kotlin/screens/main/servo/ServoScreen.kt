package screens.main.servo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import common.SingleEventEffect
import common.components.MCard
import domain.models.MonitorState
import kotlinx.coroutines.flow.SharedFlow
import org.koin.compose.viewmodel.koinViewModel
import screens.main.dashboard.DashboardAction
import screens.main.dashboard.DashboardEvent
import screens.main.dashboard.DashboardState
import screens.main.dashboard.DashboardViewModel
import screens.main.gyroscope.GyroscopeSummary
import theme.MonitorText

@Composable
fun ServoScreen(
    monitorState: MonitorState?,
    viewModel: ServoViewModel = koinViewModel()
) {
    ServoScreenContent(
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
fun ServoScreenContent(
    monitorState: MonitorState?,
    state: ServoState,
    events: SharedFlow<ServoEvent>,
    onAction: (ServoAction) -> Unit,
    onNavigation: (ServoEvent.Navigation) -> Unit,
    isPreview: Boolean = false,
) {

    val scrollState = rememberScrollState()

    SingleEventEffect(events) { event ->
        when (event) {
            is ServoEvent.Navigation -> onNavigation(event)
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

            ServoSummary(
                monitorState = monitorState,
                canMove = true,
                onAngleChanhe = { name, angle ->
                    onAction(ServoAction.OnAngleChanhe(name, angle))
                }
            )
        }
    }
}

@Composable
fun ServoSummary(monitorState: MonitorState?, canMove: Boolean = false, onAngleChanhe: ((String, Double)-> Unit)? = null) {
    if (monitorState?.robotState?.servoState == null) return
    var counter = 0
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        for (i in 0 until 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (j in 0 until 4) {
                    if (counter < monitorState.robotState.servoState.servoMotors.size) {
                        ServoView(
                            servoMotor = monitorState.robotState.servoState.servoMotors[counter],
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            canMove = canMove
                        ) { name, angle ->
                            onAngleChanhe?.invoke(name, angle)
                        }
                    } else {
                        MCard(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                        ) {
                            Text("X", style = MonitorText.Bold.Sp18.Red.style(), modifier = Modifier.fillMaxSize())
                        }
                    }
                    counter++
                }
            }
        }
    }
}