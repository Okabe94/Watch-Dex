package com.watch_dex.feature_main.presentation.state

import com.watch_dex.core.data.Type
import com.watch_dex.core.domain.dto.PokemonDTO

data class MainSelectedState(
    val pokemonDTO: PokemonDTO? = null,
    val typesSelected: List<Type> = listOf()
)
