package domain.socket

import com.google.gson.Gson
import domain.models.CommandResponse
import domain.models.Message
import domain.models.MonitorState
import domain.models.RobotState
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.removeFirst
import kotlin.text.compareTo

abstract class IMonitorStateConnection {

    protected val _uiState = MutableStateFlow(MonitorState())
    val uiState: StateFlow<MonitorState> = _uiState.asStateFlow()

    abstract suspend fun connect(ip: String, port: Int)

    abstract suspend fun connectionInfo(): Connection

    abstract suspend fun disconnect()

    abstract suspend fun sendCommand(command: String)

    abstract fun onCommandResponse(callback: (CommandResponse) -> Unit)
}

class MonitorStateConnection: IMonitorStateConnection() {

    init {
        reconnectCycle()
    }

    private var ip: String? = null
    private var port: Int? = null
    private var receiveMessageJob: Job? = null
    private var reconnectJob: Job? = null

    private var gson: Gson = Gson()

    private var socket: Socket? = null
    private var sendChannel: ByteWriteChannel? = null

    private var onCommandResponseCallback: ((CommandResponse) -> Unit)? = null

    @Volatile
    var stateList = mutableListOf<RobotState>()

    @OptIn(DelicateCoroutinesApi::class)
    private fun reconnectCycle() {
        reconnectJob = GlobalScope.async {
            while (true) {
                try {
                    if (isConnectonClosed() && ip != null && port != null) {
                        println("Try connect to $ip:$port")
                        openConnection(ip!!, port!!)
                    }
                } catch (_: Exception) {}
                delay(1000)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun connect(ip: String, port: Int) {
        this.ip = ip
        this.port = port
        _uiState.emit(_uiState.value.copy(connection = connectionInfo()))
    }

    private suspend fun openConnection(ip: String, port: Int) {
        receiveMessageJob?.cancel()
        val selectorManager = SelectorManager(Dispatchers.IO)
        socket = aSocket(selectorManager).tcp().connect(ip, port)

        val receiveChannel = socket?.openReadChannel()
        sendChannel = socket?.openWriteChannel(autoFlush = true)

        receiveMessageJob = GlobalScope.async {
            while (true) {
                val line = receiveChannel?.readUTF8Line()
                if (line != null) {
                    try {
                        val message = gson.fromJson(line, Message::class.java)
                        parseMessage(message)
                    } catch (ex: Exception) {}
                } else {
                    println("Socket disconnect: $ip:$port")
                    _uiState.emit(
                        _uiState.value.copy(
                            connection = connectionInfo(),
                        )
                    )
                    socket?.close()
                    break
                }
            }
        }
    }

    private suspend fun parseMessage(message: Message) {
        when(message.type) {
            "state" -> {
                val robotState = gson.fromJson(message.payload, RobotState::class.java)
                if (stateList.size > STATE_LIST_SIZE) {
                    stateList.removeFirst()
                }
                stateList.add(robotState)

                _uiState.emit(
                    _uiState.value.copy(
                        connection = connectionInfo(),
                        robotState = robotState,
                        stateList = stateList,
                    )
                )
            }
            "command" -> {
                val command = gson.fromJson(message.payload, CommandResponse::class.java)
                onCommandResponseCallback?.invoke(command)
            }
        }
    }

    override suspend fun connectionInfo() = Connection(
        ip = ip ?: "null",
        port = port ?: 0,
        isOpen = !isConnectonClosed(),
    )

    private fun isConnectonClosed() = socket?.isClosed == true || socket == null

    private suspend fun sendMessage(string: String) {
        sendChannel?.writeStringUtf8(string + "\n")
    }

    override suspend fun disconnect() {
        this.ip = null
        this.port = null
        socket?.close()
    }

    override suspend fun sendCommand(command: String) {
        sendMessage(
            gson.toJson(
                Message(
                    type = "command",
                    payload = command
                )
            )
        )
    }

    override fun onCommandResponse(callback: (CommandResponse) -> Unit) {
        onCommandResponseCallback = callback
    }
}

data class Connection(
    val ip: String,
    val port: Int,
    val isOpen: Boolean,
)

const val STATE_LIST_SIZE = 100