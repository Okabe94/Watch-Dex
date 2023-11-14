package com.watch_dex.feature_type_selection.data.repository

import com.watch_dex.core.data.Type
import com.watch_dex.feature_type_selection.domain.repository.TypeRepository

class TypeRepositoryImpl : TypeRepository {
    override fun getAllTypes() = Type.values().toList()
}
