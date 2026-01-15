import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.provideDomainModule
import di.provideViewModelModule
import org.koin.compose.KoinApplication
import theme.Colors
import theme.MonitorTheme

@Composable
@Preview
fun App() {
    MonitorTheme {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(color = Colors.background)
        ) {
            StartAppScreen()
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "monitor",
        undecorated = true,
        transparent = true
    ) {
        Column(modifier = Modifier.fillMaxSize().background(Colors.backgroundSecondary)) {

            // --- ТУЛБАР ---
            WindowDraggableArea { // Всё, что внутри этого блока, позволяет тащить окно
                Surface(
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    color = Colors.backgroundSecondary,
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = "monitor", color = Colors.textPrimary)

                        Row {
                            IconButton(
                                onClick = { },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Text("—", color = Colors.textPrimary)
                            }
                            IconButton(
                                onClick = { exitApplication() },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Text("✕", color = Colors.textError)
                            }
                        }
                    }
                }
            }

            Box(Modifier.fillMaxSize()) {
                KoinApplication(application = {
                    modules(
                        listOf(
                            provideViewModelModule,
                            provideDomainModule,
                        )
                    )
                }) {
                    App()
                }
            }
        }
    }
}
