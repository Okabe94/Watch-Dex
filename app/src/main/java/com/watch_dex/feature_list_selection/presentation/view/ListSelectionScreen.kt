@file:OptIn(ExperimentalComposeUiApi::class)

package com.watch_dex.feature_list_selection.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.*
import com.watch_dex.R
import com.watch_dex.core.data.model.PokemonFromList
import com.watch_dex.core.presentation.util.view.WatchDexScalingList
import com.watch_dex.core.presentation.util.view.ClickableRoundedIcon
import com.watch_dex.core.presentation.util.view.IndicatorScaffold
import com.watch_dex.core.presentation.util.view.NameWithTypeText
import com.watch_dex.feature_list_selection.presentation.event.ListSelectionEvent
import com.watch_dex.feature_list_selection.presentation.state.ListSelectionState
import com.watch_dex.feature_main.presentation.view.MainViewModel

@Composable
fun ListSelectionScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val state by viewModel.pokemonState.collectAsState()
    ListSelectionScreen(
        state = state,
        onLetterClick = { letter -> viewModel.onEvent(ListSelectionEvent.OnLetterClick(letter)) },
        onCollapseClick = { viewModel.onEvent(ListSelectionEvent.OnLetterClick(null)) },
        onExpandedClick = { selection ->
            viewModel.onEvent(ListSelectionEvent.OnPokemonClick(selection))
            navController.popBackStack()
        }
    )
}

@Composable
fun ListSelectionScreen(
    state: ListSelectionState,
    onLetterClick: (Char?) -> Unit,
    onCollapseClick: () -> Unit,
    onExpandedClick: (PokemonFromList) -> Unit
): Unit = with(state) {
    val listState = rememberScalingLazyListState()

    IndicatorScaffold(listState) {

        Box(modifier = Modifier.fillMaxSize()) {

            WatchDexScalingList(
                modifier = Modifier.fillMaxSize(),
                listState = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                item { ListLegend() }
                items(initials) { letter ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        InitialLetterChip(letter, onLetterClick)
                        ExpandableList(
                            pokemonDisplayed,
                            letterSelected == letter,
                            onExpandedClick
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
private fun ListLegend() = Card(onClick = {}) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = stringResource(id = R.string.list_legend),
        color = MaterialTheme.colors.onBackground,
        style = MaterialTheme.typography.caption3
    )
}

@Composable
private fun InitialLetterChip(letter: Char, onChipClick: (Char) -> Unit) = Chip(
    modifier = Modifier.fillMaxWidth(),
    label = {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = letter.toString(),
            textAlign = TextAlign.Center
        )
    },
    onClick = { onChipClick(letter) }
)

@Composable
private fun ColumnScope.ExpandableList(
    list: List<PokemonFromList>,
    visible: Boolean,
    onSelectFromList: (PokemonFromList) -> Unit
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
                SelectablePokemonFromList(pokemon, onSelectFromList)
            }
        }
    }
}

@Composable
private fun SelectablePokemonFromList(
    pokemon: PokemonFromList,
    onClick: (PokemonFromList) -> Unit
) = NameWithTypeText(
    name = pokemon.name,
    types = pokemon.typeList,
    modifier = Modifier.clickable { onClick(pokemon) })
