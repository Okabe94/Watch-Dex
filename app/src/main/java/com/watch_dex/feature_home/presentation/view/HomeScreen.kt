@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

package com.watch_dex.feature_home.presentation.view

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyListScope
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.watch_dex.R
import com.watch_dex.core.data.Type
import com.watch_dex.core.domain.dto.PokemonDTO
import com.watch_dex.core.presentation.util.view.IndicatorScaffold
import com.watch_dex.core.presentation.util.view.NameWithTypeText
import com.watch_dex.core.presentation.util.view.VSpacer
import com.watch_dex.core.presentation.util.view.WatchDexScalingList
import com.watch_dex.feature_home.domain.Effectiveness
import com.watch_dex.feature_home.presentation.state.HomeEvent
import com.watch_dex.feature_home.presentation.state.HomeState
import com.watch_dex.feature_main.presentation.view.MainViewModel
import com.watch_dex.theme.WearAppTheme
import java.util.EnumMap

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val state by viewModel.homeScreenState.collectAsState()

    HomeScreen(
        state,
        { viewModel.onHomeEvent(HomeEvent.SelectByPokemon(navController)) },
        { viewModel.onHomeEvent(HomeEvent.SelectByType(navController)) },
        { viewModel.onHomeEvent(HomeEvent.SelectOffensive) },
        { viewModel.onHomeEvent(HomeEvent.SelectDefensive) }
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onSelectByPokemon: () -> Unit,
    onSelectByType: () -> Unit,
    onSelectOffensive: () -> Unit,
    onSelectDefensive: () -> Unit
): Unit = with(state) {
    val listState = rememberScalingLazyListState()

    IndicatorScaffold(listState = listState) {

        WatchDexScalingList(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            listState = listState
        ) {

            item {
                Selection(
                    theme = theme,
                    listEnabled = byListEnabled,
                    onSelectByPokemon = onSelectByPokemon,
                    onSelectByType = onSelectByType
                )
            }

            if (typesSelected.isNotEmpty()) {
                detailInfo(
                    theme = theme,
                    pokemonDTO = pokemonDTO,
                    typesSelected = typesSelected,
                    isOffensive = isOffensive,
                    balance = balanceMap,
                    onSelectOffensive = onSelectOffensive,
                    onSelectDefensive = onSelectDefensive
                )
            }
        }
    }
}

@Composable
private fun SideSelectionSection(
    themeColor: Color,
    isOffensive: Boolean,
    onOffensiveClick: () -> Unit,
    onDefensiveClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SideSelectionText(
            themeColor = themeColor,
            labelId = R.string.offensive_side,
            isActive = isOffensive,
            action = onOffensiveClick
        )
        SideSelectionText(
            themeColor = themeColor,
            labelId = R.string.defensive_side,
            isActive = !isOffensive,
            action = onDefensiveClick
        )
    }
}

@Composable
private fun Selection(
    theme: Type,
    listEnabled: Boolean,
    onSelectByPokemon: () -> Unit,
    onSelectByType: () -> Unit,
) {
    TypeSelectionSection(theme, listEnabled, onSelectByPokemon, onSelectByType)
}

private fun ScalingLazyListScope.detailInfo(
    theme: Type,
    pokemonDTO: PokemonDTO?,
    typesSelected: List<Type>,
    isOffensive: Boolean,
    balance: EnumMap<Effectiveness, MutableList<Type>>,
    onSelectOffensive: () -> Unit,
    onSelectDefensive: () -> Unit,
) {
    if (pokemonDTO != null) {
        item {
            NameWithTypeText(
                number = pokemonDTO.number,
                name = pokemonDTO.name,
                region = pokemonDTO.region,
                alternateForm = pokemonDTO.alternateForm,
                types = pokemonDTO.types,
                themeColor = theme.color
            )
        }
    } else {
        item { TypesSection(typesSelected) }
    }

    item {
        SideSelectionSection(
            themeColor = theme.color,
            isOffensive = isOffensive,
            onOffensiveClick = onSelectOffensive,
            onDefensiveClick = onSelectDefensive
        )
    }
    item {
        TypeDetailSection(
            themeColor = theme.color,
            isOffensive = isOffensive,
            balance = balance
        )
    }
}

@Composable
private fun RowScope.SideSelectionText(
    themeColor: Color,
    labelId: Int,
    isActive: Boolean,
    action: () -> Unit
) {
    val verticalPadding = if (isActive) 10.dp else 5.dp
    val shape = RoundedCornerShape(15.dp)
    val inactiveColor = MaterialTheme.colors.background
    Text(
        modifier = Modifier
            .weight(1F)
            .clip(shape)
            .clickable(onClick = action)
            .border(width = 1.dp, color = Color.White, shape = shape)
            .background(if (isActive) themeColor else inactiveColor, shape)
            .padding(vertical = verticalPadding)
            .animateContentSize(),
        text = stringResource(id = labelId),
        style = MaterialTheme.typography.caption3,
        color = MaterialTheme.colors.onBackground,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun TypeSelectionSection(
    theme: Type,
    byPokemonEnabled: Boolean,
    byPokemonClick: () -> Unit,
    byTypeClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PokemonIconButton(
                drawable = R.drawable.ic_base_pokeball,
                color = theme.color,
                enabled = byPokemonEnabled,
                onClick = byPokemonClick
            )
            PokemonIconButton(
                drawable = theme.icon,
                color = theme.color,
                onClick = byTypeClick
            )
        }
    }
}

@Composable
private fun TypesSection(typeIdentities: List<Type>) = Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(4.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        val modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
        typeIdentities.forEach { PokemonTypeText(it, modifier) }
    }
}

@Composable
private fun TypeDetailSection(
    themeColor: Color,
    isOffensive: Boolean,
    balance: EnumMap<Effectiveness, MutableList<Type>>
) = Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceEvenly
) {
    balance.entries.forEach { entry ->
        if (entry.value.isNotEmpty())
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                shape = RoundedCornerShape(5.dp),
                backgroundPainter = ColorPainter(themeColor.copy(alpha = 0.3f)),
                onClick = {}
            ) {
                TypeChartDetail(isOffensive, entry.key, entry.value)
            }
    }
}

@Composable
private fun PokemonIconButton(
    @DrawableRes drawable: Int,
    description: String? = null,
    color: Color = MaterialTheme.colors.primary,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = color)
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = description
        )
    }
}

@Composable
fun PokemonTypeText(pokemonType: Type, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(10.dp)
    Text(
        modifier = modifier
            .clip(shape)
            .background(pokemonType.color)
            .border(width = 1.dp, color = Color.White, shape = shape)
            .padding(horizontal = 4.dp, vertical = 2.dp),
        text = stringResource(id = pokemonType.nameId),
        maxLines = 1,
        textAlign = TextAlign.Center,
        color = Color.White,
        style = MaterialTheme.typography.caption3,
    )
}

@Composable
private fun TypeChartDetail(
    isOffensive: Boolean,
    effectiveness: Effectiveness,
    toDetail: List<Type>
) {
    if (toDetail.isNotEmpty()) {
        val rowSize = 2
        val balanceText = stringResource(
            id = if (isOffensive) R.string.deals_damage
            else R.string.takes_damage,
            effectiveness.multiplier
        )
        Text(
            text = balanceText,
            color = Color.White,
            style = MaterialTheme.typography.body2
        )

        VSpacer(height = 5.dp)

        toDetail.chunked(rowSize).forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterHorizontally)
            ) {
                pair.forEach { type -> PokemonTypeText(type, Modifier.weight(1F)) }
            }
            VSpacer(height = 2.dp)
        }
    }
}

@Composable
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
private fun SelectPokemonTypeSectionPreview() {
    DefaultPreviewSurface { TypesSection(listOf()) }
}

@Composable
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
private fun PokemonIconButtonPreview() {
    val identity = Type.Bug
    DefaultPreviewSurface {
        PokemonIconButton(identity.icon, color = identity.color)
    }
}

@Composable
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
private fun PokemonTypeTextPreview() {
    DefaultPreviewSurface { PokemonTypeText(pokemonType = Type.Dragon) }
}

@Composable
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
private fun TypeDetailPreview() {
    WearAppTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TypeChartDetail(
                isOffensive = true,
                effectiveness = Effectiveness.SuperNotEffective,
                toDetail = listOf(Type.Rock, Type.Ice)
            )
        }
    }
}

@Composable
private fun DefaultPreviewSurface(content: @Composable BoxScope.() -> Unit) = WearAppTheme {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = content
    )
}
