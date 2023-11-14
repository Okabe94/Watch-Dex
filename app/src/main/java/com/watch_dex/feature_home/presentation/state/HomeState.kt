package com.watch_dex.feature_home.presentation.state

import com.watch_dex.core.data.Type
import com.watch_dex.core.domain.dto.PokemonDTO
import com.watch_dex.feature_home.domain.Effectiveness
import java.util.*
import kotlin.random.Random

data class HomeState(
    val theme: Type,
    val typesSelected: List<Type> = emptyList(),
    val isOffensive: Boolean = false,
    val pokemonDTO: PokemonDTO? = null,
    val byListEnabled: Boolean = true,
    val balanceMap: EnumMap<Effectiveness, MutableList<Type>> = EnumMap(Effectiveness::class.java)
)
