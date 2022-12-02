@file:OptIn(ExperimentalComposeUiApi::class)

package com.watch_dex.feature_type_selection.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.*
import com.watch_dex.R
import com.watch_dex.core.data.Type
import com.watch_dex.core.presentation.util.view.ClickableRoundedIcon
import com.watch_dex.core.presentation.util.view.IndicatorScaffold
import com.watch_dex.core.presentation.util.view.WatchDexScalingList
import com.watch_dex.feature_main.presentation.view.MainViewModel
import com.watch_dex.feature_type_selection.presentation.state.TypeSelectionEvent
import com.watch_dex.feature_type_selection.presentation.state.TypeSelectionState
import com.watch_dex.theme.WearAppTheme

@Composable
fun TypeSelectionScreen(viewModel: MainViewModel, navController: NavController) {

    val state by viewModel.byTypeScreenState.collectAsState()

    TypeSelectionScreen(
        state = state,
        onTypeClick = { viewModel.onTypeEvent(TypeSelectionEvent.OnTypeClick(it)) },
        onDoneClick = { viewModel.onTypeEvent(TypeSelectionEvent.OnDoneClick(navController)) },
        onClearClick = { viewModel.onTypeEvent(TypeSelectionEvent.OnClearClick) }
    )
}

@Composable
fun TypeSelectionScreen(
    state: TypeSelectionState,
    onTypeClick: (Int) -> Unit,
    onDoneClick: () -> Unit,
    onClearClick: () -> Unit
) {
    val listState = rememberScalingLazyListState()

    IndicatorScaffold(listState = listState) {

        Box(modifier = Modifier.fillMaxSize()) {

            WatchDexScalingList(
                modifier = Modifier.fillMaxSize(),
                listState = listState,
                verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically)
            ) {

                item {
                    ListHeader {
                        Text(text = stringResource(id = R.string.select_by_type_title))
                    }
                }

                itemsIndexed(state.allTypes) { index, type ->
                    PokemonTypeToggleChip(type, type in state.selected, index, onTypeClick)
                }
            }

            BottomSelectionOptions(state.selected, state.hasMaxAmount, onDoneClick, onClearClick)
        }
    }
}

@Composable
private fun BoxScope.BottomSelectionOptions(
    typesSelected: List<Type>,
    hasMaxAmount: Boolean,
    onConfirm: () -> Unit,
    onClear: () -> Unit,
) {
    AnimatedVisibility(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(end = 10.dp),
        visible = typesSelected.isNotEmpty(),
        enter = slideInHorizontally { it },
        exit = fadeOut()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ConfirmButton(onConfirm)
            ClearButton(hasMaxAmount, onClear)
        }
    }
}

@Composable
private fun ConfirmButton(onClick: () -> Unit) = ClickableRoundedIcon(
    bgColor = MaterialTheme.colors.primaryVariant,
    painter = painterResource(id = R.drawable.ic_check),
    contentDescription = stringResource(id = R.string.cd_select),
    onClick = onClick
)

@Composable
private fun ClearButton(maxAmountReached: Boolean, onClick: () -> Unit) {
    val color = if (maxAmountReached) MaterialTheme.colors.error
    else MaterialTheme.colors.onError
    ClickableRoundedIcon(
        bgColor = color,
        painter = painterResource(id = R.drawable.ic_clear),
        contentDescription = stringResource(id = R.string.cd_clear_all),
        onClick = onClick
    )
}

@Composable
private fun PokemonTypeToggleChip(
    type: Type,
    isChecked: Boolean,
    position: Int,
    onClick: (Int) -> Unit
) {
    ToggleChip(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        checked = isChecked,
        onCheckedChange = { onClick(position) },
        appIcon = {
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(id = type.icon),
                contentDescription = null
            )
        },
        label = {
            Text(
                text = stringResource(id = type.nameId),
                maxLines = 1,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.caption2,
            )
        },
        toggleControl = {
            Icon(
                imageVector = ToggleChipDefaults.checkboxIcon(isChecked),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .wrapContentSize(align = Alignment.Center)
            )
        },
        colors = ToggleChipDefaults.toggleChipColors(
            uncheckedStartBackgroundColor = type.color,
            uncheckedEndBackgroundColor = type.color,
            checkedStartBackgroundColor = MaterialTheme.colors.surface,
            checkedEndBackgroundColor = MaterialTheme.colors.surface,
            checkedToggleControlColor = MaterialTheme.colors.onSurface,
        ),
    )
}

@Composable
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
private fun PokemonTypeToggleChipPreview() {
    WearAppTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PokemonTypeToggleChip(Type.Fairy, true, 0) {}
        }
    }
}
