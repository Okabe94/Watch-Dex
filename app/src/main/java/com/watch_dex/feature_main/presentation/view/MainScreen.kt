package com.watch_dex.feature_main.presentation.view

import androidx.compose.runtime.Composable
import com.watch_dex.core.presentation.util.navigation.SetupNavGraph
import com.watch_dex.theme.WearAppTheme

@Composable
fun MainScreen(viewModel: MainViewModel) {
    WearAppTheme {
        SetupNavGraph(viewModel)
    }
}
