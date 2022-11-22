package com.watch_dex.core.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.watch_dex.R
import com.watch_dex.core.presentation.util.colors.*

enum class Type(
    @StringRes val nameId: Int,
    val color: Color,
    @DrawableRes val icon: Int
) {
    Bug(R.string.bug_type, bugTypeColor, R.drawable.ic_bug_type),
    Dark(R.string.dark_type, darkTypeColor, R.drawable.ic_dark_type),
    Dragon(R.string.dragon_type, dragonTypeColor, R.drawable.ic_dragon_type),
    Electric(R.string.electric_type, electricTypeColor, R.drawable.ic_electric_type),
    Fairy(R.string.fairy_type, fairyTypeColor, R.drawable.ic_fairy_type),
    Fighting(R.string.fighting_type, fightingTypeColor, R.drawable.ic_fighting_type),
    Fire(R.string.fire_type, fireTypeColor, R.drawable.ic_fire_type),
    Flying(R.string.flying_type, flyingTypeColor, R.drawable.ic_flying_type),
    Ghost(R.string.ghost_type, ghostTypeColor, R.drawable.ic_ghost_type),
    Grass(R.string.grass_type, grassTypeColor, R.drawable.ic_grass_type),
    Ground(R.string.ground_type, groundTypeColor, R.drawable.ic_ground_type),
    Ice(R.string.ice_type, iceTypeColor, R.drawable.ic_ice_type),
    Normal(R.string.normal_type, normalTypeColor, R.drawable.ic_normal_type),
    Poison(R.string.poison_type, poisonTypeColor, R.drawable.ic_poison_type),
    Psychic(R.string.psychic_type, psychicTypeColor, R.drawable.ic_psychic_type),
    Rock(R.string.rock_type, rockTypeColor, R.drawable.ic_rock_type),
    Steel(R.string.steel_type, steelTypeColor, R.drawable.ic_steel_type),
    Water(R.string.water_type, waterTypeColor, R.drawable.ic_water_type)
}
