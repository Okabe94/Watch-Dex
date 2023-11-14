package com.watch_dex.feature_home.domain

import com.watch_dex.core.domain.EFFECTIVE
import com.watch_dex.core.domain.IMMUNE
import com.watch_dex.core.domain.NOT_EFFECTIVE
import com.watch_dex.core.domain.SUPER_EFFECTIVE
import com.watch_dex.core.domain.SUPER_NOT_EFFECTIVE

enum class Effectiveness(val multiplier: Float) {
    Immune(IMMUNE),
    SuperEffective(SUPER_EFFECTIVE),
    Effective(EFFECTIVE),
    NotEffective(NOT_EFFECTIVE),
    SuperNotEffective(SUPER_NOT_EFFECTIVE),
}
