package com.watch_dex.core.presentation.util.navigation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object TypeSelectionScreen : Screen("type_selection_screen")
    object ListSelectionScreen : Screen("list_selection_screen")
}
