package com.watch_dex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.watch_dex.core.presentation.viewModelFactory
import com.watch_dex.di.MainApplication.Companion.appModule
import com.watch_dex.feature_main.presentation.view.MainScreen
import com.watch_dex.feature_main.presentation.view.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<MainViewModel>(
                factory = viewModelFactory {
                    with(appModule) {
                        MainViewModel(
                            pokemonDao = pokemonDao,
                            balanceManager = balanceManager,
                            allTypes = typeRepository.getAllTypes(),
                            theme = appTheme
                        )
                    }
                }
            )

            MainScreen(viewModel)
        }
    }

}
