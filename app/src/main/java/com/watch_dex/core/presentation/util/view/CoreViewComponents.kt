package com.watch_dex.core.presentation.util.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.watch_dex.core.data.Type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HSpacer(width: Dp) = Spacer(modifier = Modifier.width(width))

@Composable
fun VSpacer(height: Dp) = Spacer(modifier = Modifier.height(height))

@Composable
fun NameWithTypeText(
    name: String,
    types: List<Type>,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(10.dp)
    val colors = if (types.size == 1) {
        val color = types.first().color
        listOf(color, color)
    } else types.map { it.color }
    Text(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Brush.horizontalGradient(colors = colors, tileMode = TileMode.Clamp))
            .border(width = 1.dp, color = Color.White, shape = shape)
            .padding(horizontal = 4.dp, vertical = 15.dp),
        text = name,
        maxLines = 1,
        textAlign = TextAlign.Center,
        color = Color.White,
        style = MaterialTheme.typography.caption3,
    )
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

@Composable
fun ClickableRoundedIcon(
    modifier: Modifier = Modifier,
    bgColor: Color,
    painter: Painter,
    contentDescription: String?,
    onClick: () -> Unit,
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

