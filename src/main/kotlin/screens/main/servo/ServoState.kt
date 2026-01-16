package screens.main.servo

data object ServoState

sealed class ServoAction {

    data class OnAngleChanhe(
        val servoName: String,
        val angle: Double,
    ): ServoAction()
}

sealed class ServoEvent {

    sealed class Navigation: ServoEvent() {

    }
}