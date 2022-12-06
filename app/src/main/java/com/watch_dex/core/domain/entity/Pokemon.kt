package com.watch_dex.core.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pokemon(
    val name: String,
    val types: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
