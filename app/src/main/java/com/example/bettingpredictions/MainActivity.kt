package com.example.bettingpredictions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bettingpredictions.presentation.home.view.HomeScreen
import com.example.bettingpredictions.presentation.home.viewmodel.HomeViewModel
import com.example.bettingpredictions.presentation.result.view.ResultScreen
import com.example.bettingpredictions.presentation.result.viewmodel.ResultViewModel
import com.example.bettingpredictions.presentation.ui.theme.BettingPredictionsTheme
import com.example.bettingpredictions.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val homeViewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.getScreenOpenPos()
        setContent {
            BettingPredictionsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        ScreenMain(navController, homeViewModel)
                    }
                }
            }
        }
    }


    @Composable
    fun ScreenMain(navController: NavHostController, homeViewModel: HomeViewModel) {

        val resultViewModel: ResultViewModel by viewModels()

        var start = Screen.HomeScreenRoute.route
        if (homeViewModel.openScreenPos.value == 1)
            start = Screen.ResultScreenRoute.route

        NavHost(navController = navController, startDestination = start) {
            composable(Screen.HomeScreenRoute.route) {
                HomeScreen(navController, this@MainActivity.homeViewModel)
            }
            composable(Screen.ResultScreenRoute.route) {
                ResultScreen(navController, resultViewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BettingPredictionsTheme {

    }
}