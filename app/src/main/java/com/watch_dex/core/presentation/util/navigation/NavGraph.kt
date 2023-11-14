package com.watch_dex.core.presentation.util.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.watch_dex.feature_home.presentation.view.HomeScreen
import com.watch_dex.feature_list_selection.presentation.view.ListSelectionScreen
import com.watch_dex.feature_main.presentation.view.MainViewModel
import com.watch_dex.feature_type_selection.presentation.view.TypeSelectionScreen

@Composable
fun SetupNavGraph(
    viewModel: MainViewModel,
    navController: NavHostController = rememberSwipeDismissableNavController()
) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(viewModel, navController)
        }
        composable(route = Screen.TypeSelectionScreen.route) {
            TypeSelectionScreen(viewModel, navController)
        }
        composable(route = Screen.ListSelectionScreen.route) {
            ListSelectionScreen(viewModel, navController)
        }
    }
}
