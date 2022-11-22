package com.watch_dex.core.data.model

import com.watch_dex.core.data.Type

data class PokemonFromList(
    val name: String,
    val typeList : List<Type>
)
