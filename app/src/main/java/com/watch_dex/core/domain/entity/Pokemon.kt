package com.watch_dex.core.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

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
}
