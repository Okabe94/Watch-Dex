package com.watch_dex.core.data

import com.watch_dex.core.data.Type.*
import com.watch_dex.feature_home.presentation.model.Effectiveness
import java.util.*

const val SUPER_EFFECTIVE = 4.0F
const val EFFECTIVE = 2.0F
const val NEUTRAL = 1F
const val NOT_EFFECTIVE = 0.5F
const val SUPER_NOT_EFFECTIVE = 0.25F
const val IMMUNE = 0.0F

class BalanceManager {

    private val typeList = listOf(
        Bug, Dark, Dragon,
        Electric, Fairy, Fighting,
        Fire, Flying, Ghost,
        Grass, Ground, Ice,
        Normal, Poison, Psychic,
        Rock, Steel, Water
    )

    private val relation: List<List<Float>> = listOf(
        // BUG
        listOf(
            NEUTRAL, EFFECTIVE, NEUTRAL,
            NEUTRAL, NOT_EFFECTIVE, NOT_EFFECTIVE,
            NOT_EFFECTIVE, NOT_EFFECTIVE, NOT_EFFECTIVE,
            EFFECTIVE, NEUTRAL, NEUTRAL,
            NEUTRAL, NOT_EFFECTIVE, EFFECTIVE,
            NEUTRAL, NOT_EFFECTIVE, NEUTRAL
        ),
        // DARK
        listOf(
            NEUTRAL, NOT_EFFECTIVE, NEUTRAL,
            NEUTRAL, NOT_EFFECTIVE, NOT_EFFECTIVE,
            NEUTRAL, NEUTRAL, EFFECTIVE,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NEUTRAL, NEUTRAL, EFFECTIVE,
            NEUTRAL, NEUTRAL, NEUTRAL
        ),
        // DRAGON
        listOf(
            NEUTRAL, NEUTRAL, EFFECTIVE,
            NEUTRAL, IMMUNE, NEUTRAL,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NEUTRAL, NOT_EFFECTIVE, NEUTRAL
        ),
        // ELECTRIC
        listOf(
            NEUTRAL, NEUTRAL, NOT_EFFECTIVE,
            NOT_EFFECTIVE, NEUTRAL, NEUTRAL,
            NEUTRAL, EFFECTIVE, NEUTRAL,
            NOT_EFFECTIVE, IMMUNE, NEUTRAL,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NEUTRAL, NEUTRAL, EFFECTIVE
        ),
        // FAIRY
        listOf(
            NEUTRAL, EFFECTIVE, EFFECTIVE,
            NEUTRAL, NEUTRAL, EFFECTIVE,
            NOT_EFFECTIVE, NEUTRAL, NEUTRAL,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NEUTRAL, NOT_EFFECTIVE, NEUTRAL,
            NEUTRAL, NOT_EFFECTIVE, NEUTRAL
        ),
        // FIGHTING
        listOf(
            NOT_EFFECTIVE, EFFECTIVE, NEUTRAL,
            NEUTRAL, NOT_EFFECTIVE, NEUTRAL,
            NEUTRAL, NOT_EFFECTIVE, IMMUNE,
            NEUTRAL, NEUTRAL, EFFECTIVE,
            EFFECTIVE, NOT_EFFECTIVE, NOT_EFFECTIVE,
            EFFECTIVE, EFFECTIVE, NEUTRAL
        ),
        // FIRE
        listOf(
            EFFECTIVE, NEUTRAL, NOT_EFFECTIVE,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NOT_EFFECTIVE, NEUTRAL, NEUTRAL,
            EFFECTIVE, NEUTRAL, EFFECTIVE,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NOT_EFFECTIVE, EFFECTIVE, NOT_EFFECTIVE
        ),
        // FLYING
        listOf(
            EFFECTIVE, NEUTRAL, NEUTRAL,
            NOT_EFFECTIVE, NEUTRAL, EFFECTIVE,
            NEUTRAL, NEUTRAL, NEUTRAL,
            EFFECTIVE, NEUTRAL, NEUTRAL,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NOT_EFFECTIVE, NOT_EFFECTIVE, NEUTRAL
        ),
        //GHOST
        listOf(
            NEUTRAL, NOT_EFFECTIVE, NEUTRAL,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NEUTRAL, NEUTRAL, EFFECTIVE,
            NEUTRAL, NEUTRAL, NEUTRAL,
            IMMUNE, NEUTRAL, EFFECTIVE,
            NEUTRAL, NEUTRAL, NEUTRAL
        ),
        // GRASS
        listOf(
            NOT_EFFECTIVE, NEUTRAL, NOT_EFFECTIVE,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NOT_EFFECTIVE, NOT_EFFECTIVE, NEUTRAL,
            NOT_EFFECTIVE, EFFECTIVE, NEUTRAL,
            NEUTRAL, NOT_EFFECTIVE, NEUTRAL,
            EFFECTIVE, NOT_EFFECTIVE, EFFECTIVE
        ),
        // GROUND
        listOf(
            NOT_EFFECTIVE, NEUTRAL, NEUTRAL,
            EFFECTIVE, NEUTRAL, NEUTRAL,
            EFFECTIVE, IMMUNE, NEUTRAL,
            NOT_EFFECTIVE, NEUTRAL, NEUTRAL,
            NEUTRAL, EFFECTIVE, NEUTRAL,
            EFFECTIVE, EFFECTIVE, NEUTRAL
        ),
        // ICE
        listOf(
            NEUTRAL, NEUTRAL, EFFECTIVE,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NOT_EFFECTIVE, EFFECTIVE, NEUTRAL,
            EFFECTIVE, EFFECTIVE, NOT_EFFECTIVE,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NEUTRAL, NOT_EFFECTIVE, NOT_EFFECTIVE
        ),
        // NORMAL
        listOf(
            NEUTRAL, NEUTRAL, NEUTRAL,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NEUTRAL, NEUTRAL, IMMUNE,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NOT_EFFECTIVE, NOT_EFFECTIVE, NEUTRAL
        ),
        // POISON
        listOf(
            NEUTRAL, NEUTRAL, NEUTRAL,
            NEUTRAL, EFFECTIVE, NEUTRAL,
            NEUTRAL, NEUTRAL, NOT_EFFECTIVE,
            EFFECTIVE, NOT_EFFECTIVE, NEUTRAL,
            NEUTRAL, NOT_EFFECTIVE, NEUTRAL,
            NOT_EFFECTIVE, IMMUNE, NEUTRAL
        ),
        // PSYCHIC
        listOf(
            NEUTRAL, IMMUNE, NEUTRAL,
            NEUTRAL, NEUTRAL, EFFECTIVE,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NEUTRAL, EFFECTIVE, NOT_EFFECTIVE,
            NEUTRAL, NOT_EFFECTIVE, NEUTRAL
        ),
        // ROCK
        listOf(
            EFFECTIVE, NEUTRAL, NEUTRAL,
            NEUTRAL, NEUTRAL, NOT_EFFECTIVE,
            EFFECTIVE, EFFECTIVE, NEUTRAL,
            NEUTRAL, NOT_EFFECTIVE, EFFECTIVE,
            NEUTRAL, NEUTRAL, NEUTRAL,
            NEUTRAL, NOT_EFFECTIVE, NEUTRAL
        ),
        // STEEL
        listOf(
            NEUTRAL, NEUTRAL, NEUTRAL,
            NOT_EFFECTIVE, EFFECTIVE, NEUTRAL,
            NOT_EFFECTIVE, NEUTRAL, NEUTRAL,
            NEUTRAL, NEUTRAL, EFFECTIVE,
            NEUTRAL, NEUTRAL, NEUTRAL,
            EFFECTIVE, NOT_EFFECTIVE, NOT_EFFECTIVE
        ),
        //WATER
        listOf(
            NEUTRAL, NEUTRAL, NOT_EFFECTIVE,
            NEUTRAL, NEUTRAL, NEUTRAL,
            EFFECTIVE, NEUTRAL, NEUTRAL,
            NOT_EFFECTIVE, EFFECTIVE, NEUTRAL,
            NEUTRAL, NEUTRAL, NEUTRAL,
            EFFECTIVE, NEUTRAL, NOT_EFFECTIVE
        )
    )

    private fun getOffense(type: Type) = relation[getIndex(type)]

    private fun getDefense(type: Type): List<Float> {
        val index = getIndex(type)
        return relation.map { it[index] }
    }

    private fun calculateRelation(
        types: List<Type>,
        sideOfBattle: (Type) -> List<Float>
    ): List<Float> {
        if (types.isEmpty()) return listOf()
        val firstTypeRelation = sideOfBattle(types.first())
        return if (types.size == 1) firstTypeRelation
        else {
            val secondTypeRelation = sideOfBattle(types.last())
            merge(firstTypeRelation, secondTypeRelation)
        }
    }

    private fun merge(
        first: List<Float>,
        second: List<Float>,
    ): List<Float> = mutableListOf<Float>().apply {
        repeat(first.size) { add(first[it] * second[it]) }
    }

    private fun EnumMap<Effectiveness, MutableList<Type>>.computeOrAdd(
        effectiveness: Effectiveness,
        type: Type
    ) {
        computeIfPresent(effectiveness) { _, list -> list.apply { add(type) } }
        putIfAbsent(effectiveness, mutableListOf(type))
    }

    private fun getIndex(type: Type) = typeList.indexOf(type)

    private fun fillBalanceMap(
        calculationList: List<Float>,
        effectivenessDivision: (index: Int, value: Float, map: EnumMap<Effectiveness, MutableList<Type>>) -> Unit
    ) = EnumMap<Effectiveness, MutableList<Type>>(Effectiveness::class.java).apply {
        calculationList.forEachIndexed { index, value ->
            effectivenessDivision(index, value, this)
        }
    }

    private fun offense(types: List<Type>): EnumMap<Effectiveness, MutableList<Type>> {
        val calculationList = calculateRelation(types, ::getOffense)
        return fillBalanceMap(calculationList) { index, value, map ->
            when {
                value >= EFFECTIVE -> map.computeOrAdd(
                    Effectiveness.Effective,
                    typeList[index]
                )
                value >= SUPER_NOT_EFFECTIVE && value < NEUTRAL -> map.computeOrAdd(
                    Effectiveness.NotEffective,
                    typeList[index]
                )
                value == IMMUNE -> map.computeOrAdd(Effectiveness.Immune, typeList[index])
            }
        }
    }

    private fun defense(types: List<Type>): EnumMap<Effectiveness, MutableList<Type>> {
        val calculationList = calculateRelation(types, ::getDefense)
        return fillBalanceMap(calculationList) { index, value, map ->
            when {
                value >= SUPER_EFFECTIVE -> map.computeOrAdd(
                    Effectiveness.SuperEffective,
                    typeList[index]
                )
                value >= EFFECTIVE -> map.computeOrAdd(
                    Effectiveness.Effective,
                    typeList[index]
                )
                value >= NOT_EFFECTIVE && value < NEUTRAL -> map.computeOrAdd(
                    Effectiveness.NotEffective,
                    typeList[index]
                )
                value >= SUPER_NOT_EFFECTIVE && value < NEUTRAL -> map.computeOrAdd(
                    Effectiveness.SuperNotEffective,
                    typeList[index]
                )
                value == IMMUNE -> map.computeOrAdd(Effectiveness.Immune, typeList[index])
            }
        }
    }

    fun getBalanceMap(
        isOffensive: Boolean,
        types: List<Type>
    ): EnumMap<Effectiveness, MutableList<Type>> {
        return if (isOffensive) offense(types)
        else defense(types)
    }
}
