package com.watch_dex.feature_type_selection.domain.repository

import com.watch_dex.core.data.Type

interface TypeRepository {
    fun getAllTypes(): List<Type>
}
