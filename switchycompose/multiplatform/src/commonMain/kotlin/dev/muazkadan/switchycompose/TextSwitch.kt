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
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable function that creates a switch with text labels for both states.
 *
 * @param checked The current checked state of the switch.
 * @param onCheckedChange Callback invoked when the switch state changes.
 * @param modifier The modifier to be applied to the switch.
 * @param enabled Whether the switch is enabled and can be interacted with.
 * @param shape The shape of the switch. Default is a rounded corner shape with a radius of 10.dp.
 * @param color The color of the switch's active background. Default is the primary container color from the current MaterialTheme.
 * @param borderColor The color of the switch's border. Default is the primary container color from the current MaterialTheme.
 * @param textColor The color of the text. Default is the onPrimaryContainer color from the current MaterialTheme color scheme.
 * @param disabledColor The color of the active background when the switch is disabled.
 * @param disabledBorderColor The color of the border when the switch is disabled.
 * @param disabledTextColor The color of the text when the switch is disabled.
 * @param positiveText The text to display on the 'on' side of the switch. Default is "Yes".
 * @param negativeText The text to display on the 'off' side of the switch. Default is "No".
 * @param interactionSource The MutableInteractionSource representing the stream of interactions with this switch.
 */
@Composable
fun TextSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    borderColor: Color = MaterialTheme.colorScheme.primaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    disabledColor: Color = color.copy(alpha = 0.38f),
    disabledBorderColor: Color = borderColor.copy(alpha = 0.38f),
    disabledTextColor: Color = textColor.copy(alpha = 0.38f),
    positiveText: String = "Yes",
    negativeText: String = "No",
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
        targetValue = if (!enabled) disabledColor else color,
        animationSpec = tween(
            durationMillis = 150,
            easing = FastOutSlowInEasing
        ),
        label = "color"
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (!enabled) disabledBorderColor else borderColor,
        animationSpec = tween(
            durationMillis = 150,
            easing = FastOutSlowInEasing
        ),
        label = "border_color"
    )

    val animatedTextColor by animateColorAsState(
        targetValue = if (!enabled) disabledTextColor else textColor,
        animationSpec = tween(
            durationMillis = 150,
            easing = FastOutSlowInEasing
        ),
        label = "text_color"
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
                color = animatedBorderColor,
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
        Row(
            modifier = Modifier
                .fillMaxHeight()
        ) {
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
        Row(
            modifier = Modifier
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = negativeText,
                textAlign = TextAlign.Center,
                color = animatedTextColor
            )
            Text(
                modifier = Modifier.weight(1f),
                text = positiveText,
                textAlign = TextAlign.Center,
                color = animatedTextColor
            )
        }
    }
}

/**
 * @deprecated Use [TextSwitch] with checked and onCheckedChange parameters instead.
 */
@Deprecated(
    message = "This function is deprecated. Use TextSwitch with the checked and onCheckedChange parameters instead.",
    replaceWith = ReplaceWith(
        "TextSwitch(checked = switchValue, onCheckedChange = onValueChanged, modifier = modifier, shape = shape, color = color, borderColor = borderColor, textColor = textColor, positiveText = positiveText, negativeText = negativeText)",
        "dev.muazkadan.switchycompose.TextSwitch"
    )
)
@Composable
fun TextSwitch(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    switchValue: Boolean,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    borderColor: Color = MaterialTheme.colorScheme.primaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    positiveText: String = "Yes",
    negativeText: String = "No",
    onValueChanged: (Boolean) -> Unit,
) {
    TextSwitch(
        checked = switchValue,
        onCheckedChange = onValueChanged,
        modifier = modifier,
        shape = shape,
        color = color,
        borderColor = borderColor,
        textColor = textColor,
        positiveText = positiveText,
        negativeText = negativeText
    )
}

@Preview
@Composable
private fun TextSwitchPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var checked1 by remember { mutableStateOf(true) }
        TextSwitch(
            checked = checked1,
            onCheckedChange = { checked1 = it }
        )

        var checked2 by remember { mutableStateOf(false) }
        TextSwitch(
            checked = checked2,
            onCheckedChange = { checked2 = it },
            positiveText = "ON",
            negativeText = "OFF"
        )

        // Disabled states
        TextSwitch(
            checked = true,
            onCheckedChange = null,
            enabled = false,
            positiveText = "Enabled",
            negativeText = "Disabled"
        )
    }
}