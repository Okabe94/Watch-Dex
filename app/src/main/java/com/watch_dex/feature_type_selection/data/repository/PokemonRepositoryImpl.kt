package com.watch_dex.feature_type_selection.data.repository

import com.watch_dex.core.data.Type.*
import com.watch_dex.core.data.pokemonList
import com.watch_dex.feature_type_selection.domain.repository.PokemonRepository

class PokemonRepositoryImpl : PokemonRepository {

    override suspend fun getAllTypes() = listOf(
        Bug, Dark, Dragon, Electric, Fairy, Fighting,
        Fire, Flying, Ghost, Grass, Ground, Ice,
        Normal, Poison, Psychic, Rock, Steel, Water
    )

    override suspend fun getAllPokemon() = pokemonList
}
