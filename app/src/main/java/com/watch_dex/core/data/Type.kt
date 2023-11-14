package com.watch_dex.core.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.watch_dex.R
import com.watch_dex.core.presentation.util.colors.*

enum class Type(
    val typeId: String,
    @StringRes val nameId: Int,
    val color: Color,
    @DrawableRes val icon: Int
) {
    Bug("0", R.string.bug_type, bugTypeColor, R.drawable.ic_bug_type),
    Dark("1", R.string.dark_type, darkTypeColor, R.drawable.ic_dark_type),
    Dragon("2", R.string.dragon_type, dragonTypeColor, R.drawable.ic_dragon_type),
    Electric("3", R.string.electric_type, electricTypeColor, R.drawable.ic_electric_type),
    Fairy("4", R.string.fairy_type, fairyTypeColor, R.drawable.ic_fairy_type),
    Fighting("5", R.string.fighting_type, fightingTypeColor, R.drawable.ic_fighting_type),
    Fire("6", R.string.fire_type, fireTypeColor, R.drawable.ic_fire_type),
    Flying("7", R.string.flying_type, flyingTypeColor, R.drawable.ic_flying_type),
    Ghost("8", R.string.ghost_type, ghostTypeColor, R.drawable.ic_ghost_type),
    Grass("9", R.string.grass_type, grassTypeColor, R.drawable.ic_grass_type),
    Ground("A", R.string.ground_type, groundTypeColor, R.drawable.ic_ground_type),
    Ice("B", R.string.ice_type, iceTypeColor, R.drawable.ic_ice_type),
    Normal("C", R.string.normal_type, normalTypeColor, R.drawable.ic_normal_type),
    Poison("D", R.string.poison_type, poisonTypeColor, R.drawable.ic_poison_type),
    Psychic("E", R.string.psychic_type, psychicTypeColor, R.drawable.ic_psychic_type),
    Rock("F", R.string.rock_type, rockTypeColor, R.drawable.ic_rock_type),
    Steel("G", R.string.steel_type, steelTypeColor, R.drawable.ic_steel_type),
    Water("H", R.string.water_type, waterTypeColor, R.drawable.ic_water_type)
}

fun pokemonTypeFromId(id: Char) = Type.values().firstOrNull { it.typeId == id.toString() }
