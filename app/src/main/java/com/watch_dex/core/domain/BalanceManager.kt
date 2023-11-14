package com.watch_dex.core.domain

import com.watch_dex.core.data.Type
import com.watch_dex.feature_home.domain.Effectiveness
import java.util.EnumMap

const val SUPER_EFFECTIVE = 4.0F
const val EFFECTIVE = 2.0F
const val NEUTRAL = 1F
const val NOT_EFFECTIVE = 0.5F
const val SUPER_NOT_EFFECTIVE = 0.25F
const val IMMUNE = 0.0F

interface BalanceManager {
    fun getBalanceMap(
        isOffensive: Boolean,
        types: List<Type>
    ): EnumMap<Effectiveness, MutableList<Type>>
}
