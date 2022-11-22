package com.watch_dex.feature_type_selection.domain.repository

import com.watch_dex.core.data.Type
import com.watch_dex.core.data.model.PokemonFromList

interface PokemonRepository {
    suspend fun getAllTypes(): List<Type>
    suspend fun getAllPokemon(): List<PokemonFromList>
}
