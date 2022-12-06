package com.watch_dex.core.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TypeBalance(
    @PrimaryKey(autoGenerate = false)
    val id: Char,
    val name: String,
    val relation: String,
)
