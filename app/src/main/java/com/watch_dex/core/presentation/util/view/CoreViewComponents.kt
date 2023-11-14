package com.watch_dex.core.presentation.util.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListScope
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.watch_dex.core.data.Type
import com.watch_dex.core.domain.dto.PokemonDTO
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private fun String.firstUpper() = this.lowercase().replaceFirstChar { it.uppercase() }

@Composable
private fun RoundedTypeIcon(
    bgColor: Color,
    @DrawableRes iconId: Int,
    description: String
) = Box(
    modifier = Modifier
        .background(color = bgColor, shape = CircleShape)
        .border(width = 1.dp, color = Color.White, shape = CircleShape)
        .padding(4.dp)
) {
    Icon(
        modifier = Modifier.size(8.dp),
        painter = painterResource(id = iconId),
        contentDescription = description
    )
}

@Composable
private fun DetailText(
    text: String,
    fontSize: TextUnit = 10.sp,
    fontWeight: FontWeight = FontWeight.Light,
    fontStyle: FontStyle = FontStyle.Italic
) = Text(
    text = text,
    fontSize = fontSize,
    fontWeight = fontWeight,
    fontStyle = fontStyle
)

@Composable
fun HSpacer(width: Dp) = Spacer(modifier = Modifier.width(width))

@Composable
fun VSpacer(height: Dp) = Spacer(modifier = Modifier.height(height))

@Preview
@Composable
fun NameWithTextPreview() {
    NameWithTypeText(
        number = 22,
        name = "Charizard",
        region = "Galar",
        alternateForm = "Mega X",
        types = listOf(Type.Dragon, Type.Fire),
        themeColor = Type.Dragon.color
    )
}

@Composable
fun NameWithTypeText(
    number: Int,
    name: String,
    region: String?,
    alternateForm: String?,
    types: Collection<Type>,
    themeColor: Color,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(10.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(color = themeColor, shape = shape)
            .border(width = 1.dp, color = Color.White, shape = shape)
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            DetailText(text = "# $number")
            types.forEach {
                RoundedTypeIcon(bgColor = it.color, iconId = it.icon, description = it.name)
            }
        }

        Text(text = name, style = MaterialTheme.typography.body1)

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            region?.let { DetailText(text = it.firstUpper()) }

            if (region != null && alternateForm != null) {
                DetailText(text = "|")
            }

            alternateForm?.let { DetailText(text = it.firstUpper()) }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun WatchDexScalingList(
    modifier: Modifier = Modifier,
    listState: ScalingLazyListState = rememberScalingLazyListState(),
    autoCenteringParams: AutoCenteringParams? = AutoCenteringParams(0),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    focusRequester: FocusRequester = remember { FocusRequester() },
    content: ScalingLazyListScope.() -> Unit
) {
    ScalingLazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .onRotaryScrollEvent {
                coroutineScope.launch { listState.scrollBy(it.verticalScrollPixels) }
                true
            }
            .focusRequester(focusRequester)
            .focusable(),
        autoCentering = autoCenteringParams,
        state = listState,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement,
        content = content
    )
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

@Composable
fun IndicatorScaffold(listState: ScalingLazyListState, content: @Composable () -> Unit) = Scaffold(
    positionIndicator = { PositionIndicator(scalingLazyListState = listState) },
    content = content
)

@Composable
fun ClickableRoundedArea(
    backgroundColor: Color,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Row(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .clip(shape)
            .background(color = backgroundColor, shape = shape)
            .border(width = 1.dp, color = MaterialTheme.colors.onBackground, shape = shape)
            .clickable(onClick = onClick)
            .padding(10.dp, 5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Preview
@Composable
fun ClickableRoundedIconPreview() {
    ClickableRoundedIcon(
        onClick = {},
        bgColor = Type.Grass.color,
        painter = painterResource(id = Type.Grass.icon),
        contentDescription = Type.Grass.name
    )
}

@Composable
fun ClickableRoundedIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    bgColor: Color,
    painter: Painter,
    contentDescription: String?,
) {
    ClickableRoundedArea(backgroundColor = bgColor, onClick = onClick) {
        Icon(
            modifier = modifier.size(15.dp),
            painter = painter,
            tint = MaterialTheme.colors.onBackground,
            contentDescription = contentDescription
        )
    }
}

