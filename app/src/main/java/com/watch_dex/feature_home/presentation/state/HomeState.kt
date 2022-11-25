package com.watch_dex.feature_home.presentation.state

import com.watch_dex.core.data.Type
import com.watch_dex.feature_home.presentation.model.Effectiveness
import java.util.*
import kotlin.random.Random

data class HomeState(
    val typesSelected: List<Type> = emptyList(),
    val isOffensive: Boolean = false,
    val pokemonName: String? = null,
    val byListEnabled: Boolean = false,
    val randomType: Type = Type.values().random(Random(System.currentTimeMillis())),
    val balanceMap: EnumMap<Effectiveness, MutableList<Type>> = EnumMap(Effectiveness::class.java)
)
