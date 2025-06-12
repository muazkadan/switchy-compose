package dev.muazkadan.switchycompose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable function that creates an interactive morphing switch with shape transitions,
 * pulsing animations, and dynamic visual feedback.
 *
 * @param checked The current checked state of the switch.
 * @param onCheckedChange Callback invoked when the switch state changes.
 * @param modifier The modifier to be applied to the switch.
 * @param enabled Whether the switch is enabled and can be interacted with.
 * @param switchHeight The height of the switch. Default is 48.dp.
 * @param switchWidth The width of the switch. Default is 80.dp.
 * @param positiveColor The color of the switch when it's in the 'on' state. Default is a vibrant blue.
 * @param negativeColor The color of the switch when it's in the 'off' state. Default is a subtle gray.
 * @param disabledPositiveColor The color when checked but disabled.
 * @param disabledNegativeColor The color when unchecked and disabled.
 * @param thumbColor The color of the thumb. Default is white.
 * @param enablePulseAnimation Whether to enable the pulsing animation effect. Default is true.
 * @param enableMorphing Whether to enable shape morphing animation. Default is true.
 * @param interactionSource The MutableInteractionSource representing the stream of interactions with this switch.
 */
@Composable
fun MorphingSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    switchHeight: Dp = 48.dp,
    switchWidth: Dp = 80.dp,
    positiveColor: Color = Color(0xFF2196F3),
    negativeColor: Color = Color(0xFFE0E0E0),
    disabledPositiveColor: Color = positiveColor.copy(alpha = 0.38f),
    disabledNegativeColor: Color = negativeColor.copy(alpha = 0.38f),
    thumbColor: Color = Color.White,
    enablePulseAnimation: Boolean = true,
    enableMorphing: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Pulsing animation
    val pulseAnimation by animateFloatAsState(
        targetValue = if (enablePulseAnimation && isPressed) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "pulse_animation"
    )
    
    // Morphing animation for shape transition
    val morphingProgress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "morphing_progress"
    )
    
    // Thumb position animation
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) switchWidth - switchHeight else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "thumb_offset"
    )
    
    // Background color animation
    val animatedBackgroundColor by animateColorAsState(
        targetValue = when {
            !enabled && checked -> disabledPositiveColor
            !enabled && !checked -> disabledNegativeColor
            checked -> positiveColor
            else -> negativeColor
        },
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "background_color"
    )
    
    // Alpha animation for disabled state
    val animatedAlpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.5f,
        animationSpec = tween(durationMillis = 200),
        label = "alpha_animation"
    )
    
    // Rotation animation for thumb
    val thumbRotation by animateFloatAsState(
        targetValue = if (checked) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "thumb_rotation"
    )
    
    // Scale animation for thumb
    val thumbScale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "thumb_scale"
    )
    
    // Morphing shape calculation
    val morphingShape = if (enableMorphing) {
        val cornerRadius = lerp(switchHeight / 2, 8.dp, morphingProgress)
        RoundedCornerShape(cornerRadius)
    } else {
        RoundedCornerShape(switchHeight / 2)
    }
    
    Box(
        modifier = modifier
            .size(width = switchWidth, height = switchHeight)
            .scale(pulseAnimation)
            .clip(morphingShape)
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
        // Thumb with morphing and rotation effects
        Box(
            modifier = Modifier
                .size(switchHeight - 8.dp)
                .offset(x = thumbOffset + 4.dp, y = 4.dp)
                .scale(thumbScale)
                .graphicsLayer(
                    rotationZ = thumbRotation,
                    transformOrigin = TransformOrigin(0.5f, 0.5f)
                )
                .clip(CircleShape)
                .background(thumbColor)
                .border(
                    width = 2.dp,
                    color = animatedBackgroundColor.copy(alpha = 0.3f),
                    shape = CircleShape
                )
        ) {
            // Inner decorative element that changes based on state
            Box(
                modifier = Modifier
                    .size(switchHeight / 3)
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .background(
                        if (checked) positiveColor.copy(alpha = 0.7f)
                        else negativeColor.copy(alpha = 0.3f)
                    )
            )
        }
        
        // Ripple effect overlay
        if (isPressed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(morphingShape)
                    .background(
                        Color.White.copy(alpha = 0.2f)
                    )
            )
        }
    }
}

/**
 * Helper function to interpolate between two Dp values
 */
private fun lerp(start: Dp, end: Dp, fraction: Float): Dp {
    return start + (end - start) * fraction
}


@Preview
@Composable
private fun MorphingSwitchPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var checked1 by remember { mutableStateOf(true) }
        MorphingSwitch(
            checked = checked1,
            onCheckedChange = { checked1 = it }
        )

        var checked2 by remember { mutableStateOf(false) }
        MorphingSwitch(
            checked = checked2,
            onCheckedChange = { checked2 = it }
        )

        var checked3 by remember { mutableStateOf(true) }
        MorphingSwitch(
            checked = checked3,
            onCheckedChange = { checked3 = it },
            positiveColor = Color(0xFFE91E63),
            negativeColor = Color(0xFFF5F5F5),
            thumbColor = Color(0xFFFFEB3B)
        )

        // Disabled state
        MorphingSwitch(
            checked = true,
            onCheckedChange = null,
            enabled = false
        )
        
        // Without morphing
        var checked4 by remember { mutableStateOf(false) }
        MorphingSwitch(
            checked = checked4,
            onCheckedChange = { checked4 = it },
            enableMorphing = false
        )
        
        // Without pulse animation
        var checked5 by remember { mutableStateOf(true) }
        MorphingSwitch(
            checked = checked5,
            onCheckedChange = { checked5 = it },
            enablePulseAnimation = false
        )
    }
} 