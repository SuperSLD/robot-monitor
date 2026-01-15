import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import navigation.ConnectDestination
import navigation.MainDestination
import navigation.SplashDestination
import screens.connect.ConnectScreen
import screens.main.MainScreen
import screens.splash.SplashScreen

@Composable
fun StartAppScreen() {

    val navController = rememberNavController()

    NavHost(navController, SplashDestination) {
        composable<SplashDestination> {
            SplashScreen(navController)
        }
        composable<ConnectDestination> {
            ConnectScreen(navController)
        }
        composable<MainDestination> {
            MainScreen(navController)
        }
    }
}