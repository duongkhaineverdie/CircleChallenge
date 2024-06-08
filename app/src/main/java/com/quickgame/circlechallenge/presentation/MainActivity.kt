package com.quickgame.circlechallenge.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.quickgame.circlechallenge.domain.navigation.Destination
import com.quickgame.circlechallenge.domain.navigation.NavHostTrueColor
import com.quickgame.circlechallenge.domain.navigation.composable
import com.quickgame.circlechallenge.presentation.ui.circlechallenge.CircleChallengeScreen
import com.quickgame.circlechallenge.presentation.ui.home.HomeScreen
import com.quickgame.circlechallenge.presentation.ui.home.HomeViewModel
import com.quickgame.circlechallenge.presentation.ui.setting.SettingScreen
import com.quickgame.circlechallenge.presentation.ui.theme.CircleChallengeTheme
import org.koin.android.ext.android.getKoin
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    private val koin = getKoin()
    private val mainViewModel: MainViewModel = koin.get<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars.
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Add a listener to update the behavior of the toggle fullscreen button when
        // the system bars are hidden or revealed.
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            // You can hide the caption bar even when the other system bars are visible.
            // To account for this, explicitly check the visibility of navigationBars()
            // and statusBars() rather than checking the visibility of systemBars().
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            view.onApplyWindowInsets(windowInsets)
        }

        setContent {
            val navController: NavHostController = rememberNavController()
            CircleChallengeTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupNavigation(navController = navController, this::finish)
                }
            }
        }
    }

    override fun onPause() {
        mainViewModel.onPause()
        super.onPause()
    }
    override fun onResume() {
        mainViewModel.onResume()
        super.onResume()
    }

}

@Composable
private fun SetupNavigation(navController: NavHostController, onFinish: () -> Unit) {
    NavHostTrueColor(navController = navController, startDestination = Destination.HomeScreen) {
        composable(Destination.HomeScreen) { HomeScreen(navController, onFinish) }
        composable(Destination.CircleChallenge) { CircleChallengeScreen(navController) }
        composable(Destination.SettingScreen) { SettingScreen(navController) }
    }
}