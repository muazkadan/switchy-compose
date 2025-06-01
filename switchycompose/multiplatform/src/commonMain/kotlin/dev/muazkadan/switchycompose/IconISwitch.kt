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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable function that creates an iOS-style switch with customizable colors and icons.
 *
 * @param checked The current checked state of the switch.
 * @param onCheckedChange Callback invoked when the switch state changes.
 * @param modifier The modifier to be applied to the switch.
 * @param enabled Whether the switch is enabled and can be interacted with.
 * @param buttonHeight The height of the switch. Default is 40.dp.
 * @param innerPadding Padding inside the switch for the thumb. Default is 3.5.dp.
 * @param shape The shape of the switch. Default is a rounded corner shape with a radius of 45.dp.
 * @param positiveColor The color of the switch when it's in the 'on' state. Default is iOS green color.
 * @param negativeColor The color of the switch when it's in the 'off' state. Default is iOS gray color.
 * @param disabledPositiveColor The color when checked but disabled.
 * @param disabledNegativeColor The color when unchecked and disabled.
 * @param positiveIcon The icon to display in the thumb when the switch is in the 'on' state.
 * @param negativeIcon The icon to display in the thumb when the switch is in the 'off' state.
 * @param interactionSource The MutableInteractionSource representing the stream of interactions with this switch.
 */
@Composable
fun IconISwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonHeight: Dp = 40.dp,
    innerPadding: Dp = 3.5.dp,
    shape: RoundedCornerShape = RoundedCornerShape(45.dp),
    positiveColor: Color = Color(0xFF35C759),
    negativeColor: Color = Color(0xFFE9E9EA),
    disabledPositiveColor: Color = positiveColor.copy(alpha = 0.38f),
    disabledNegativeColor: Color = negativeColor.copy(alpha = 0.38f),
    positiveIcon: ImageVector = Icons.Default.Done,
    negativeIcon: ImageVector = Icons.Default.Close,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    var width by remember { mutableStateOf(0.dp) }

    val thumbOffset by remember(checked, width) {
        derivedStateOf {
            if (checked) width - buttonHeight else 0.dp
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

    val animatedBackgroundColor by animateColorAsState(
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
        label = "switch_bg_color"
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
                minWidth = buttonHeight * 2,
                minHeight = buttonHeight
            )
            .onGloballyPositioned { coordinates ->
                width = with(localDensity) {
                    coordinates.size.width.toDp()
                }
            }
            .height(buttonHeight)
            .clip(shape = shape)
            .background(animatedBackgroundColor)
            .alpha(animatedAlpha)
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
                    .size(buttonHeight)
                    .padding(innerPadding)
                    .shadow(elevation = 5.dp, shape)
                    .clip(shape = shape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (checked) positiveIcon else negativeIcon,
                    contentDescription = null,
                    tint = animatedBackgroundColor
                )
            }
        }
    }
}

/**
 * @deprecated Use [IconISwitch] with checked and onCheckedChange parameters instead.
 */
@Deprecated(
    message = "This function is deprecated. Use IconISwitch with the checked and onCheckedChange parameters instead.",
    replaceWith = ReplaceWith(
        "IconISwitch(checked = switchValue, onCheckedChange = onValueChanged, modifier = modifier, buttonHeight = buttonHeight, innerPadding = innerPadding, shape = shape, positiveColor = positiveColor, negativeColor = negativeColor, positiveIcon = positiveIcon, negativeIcon = negativeIcon)",
        "dev.muazkadan.switchycompose.IconISwitch"
    )
)
@Composable
fun IconISwitch(
    modifier: Modifier = Modifier,
    buttonHeight: Dp = 40.dp,
    innerPadding: Dp = 3.5.dp,
    shape: RoundedCornerShape = RoundedCornerShape(45.dp),
    switchValue: Boolean,
    positiveColor: Color = Color(0xFF35C759),
    negativeColor: Color = Color(0xFFE9E9EA),
    positiveIcon: ImageVector = Icons.Default.Done,
    negativeIcon: ImageVector = Icons.Default.Close,
    onValueChanged: (Boolean) -> Unit,
) {
    IconISwitch(
        checked = switchValue,
        onCheckedChange = onValueChanged,
        modifier = modifier,
        buttonHeight = buttonHeight,
        innerPadding = innerPadding,
        shape = shape,
        positiveColor = positiveColor,
        negativeColor = negativeColor,
        positiveIcon = positiveIcon,
        negativeIcon = negativeIcon
    )
}

@Preview
@Composable
private fun IconISwitchPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var checked1 by remember { mutableStateOf(true) }
        IconISwitch(
            checked = checked1,
            onCheckedChange = { checked1 = it }
        )

        var checked2 by remember { mutableStateOf(false) }
        IconISwitch(
            checked = checked2,
            onCheckedChange = { checked2 = it }
        )

        // Disabled states
        IconISwitch(
            checked = true,
            onCheckedChange = null,
            enabled = false
        )
    }
}