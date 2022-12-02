package com.watch_dex.feature_main.presentation.state

import com.watch_dex.core.data.Type

data class MainSelectedState(
    val pokemonName: String? = null,
    val typesSelected: List<Type> = listOf()
)
