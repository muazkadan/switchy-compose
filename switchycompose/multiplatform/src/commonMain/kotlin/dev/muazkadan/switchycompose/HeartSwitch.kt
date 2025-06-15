package dev.muazkadan.switchycompose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

/**
 * A composable function that creates an interactive switch shaped like a heart.
 * It provides visual feedback through color changes, animations, and an optional scaling effect on press.
 *
 * @param checked The current checked state of the switch.
 * @param onCheckedChange Callback invoked when the switch state changes.
 * @param modifier The modifier to be applied to the switch.
 * @param enabled Whether the switch is enabled and can be interacted with. Default is true.
 * @param positiveColor The fill color of the heart when the switch is in the 'on' (checked) state. Default is a pinkish red.
 * @param negativeColor The fill color of the heart when the switch is in the 'off' (unchecked) state. Default is white.
 * @param disabledPositiveColor The fill color of the heart when the switch is checked but disabled. Defaults to a less opaque version of [positiveColor].
 * @param disabledNegativeColor The fill color of the heart when the switch is unchecked and disabled. Defaults to a less opaque version of [negativeColor].
 * @param checkedBorderColor The border color of the heart when the switch is in the 'on' (checked) state. Default is a darker pinkish red.
 * @param uncheckedBorderColor The border color of the heart when the switch is in the 'off' (unchecked) state. Default is a light gray.
 * @param thumbColor The color of the movable thumb when the switch is in the 'on' (checked) state. Default is white.
 * @param uncheckedThumbColor The color of the movable thumb when the switch is in the 'off' (unchecked) state. Default is a light gray.
 * @param enableScaleAnimation Whether to enable a scaling animation when the switch is pressed. Default is true.
 * @param interactionSource The MutableInteractionSource representing the stream of interactions with this switch.
 */
@Composable
fun HeartSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    positiveColor: Color = Color(0xffff708f),
    negativeColor: Color = Color.White,
    disabledPositiveColor: Color = positiveColor.copy(alpha = 0.38f),
    disabledNegativeColor: Color = negativeColor.copy(alpha = 0.38f),
    checkedBorderColor: Color = Color(0xffff4e74),
    uncheckedBorderColor: Color = Color(0xffd1d1d1),
    thumbColor: Color = Color.White,
    uncheckedThumbColor: Color = Color(0xfff0f0f0),
    enableScaleAnimation: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    // Scale animation for press feedback
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enableScaleAnimation) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale_animation"
    )

    // Animation progress for thumb movement
    val animationProgress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "thumb_movement"
    )

    // Color animations for fill and border
    val fillColor by animateColorAsState(
        targetValue = when {
            !enabled && checked -> disabledPositiveColor
            !enabled && !checked -> disabledNegativeColor
            checked -> positiveColor
            else -> negativeColor
        },
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        ),
        label = "fill_color"
    )

    val borderColor by animateColorAsState(
        targetValue = if (checked) checkedBorderColor else uncheckedBorderColor,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        ),
        label = "border_color"
    )

    val thumbColor by animateColorAsState(
        targetValue = if (checked) thumbColor else uncheckedThumbColor,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        ),
        label = "thumb_color"
    )

    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.38f,
        animationSpec = tween(durationMillis = 150),
        label = "alpha_animation"
    )

    val switchWidth = 56.dp
    val switchHeight = 48.dp
    val thumbSize = 24.dp
    val borderWidth = 2.dp

    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = switchWidth,
                minHeight = switchHeight
            )
            .requiredSize(switchWidth, switchHeight)
            .scale(scale)
            .alpha(alpha)
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
        // Heart track
        Canvas(modifier = Modifier.matchParentSize()) {
            val path = Path().apply { addHeart(size) }
            // Fill
            drawPath(
                path = path,
                color = fillColor
            )
            // Border
            drawPath(
                path = path,
                color = borderColor,
                style = Stroke(width = borderWidth.toPx())
            )
        }

        // Thumb
        Box(
            modifier = Modifier
                .requiredSize(thumbSize)
                .thumbOffset(
                    switchWidth = switchWidth,
                    switchHeight = switchHeight,
                    thumbSize = thumbSize,
                    progress = animationProgress
                )
                .shadow(2.dp, CircleShape)
                .background(thumbColor, CircleShape)
        )
    }
}

// Helper extension to calculate thumb offset along heart path
private fun Modifier.thumbOffset(
    switchWidth: Dp,
    switchHeight: Dp,
    thumbSize: Dp,
    progress: Float
): Modifier = this.then(
    Modifier.offset {
        val width = switchWidth.toPx()
        val height = switchHeight.toPx()

        // Define key positions along the heart path for better balance
        val startOffset = Offset(
            x = width * 0.27f, // Position in left lobe
            y = height * 0.36f  // Upper portion of heart
        )

        val bottomOffset = Offset(
            x = width / 2f,
            y = height * 0.65f  // Near bottom but not at edge
        )

        val endOffset = Offset(
            x = width * 0.73f,  // Position in right lobe
            y = height * 0.36f  // Upper portion of heart
        )

        // Interpolate between positions based on progress
        val currentOffset = when {
            progress <= 0.5f -> {
                val t = progress * 2f
                Offset(
                    x = startOffset.x + (bottomOffset.x - startOffset.x) * t,
                    y = startOffset.y + (bottomOffset.y - startOffset.y) * t
                )
            }

            else -> {
                val t = (progress - 0.5f) * 2f
                Offset(
                    x = bottomOffset.x + (endOffset.x - bottomOffset.x) * t,
                    y = bottomOffset.y + (endOffset.y - bottomOffset.y) * t
                )
            }
        }

        IntOffset(
            x = (currentOffset.x - thumbSize.toPx() / 2).roundToInt(),
            y = (currentOffset.y - thumbSize.toPx() / 2).roundToInt()
        )
    }
)

// Helper extension to draw a heart shaped path based on canvas size
private fun Path.addHeart(size: Size) {
    val width = size.width
    val height = size.height

    // Start at top centre of the heart
    moveTo(width / 2f, height / 5f)

    // Upper left curve
    cubicTo(
        5f * width / 14f, 0f,
        0f, height / 15f,
        width / 28f, 2f * height / 5f
    )

    // Lower left curve
    cubicTo(
        width / 14f, 2f * height / 3f,
        3f * width / 7f, 5f * height / 6f,
        width / 2f, height
    )

    // Lower right curve
    cubicTo(
        4f * width / 7f, 5f * height / 6f,
        13f * width / 14f, 2f * height / 3f,
        27f * width / 28f, 2f * height / 5f
    )

    // Upper right curve back to start
    cubicTo(
        width, height / 15f,
        9f * width / 14f, 0f,
        width / 2f, height / 5f
    )

    close()
}

@Preview
@Composable
fun HeartSwitchPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var checked1 by remember { mutableStateOf(true) }
        HeartSwitch(
            checked = checked1,
            onCheckedChange = { checked1 = it }
        )

        var checked2 by remember { mutableStateOf(false) }
        HeartSwitch(
            checked = checked2,
            onCheckedChange = { checked2 = it },
            thumbColor = Color.Green
        )

        var checked3 by remember { mutableStateOf(true) }
        HeartSwitch(
            checked = checked3,
            onCheckedChange = { checked3 = it },
            positiveColor = Color(0xFFE91E63),
            negativeColor = Color(0xFFF5F5F5),
        )

        // Disabled state
        HeartSwitch(
            checked = true,
            onCheckedChange = null,
            enabled = false
        )

        // Without scale
        var checked4 by remember { mutableStateOf(false) }
        HeartSwitch(
            checked = checked4,
            onCheckedChange = { checked4 = it },
            enableScaleAnimation = false
        )
    }
}