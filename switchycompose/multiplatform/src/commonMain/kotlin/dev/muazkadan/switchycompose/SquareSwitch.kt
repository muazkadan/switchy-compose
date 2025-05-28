package dev.muazkadan.switchycompose

import androidx.compose.runtime.Composable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SquareSwitch(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(2.dp),
    switchValue: Boolean,
    squareColor: Color = MaterialTheme.colorScheme.primaryContainer,
    containerColor: Color = squareColor.copy(alpha = 0.6f),
    onValueChanged: (Boolean) -> Unit,
) {
    var width by remember { (mutableStateOf(0.dp)) }
    var height by remember { (mutableStateOf(ButtonDefaults.MinHeight)) }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    var padding by remember {
        mutableStateOf(0.dp)
    }

    padding = if (switchValue) 0.dp else width - (width / 2)

    val animateSize by animateDpAsState(
        targetValue = if (switchValue) 0.dp else padding,
        tween(
            durationMillis = 333,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    val localDensity = LocalDensity.current
    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = ButtonDefaults.MinHeight * 2,
                minHeight = ButtonDefaults.MinHeight
            )
            .onGloballyPositioned {
                width = with(localDensity) {
                    it.size.width.toDp()
                }
                height = with(localDensity) {
                    it.size.height.toDp()
                }
            }
            .height(height)
            .clip(shape = shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onValueChanged(!switchValue)
            }
    ) {
        Box(
            modifier = Modifier
                .height(height / 2)
                .width(width / 1.3f)
                .clip(shape = shape)
                .background(containerColor)
                .align(Alignment.Center)
        )
        Row {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(animateSize)
                    .background(Color.Transparent)
            )
            Box(
                modifier = Modifier
                    .height(height)
                    .width(width / 2)
                    .clip(shape = shape)
                    .background(squareColor)
            )
        }
    }
}


@Preview
@Composable
private fun SquareSwitchPreview() {
    SquareSwitch(
        switchValue = true,
        onValueChanged = {}
    )
}