package dev.muazkadan.switchycompose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.Composable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A composable function that creates a switch with customizable colors.
 *
 * @param modifier The modifier to be applied to the switch.
 * @param shape The shape of the switch. Default is a rounded corner shape with a radius of 10.dp.
 * @param switchValue The initial value of the switch. If true, the switch is in the 'on' state, otherwise it's in the 'off' state.
 * @param positiveColor The color of the switch when it's in the 'on' state. Default is green.
 * @param negativeColor The color of the switch when it's in the 'off' state. Default is red.
 * @param borderColor The color of the switch's border. Default is the primary container color from the current MaterialTheme color scheme.
 * @param onValueChanged A callback function that is invoked when the switch's value changes. It receives the new value of the switch as a parameter.
 */
@Composable
fun ColoredSwitch(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    switchValue: Boolean,
    positiveColor: Color = Color.Green,
    negativeColor: Color = Color.Red,
    borderColor: Color = MaterialTheme.colorScheme.primaryContainer,
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

    val animateColor by animateColorAsState(
        targetValue = if (switchValue) positiveColor else negativeColor,
        tween(
            durationMillis = 333,
            delayMillis = 0,
            easing = FastOutSlowInEasing
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
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onValueChanged(!switchValue)
            }
    ) {
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
                    .background(animateColor)
            )
        }
    }
}


@Preview
@Composable
private fun ColoredSwitchPreview() {
    ColoredSwitch(
        switchValue = true,
        onValueChanged = {}
    )
}