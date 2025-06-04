package dev.muazkadan.switchycompose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable function that creates a switch with customizable colors.
 *
 * @param checked The current checked state of the switch.
 * @param onCheckedChange Callback invoked when the switch state changes.
 * @param modifier The modifier to be applied to the switch.
 * @param enabled Whether the switch is enabled and can be interacted with.
 * @param shape The shape of the switch. Default is a rounded corner shape with a radius of 10.dp.
 * @param positiveColor The color of the switch when it's in the 'on' state. Default is green.
 * @param negativeColor The color of the switch when it's in the 'off' state. Default is red.
 * @param disabledPositiveColor The color when checked but disabled.
 * @param disabledNegativeColor The color when unchecked and disabled.
 * @param borderColor The color of the switch's border. Default is the primary container color from the current MaterialTheme color scheme.
 * @param interactionSource The MutableInteractionSource representing the stream of interactions with this switch.
 */
@Composable
fun ColoredSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    positiveColor: Color = Color.Green,
    negativeColor: Color = Color.Red,
    disabledPositiveColor: Color = positiveColor.copy(alpha = 0.38f),
    disabledNegativeColor: Color = negativeColor.copy(alpha = 0.38f),
    borderColor: Color = MaterialTheme.colorScheme.primaryContainer,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    var width by remember { mutableStateOf(0.dp) }
    var height by remember { mutableStateOf(ButtonDefaults.MinHeight) }

    val thumbOffset by remember(checked, width) {
        derivedStateOf {
            if (checked) width - (width / 2) else 0.dp
        }
    }

    val animatedThumbOffset by animateDpAsState(
        targetValue = thumbOffset,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "thumb_offset"
    )

    val animatedColor by animateColorAsState(
        targetValue = when {
            !enabled && checked -> disabledPositiveColor
            !enabled && !checked -> disabledNegativeColor
            checked -> positiveColor
            else -> negativeColor
        },
        animationSpec = tween(
            durationMillis = 150,
            easing = FastOutSlowInEasing
        ),
        label = "switch_color"
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.38f,
        animationSpec = tween(durationMillis = 150),
        label = "switch_alpha"
    )

    val localDensity = LocalDensity.current
    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = ButtonDefaults.MinHeight * 2,
                minHeight = ButtonDefaults.MinHeight
            )
            .onGloballyPositioned { coordinates ->
                width = with(localDensity) {
                    coordinates.size.width.toDp()
                }
                height = with(localDensity) {
                    coordinates.size.height.toDp()
                }
            }
            .height(height)
            .clip(shape = shape)
            .border(
                width = 1.dp,
                color = borderColor.copy(alpha = animatedAlpha),
                shape = shape
            )
            .then(
                if (onCheckedChange != null) {
                    Modifier.toggleable(
                        value = checked,
                        enabled = enabled,
                        role = Role.Switch,
                        interactionSource = interactionSource,
                        indication = null,
                        onValueChange = onCheckedChange
                    )
                } else {
                    Modifier
                }
            )
    ) {
        Row {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(animatedThumbOffset)
                    .background(Color.Transparent)
            )
            Box(
                modifier = Modifier
                    .height(height)
                    .width(width / 2)
                    .clip(shape = shape)
                    .background(animatedColor)
                    .alpha(animatedAlpha)
            )
        }
    }
}

/**
 * @deprecated Use [ColoredSwitch] with checked and onCheckedChange parameters instead.
 */
@Deprecated(
    message = "This function is deprecated. Use ColoredSwitch with the checked and onCheckedChange parameters instead.",
    replaceWith = ReplaceWith(
        "ColoredSwitch(checked = switchValue, onCheckedChange = onValueChanged, modifier = modifier, shape = shape, positiveColor = positiveColor, negativeColor = negativeColor, borderColor = borderColor)",
        "dev.muazkadan.switchycompose.ColoredSwitch"
    )
)
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
    ColoredSwitch(
        checked = switchValue,
        onCheckedChange = onValueChanged,
        modifier = modifier,
        shape = shape,
        positiveColor = positiveColor,
        negativeColor = negativeColor,
        borderColor = borderColor
    )
}

@Preview
@Composable
private fun ColoredSwitchPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var checked1 by remember { mutableStateOf(true) }
        ColoredSwitch(
            checked = checked1,
            onCheckedChange = { checked1 = it }
        )

        var checked2 by remember { mutableStateOf(false) }
        ColoredSwitch(
            checked = checked2,
            onCheckedChange = { checked2 = it }
        )

        // Disabled states
        ColoredSwitch(
            checked = true,
            onCheckedChange = null,
            enabled = false
        )
    }
}