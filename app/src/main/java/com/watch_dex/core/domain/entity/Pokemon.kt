package com.watch_dex.core.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.watch_dex.core.data.pokemonTypeFromId
import com.watch_dex.core.domain.dto.PokemonDTO

@Entity(tableName = "pokemon")
data class Pokemon(
    val number: Int,
    val generation: Int,
    val name: String,
    val types: String,
    val region: String? = null,
    val alternateForm: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun toDTO(): PokemonDTO {
        val dtoTypes = types.mapNotNull { pokemonTypeFromId(it) }
        return PokemonDTO(
            number = number,
            generation = generation,
            name = name,
            types = dtoTypes,
            region = region,
            alternateForm = alternateForm
        )
    }
}
