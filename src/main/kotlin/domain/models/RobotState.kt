package domain.models

import androidx.compose.ui.graphics.Color
import domain.socket.Connection

data class MonitorState(
    val connection: Connection? = null,
    val robotState: RobotState? = null,
    val stateList: List<RobotState> = emptyList(),
    val logs: List<Pair<Color, String>> = emptyList(),
)

data class RobotState(
    val mpuState: MpuState?,
    val mpu6050: MpuResult?,
    val servoState: ServoState?,
    val time: Long
)

/**
 * Состояние гироскопа
 */
data class MpuState(
    val isInit: Boolean,
    val calibrationFinished: Boolean,
    val initError: String?,
)

/**
 * Результат последнего замера гироскопа.
 */
data class MpuResult(
    val accelX: Double,
    val accelY: Double,
    val accelZ: Double,
    val temp: Double,
    val gyroX: Double,
    val gyroY: Double,
    val gyroZ: Double,
    val yaw: Double,
    val roll: Double,
    val pitch: Double,
    val angleX: Double,
    val angleY: Double,
    val angleZ: Double,
)

data class ServoState(
    val servoMotors: List<ServoMotor>,
)

data class ServoMotor(
    val name: String,
    val humanName: String,
    val currentAngle: Double,
)