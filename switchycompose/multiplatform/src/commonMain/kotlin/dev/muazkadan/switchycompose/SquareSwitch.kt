package dev.muazkadan.switchycompose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
 * A composable function that creates a square-style switch with customizable colors.
 *
 * @param checked The current checked state of the switch.
 * @param onCheckedChange Callback invoked when the switch state changes.
 * @param modifier The modifier to be applied to the switch.
 * @param enabled Whether the switch is enabled and can be interacted with.
 * @param shape The shape of the switch. Default is a rounded corner shape with a radius of 2.dp.
 * @param squareColor The color of the square. Default is the primary container color from the current MaterialTheme.
 * @param containerColor The color of the switch container. Default is the square color with reduced alpha.
 * @param disabledSquareColor The color of the square when the switch is disabled.
 * @param disabledContainerColor The color of the container when the switch is disabled.
 * @param interactionSource The MutableInteractionSource representing the stream of interactions with this switch.
 */
@Composable
fun SquareSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(2.dp),
    squareColor: Color = MaterialTheme.colorScheme.primaryContainer,
    containerColor: Color = squareColor.copy(alpha = 0.6f),
    disabledSquareColor: Color = squareColor.copy(alpha = 0.38f),
    disabledContainerColor: Color = containerColor.copy(alpha = 0.38f),
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

    val animatedSquareColor by animateColorAsState(
        targetValue = if (!enabled) disabledSquareColor else squareColor,
        animationSpec = tween(
            durationMillis = 150,
            easing = FastOutSlowInEasing
        ),
        label = "square_color"
    )

    val animatedContainerColor by animateColorAsState(
        targetValue = if (!enabled) disabledContainerColor else containerColor,
        animationSpec = tween(
            durationMillis = 150,
            easing = FastOutSlowInEasing
        ),
        label = "container_color"
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
        Box(
            modifier = Modifier
                .height(height / 2)
                .width(width / 1.3f)
                .clip(shape = shape)
                .background(animatedContainerColor)
                .alpha(animatedAlpha)
                .align(Alignment.Center)
        )
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
                    .background(animatedSquareColor)
                    .alpha(animatedAlpha)
            )
        }
    }
}

/**
 * @deprecated Use [SquareSwitch] with checked and onCheckedChange parameters instead.
 */
@Deprecated(
    message = "This function is deprecated. Use SquareSwitch with the checked and onCheckedChange parameters instead.",
    replaceWith = ReplaceWith(
        "SquareSwitch(checked = switchValue, onCheckedChange = onValueChanged, modifier = modifier, shape = shape, squareColor = squareColor, containerColor = containerColor)",
        "dev.muazkadan.switchycompose.SquareSwitch"
    )
)
@Composable
fun SquareSwitch(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(2.dp),
    switchValue: Boolean,
    squareColor: Color = MaterialTheme.colorScheme.primaryContainer,
    containerColor: Color = squareColor.copy(alpha = 0.6f),
    onValueChanged: (Boolean) -> Unit,
) {
    SquareSwitch(
        checked = switchValue,
        onCheckedChange = onValueChanged,
        modifier = modifier,
        shape = shape,
        squareColor = squareColor,
        containerColor = containerColor
    )
}

@Preview
@Composable
private fun SquareSwitchPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var checked1 by remember { mutableStateOf(true) }
        SquareSwitch(
            checked = checked1,
            onCheckedChange = { checked1 = it }
        )

        var checked2 by remember { mutableStateOf(false) }
        SquareSwitch(
            checked = checked2,
            onCheckedChange = { checked2 = it }
        )

        // Disabled states
        SquareSwitch(
            checked = true,
            onCheckedChange = null,
            enabled = false
        )
    }
}