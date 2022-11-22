package com.watch_dex.feature_type_selection.presentation.model

import com.watch_dex.core.data.Type

data class TypeSelectionListItem(
    val type: Type,
    var isChecked: Boolean = false
)
