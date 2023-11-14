package com.watch_dex.feature_list_selection.presentation.state

import com.watch_dex.core.data.Type
import com.watch_dex.core.domain.dto.PokemonDTO

data class ListSelectionState(
    val theme: Type,
    val letterSelected: Char? = null,
    val pokemonDisplayed: List<PokemonDTO> = emptyList(),
    val initials: List<Char> = ('A'..'Z').toList(),
)
