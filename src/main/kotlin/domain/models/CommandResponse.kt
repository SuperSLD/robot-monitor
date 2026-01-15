package domain.models

import theme.Colors

data class CommandResponse(
    val text: String,
    val type: Int,
) {

    companion object {
        const val DEFAULT = 0
        const val ERROR = 1
        const val SUCCESS = 2
        const val WARNING = 3
        const val INFO = 4
    }

    fun color() = when(type) {
        DEFAULT -> Colors.textPrimary
        ERROR -> Colors.textError
        SUCCESS -> Colors.textGreen
        WARNING -> Colors.textPrimary
        INFO -> Colors.textSecondary
        else -> Colors.textPrimary
    }
}
