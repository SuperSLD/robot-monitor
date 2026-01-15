package screens.splash

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import common.SingleEventEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import navigation.ConnectDestination
import navigation.MainDestination
import org.koin.compose.viewmodel.koinViewModel
import theme.Colors

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = koinViewModel()
) {
    SplashScreenContent(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        events = viewModel.uiEvents,
        onAction = viewModel::onAction,
        onNavigation = {
            when(it) {
                is SplashEvent.SplashNavigation.NavigateToConnect -> {
                    navController.popBackStack()
                    navController.navigate(ConnectDestination)
                }
                is SplashEvent.SplashNavigation.NavigateToMain -> {
                    navController.popBackStack()
                    navController.navigate(MainDestination)
                }
            }
        }
    )
}

@Composable
fun SplashScreenContent(
    state: SplashState,
    events: SharedFlow<SplashEvent>,
    onAction: (SplashAction) -> Unit,
    onNavigation: (SplashEvent.SplashNavigation) -> Unit,
) {

    SingleEventEffect(events) { event ->
        when (event) {
            is SplashEvent.SplashNavigation -> onNavigation(event)
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "/monitor",
                fontSize = 64.sp,
                color = Colors.white,
            )
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreenContent(
        state = SplashState,
        onAction = {},
        events = MutableSharedFlow(),
        onNavigation = {},
    )
}