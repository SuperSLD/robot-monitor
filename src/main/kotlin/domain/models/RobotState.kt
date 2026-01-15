package domain.models

import domain.socket.Connection

data class MonitorState(
    val connection: Connection? = null,
    val robotState: RobotState? = null,
    val stateList: List<RobotState> = emptyList(),
)

data class RobotState(
    val mpuState: MpuState?,
    val mpu6050: MpuResult?,
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