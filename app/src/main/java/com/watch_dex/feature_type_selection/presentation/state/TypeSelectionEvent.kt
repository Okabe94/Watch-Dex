package com.watch_dex.feature_type_selection.presentation.state

import androidx.navigation.NavController

sealed class TypeSelectionEvent {
    object OnClearClick : TypeSelectionEvent()
    class OnDoneClick(val navController: NavController) : TypeSelectionEvent()
    class OnTypeClick(val position: Int) : TypeSelectionEvent()
}
