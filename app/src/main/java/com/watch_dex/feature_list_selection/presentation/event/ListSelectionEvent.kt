package com.watch_dex.feature_list_selection.presentation.event

import com.watch_dex.core.data.model.PokemonFromList

sealed class ListSelectionEvent {
    class OnLetterClick(val char: Char?) : ListSelectionEvent()
    class OnPokemonClick(val selection: PokemonFromList) : ListSelectionEvent()
}
