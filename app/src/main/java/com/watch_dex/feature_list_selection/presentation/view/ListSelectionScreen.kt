@file:OptIn(ExperimentalComposeUiApi::class)

package com.watch_dex.feature_list_selection.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.watch_dex.R
import com.watch_dex.core.data.Type
import com.watch_dex.core.domain.dto.PokemonDTO
import com.watch_dex.core.presentation.util.view.ClickableRoundedIcon
import com.watch_dex.core.presentation.util.view.IndicatorScaffold
import com.watch_dex.core.presentation.util.view.NameWithTypeText
import com.watch_dex.core.presentation.util.view.WatchDexScalingList
import com.watch_dex.feature_list_selection.presentation.event.ListSelectionEvent
import com.watch_dex.feature_list_selection.presentation.state.ListSelectionState
import com.watch_dex.feature_main.presentation.view.MainViewModel

@Composable
fun ListSelectionScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
) {
    val state by viewModel.byListScreenState.collectAsState()
    ListSelectionScreen(
        state = state,
        onLetterClick = { letter -> viewModel.onListEvent(ListSelectionEvent.OnLetterClick(letter)) },
        onCollapseClick = { viewModel.onListEvent(ListSelectionEvent.OnLetterClick(null)) },
        onExpandedClick = { selection ->
            viewModel.onListEvent(ListSelectionEvent.OnPokemonClick(selection))
            navController.popBackStack()
        }
    )
}

@Composable
fun ListSelectionScreen(
    state: ListSelectionState,
    onLetterClick: (Char?) -> Unit,
    onCollapseClick: () -> Unit,
    onExpandedClick: (PokemonDTO) -> Unit
): Unit = with(state) {
    val listState = rememberScalingLazyListState()

    IndicatorScaffold(listState) {

        Box(modifier = Modifier.fillMaxSize()) {

            WatchDexScalingList(
                modifier = Modifier.fillMaxSize(),
                listState = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(initials) { letter ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        InitialLetterChip(letter, theme.color, onLetterClick)
                        ExpandableList(
                            themeColor = theme.color,
                            list = pokemonDisplayed,
                            visible = letterSelected == letter,
                            onSelectFromList = onExpandedClick
                        )
                    }
                }
            }

            CollapseButton(letterSelected, onCollapseClick)

        }
    }
}

@Composable
private fun BoxScope.CollapseButton(letter: Char?, onclick: () -> Unit) {
    AnimatedVisibility(
        modifier = Modifier
            .padding(end = 5.dp)
            .align(Alignment.CenterEnd),
        visible = letter != null,
        enter = slideInHorizontally { it * 2 },
        exit = fadeOut()
    ) {
        ClickableRoundedIcon(
            bgColor = MaterialTheme.colors.primary,
            painter = painterResource(id = R.drawable.ic_compress),
            contentDescription = stringResource(id = R.string.cd_collapse),
            onClick = onclick
        )
    }
}

@Composable
private fun InitialLetterChip(
    letter: Char,
    themeColor: Color,
    onChipClick: (Char) -> Unit
) = Chip(
    colors = ChipDefaults.chipColors(backgroundColor = themeColor),
    modifier = Modifier.fillMaxWidth(),
    label = {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = letter.toString(),
            textAlign = TextAlign.Center,
            color = Color.White,
        )
    },
    onClick = { onChipClick(letter) }
)

@Composable
private fun ColumnScope.ExpandableList(
    themeColor: Color,
    list: List<PokemonDTO>,
    visible: Boolean,
    onSelectFromList: (PokemonDTO) -> Unit
) {
    AnimatedVisibility(visible = visible) {
        WatchDexScalingList(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            autoCenteringParams = null
        ) {
            items(list) { pokemon ->
                SelectablePokemonFromList(
                    themeColor = themeColor,
                    pokemon = pokemon,
                    onClick = onSelectFromList
                )
            }
        }
    }
}

@Composable
private fun SelectablePokemonFromList(
    themeColor: Color,
    pokemon: PokemonDTO,
    onClick: (PokemonDTO) -> Unit
) = NameWithTypeText(
    modifier = Modifier.clickable { onClick(pokemon) },
    number = pokemon.number,
    name = pokemon.name,
    region = pokemon.region,
    alternateForm = pokemon.alternateForm,
    types = pokemon.types,
    themeColor = themeColor
)
