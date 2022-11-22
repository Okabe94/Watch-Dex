package com.watch_dex.feature_home.presentation.model

import com.watch_dex.core.data.*

enum class Effectiveness(val multiplier: Float) {
    Immune(IMMUNE),
    SuperEffective(SUPER_EFFECTIVE),
    Effective(EFFECTIVE),
    NotEffective(NOT_EFFECTIVE),
    SuperNotEffective(SUPER_NOT_EFFECTIVE),
}
