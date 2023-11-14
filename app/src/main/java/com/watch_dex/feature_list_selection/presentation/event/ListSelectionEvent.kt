package com.watch_dex.feature_list_selection.presentation.event

import com.watch_dex.core.domain.dto.PokemonDTO

sealed class ListSelectionEvent {
    class OnLetterClick(val char: Char?) : ListSelectionEvent()
    class OnPokemonClick(val selection: PokemonDTO) : ListSelectionEvent()
}
