package com.watch_dex.feature_type_selection.presentation.state

import com.watch_dex.core.data.Type

data class TypeSelectionState(
    val selected : List<Type> = emptyList(),
    val hasMaxAmount: Boolean = false,
    val allTypes: List<Type> = emptyList()
)
