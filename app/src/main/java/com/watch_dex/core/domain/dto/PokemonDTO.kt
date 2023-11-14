package com.watch_dex.core.domain.dto

import com.watch_dex.core.data.Type

data class PokemonDTO(
    val number: Int,
    val generation: Int,
    val name: String,
    val types: List<Type>,
    val region: String? = null,
    val alternateForm: String? = null
)

