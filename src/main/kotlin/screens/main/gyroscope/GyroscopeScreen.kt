package screens.main.gyroscope

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import common.SingleEventEffect
import common.components.CustomLinePlot
import common.components.MCard
import common.components.ParamsTable
import common.components.PlotLine
import domain.models.MonitorState
import domain.models.MpuResult
import domain.models.MpuState
import domain.models.RobotState
import kotlinx.coroutines.flow.SharedFlow
import org.jetbrains.letsPlot.Figure
import org.jetbrains.letsPlot.compose.PlotPanel
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.themes.themeGrey
import org.koin.compose.viewmodel.koinViewModel
import screens.main.dashboard.DashboardAction
import screens.main.dashboard.DashboardEvent
import screens.main.dashboard.DashboardState
import screens.main.dashboard.DashboardViewModel
import theme.Colors
import theme.MonitorText

@Composable
fun GyroscopeScreen(
    monitorState: MonitorState?,
    viewModel: DashboardViewModel = koinViewModel()
) {
    GyroscopeScreenContent(
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
fun GyroscopeScreenContent(
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

            GyroscopeSummary(monitorState)

            MCard(Modifier.height(300.dp)) {
                CustomLinePlot(
                    plotLines = listOf(
                        PlotLine(
                            x = listOf(),
                            y = monitorState?.stateList?.map { it.mpu6050?.temp ?: 0.0 } ?: emptyList(),
                            color = Colors.textError,
                            name = "temperature",
                        ),
                    ),
                    modifier = Modifier.height(400.dp),
                    title = "Temperature C ${monitorState?.robotState?.mpu6050?.temp}",
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ParamsTable(
                    title = "Accelerometer readings",
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    table = listOf(
                        listOf("Axis", "Value",),
                        listOf("X", monitorState?.robotState?.mpu6050?.accelX.toString()),
                        listOf("Y", monitorState?.robotState?.mpu6050?.accelY.toString()),
                        listOf("Z", monitorState?.robotState?.mpu6050?.accelZ.toString()),
                    )
                )
                ParamsTable(
                    title = "Gyroscope readings",
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    table = listOf(
                        listOf("Axis", "Value",),
                        listOf("X", monitorState?.robotState?.mpu6050?.gyroX.toString()),
                        listOf("Y", monitorState?.robotState?.mpu6050?.gyroY.toString()),
                        listOf("Z", monitorState?.robotState?.mpu6050?.gyroZ.toString()),
                    )
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MCard(Modifier.weight(1f)) {
                    CustomLinePlot(
                        plotLines = listOf(
                            PlotLine(
                                x = listOf(),
                                y = monitorState?.stateList?.map { it.mpu6050?.accelX ?: 0.0 } ?: emptyList(),
                                color = Colors.textError,
                                name = "accel x",
                            ),
                            PlotLine(
                                x = listOf(),
                                y = monitorState?.stateList?.map { it.mpu6050?.accelY ?: 0.0 } ?: emptyList(),
                                color = Colors.primary,
                                name = "accel y",
                            ),
                            PlotLine(
                                x = listOf(),
                                y = monitorState?.stateList?.map { it.mpu6050?.accelZ ?: 0.0 } ?: emptyList(),
                                color = Colors.textGreen,
                                name = "accel z",
                            ),
                        ),
                        modifier = Modifier.height(400.dp),
                        title = "Accelerometer readings",
                    )
                }

                MCard(Modifier.weight(1f)) {
                    CustomLinePlot(
                        plotLines = listOf(
                            PlotLine(
                                x = listOf(),
                                y = monitorState?.stateList?.map { it.mpu6050?.gyroX ?: 0.0 } ?: emptyList(),
                                color = Colors.textError,
                                name = "gyro x",
                            ),
                            PlotLine(
                                x = listOf(),
                                y = monitorState?.stateList?.map { it.mpu6050?.gyroY ?: 0.0 } ?: emptyList(),
                                color = Colors.primary,
                                name = "gyro y",
                            ),
                            PlotLine(
                                x = listOf(),
                                y = monitorState?.stateList?.map { it.mpu6050?.gyroZ ?: 0.0 } ?: emptyList(),
                                color = Colors.textGreen,
                                name = "gyro z",
                            ),
                        ),
                        modifier = Modifier.height(400.dp),
                        title = "Gyroscope readings",
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ParamsTable(
                    title = "Angle by accelerometer",
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    table = listOf(
                        listOf("Axis", "Value",),
                        listOf("X", monitorState?.robotState?.mpu6050?.angleX.toString()),
                        listOf("Y", monitorState?.robotState?.mpu6050?.angleY.toString()),
                        listOf("Z", monitorState?.robotState?.mpu6050?.angleZ.toString()),
                    )
                )
                ParamsTable(
                    title = "Angle by gyroscope and accelerometer",
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    table = listOf(
                        listOf("Axis", "Name" ,"Value",),
                        listOf("X", "roll", monitorState?.robotState?.mpu6050?.roll.toString()),
                        listOf("Y", "pitch", monitorState?.robotState?.mpu6050?.pitch.toString()),
                        listOf("Z", "yaw", monitorState?.robotState?.mpu6050?.yaw.toString()),
                    )
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MCard(Modifier.weight(1f)) {
                    CustomLinePlot(
                        plotLines = listOf(
                            PlotLine(
                                x = listOf(),
                                y = monitorState?.stateList?.map { it.mpu6050?.angleX ?: 0.0 } ?: emptyList(),
                                color = Colors.textError,
                                name = "angle x",
                            ),
                            PlotLine(
                                x = listOf(),
                                y = monitorState?.stateList?.map { it.mpu6050?.angleY ?: 0.0 } ?: emptyList(),
                                color = Colors.primary,
                                name = "angle y",
                            ),
                            PlotLine(
                                x = listOf(),
                                y = monitorState?.stateList?.map { it.mpu6050?.angleZ ?: 0.0 } ?: emptyList(),
                                color = Colors.textGreen,
                                name = "angle z",
                            ),
                        ),
                        modifier = Modifier.height(400.dp),
                        title = "Angle by accelerometer",
                    )
                }

                MCard(Modifier.weight(1f)) {
                    CustomLinePlot(
                        plotLines = listOf(
                            PlotLine(
                                x = listOf(),
                                y = monitorState?.stateList?.map { it.mpu6050?.roll ?: 0.0 } ?: emptyList(),
                                color = Colors.textError,
                                name = "roll",
                            ),
                            PlotLine(
                                x = listOf(),
                                y = monitorState?.stateList?.map { it.mpu6050?.pitch ?: 0.0 } ?: emptyList(),
                                color = Colors.primary,
                                name = "pitch",
                            ),
                            PlotLine(
                                x = listOf(),
                                y = monitorState?.stateList?.map { it.mpu6050?.yaw ?: 0.0 } ?: emptyList(),
                                color = Colors.textGreen,
                                name = "yaw",
                            ),
                        ),
                        modifier = Modifier.height(400.dp),
                        title = "Angle by gyroscope and accelerometer",
                    )
                }
            }
        }
    }
}

@Composable
fun GyroscopeSummary(monitorState: MonitorState?, showPlot: Boolean = false,) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        MCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .weight(0.5f)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("MPU6050", style = MonitorText.Bold.Sp32.White.style())
                Text("Gyroscope", style = MonitorText.Regular.Sp18.Gray.style())
                Spacer(Modifier.weight(1f))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Initialisation:", style = MonitorText.Bold.Sp18.White.style())
                    if (monitorState?.robotState?.mpuState?.isInit == true) {
                        Text("TRUE", style = MonitorText.Regular.Sp18.Green.style())
                    } else {
                        Text("FALSE", style = MonitorText.Regular.Sp18.Red.style())
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Calibration:", style = MonitorText.Bold.Sp18.White.style())
                    if (monitorState?.robotState?.mpuState?.calibrationFinished == true) {
                        Text("TRUE", style = MonitorText.Regular.Sp18.Green.style())
                    } else {
                        Text("FALSE", style = MonitorText.Regular.Sp18.Red.style())
                    }
                }
                if (monitorState?.robotState?.mpuState?.initError?.isNotBlank() ?: false) {
                    Text(
                        monitorState.robotState.mpuState.initError,
                        style = MonitorText.Regular.Sp16.Gray.style()
                    )
                }
            }
        }

        if (showPlot) {
            MCard(Modifier.weight(1f).height(300.dp)) {
                CustomLinePlot(
                    plotLines = listOf(
                        PlotLine(
                            x = listOf(),
                            y = monitorState?.stateList?.map { it.mpu6050?.roll ?: 0.0 } ?: emptyList(),
                            color = Colors.textError,
                            name = "roll",
                        ),
                        PlotLine(
                            x = listOf(),
                            y = monitorState?.stateList?.map { it.mpu6050?.pitch ?: 0.0} ?: emptyList(),
                            color = Colors.primary,
                            name = "pitch",
                        ),
                        PlotLine(
                            x = listOf(),
                            y = monitorState?.stateList?.map { it.mpu6050?.yaw ?: 0.0} ?: emptyList(),
                            color = Colors.textGreen,
                            name = "yaw",
                        ),
                    ),
                    modifier = Modifier.fillMaxSize(),
                    title = "Angle by gyroscope and accelerometer",
                )
            }
        }
    }
}