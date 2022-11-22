package com.watch_dex.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme
import com.watch_dex.theme.Typography
import com.watch_dex.theme.wearColorPalette

@Composable
fun WearAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = wearColorPalette,
        typography = Typography,
        content = content
    )
}
