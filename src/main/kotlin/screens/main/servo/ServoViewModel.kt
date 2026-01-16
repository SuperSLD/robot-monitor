package screens.main.servo

import common.BaseViewModel
import domain.socket.IMonitorStateConnection

class ServoViewModel(
    val monitorStateConnection: IMonitorStateConnection,
): BaseViewModel<ServoState, ServoEvent>() {

    override fun startState() = ServoState

    fun onAction(action: ServoAction) {
        when(action) {
            is ServoAction.OnAngleChanhe -> onChangeAngle(action.servoName, action.angle)
        }
    }

    private fun onChangeAngle(servoName: String, angle: Double) {
        launchUI {
            withIO {
                monitorStateConnection.sendCommand("servo $servoName ${angle.toInt()}")
            }
        }
    }
}