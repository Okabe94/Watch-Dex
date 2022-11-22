package com.watch_dex.feature_list_selection.presentation.state

import com.watch_dex.core.data.model.PokemonFromList

data class ListSelectionState(
    val letterSelected: Char? = null,
    val pokemonDisplayed: List<PokemonFromList> = emptyList(),
    val initials: List<Char> = listOf(
        'A', 'B', 'C', 'D', 'E',
        'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O',
        'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z'
    )
)
