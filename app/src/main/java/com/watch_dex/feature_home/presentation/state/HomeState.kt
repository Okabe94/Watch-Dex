package com.watch_dex.feature_home.presentation.state

import com.watch_dex.core.data.Type
import com.watch_dex.feature_home.presentation.model.Effectiveness
import java.util.*
import kotlin.random.Random

data class HomeState(
    val side: Side = Side.Defensive,
    val pokemonName: String? = null,
    val byPokemonEnabled: Boolean = false,
    val randomType: Type = Type.values().random(Random(System.currentTimeMillis())),
    val typesSelected: List<Type> = listOf(),
    val offensiveMap: EnumMap<Effectiveness, MutableList<Type>> = EnumMap(Effectiveness::class.java),
    val defensiveMap: EnumMap<Effectiveness, MutableList<Type>> = EnumMap(Effectiveness::class.java)
) {
    fun getBalanceMap() = if (side == Side.Offensive) offensiveMap else defensiveMap
}

enum class Side {
    Offensive, Defensive
}
