package com.watch_dex.feature_home.presentation.state

import androidx.navigation.NavController

sealed class HomeEvent {
    object SelectDefensive : HomeEvent()
    object SelectOffensive : HomeEvent()
    class SelectByType(val navController: NavController) : HomeEvent()
    class SelectByPokemon(val navController: NavController) : HomeEvent()
}
