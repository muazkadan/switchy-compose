package dev.muazkadan.switchycompose

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import kotlin.math.roundToInt

/**
 * Places a thumb horizontally inside its parent, from the start edge ([progress] = 0f)
 * to the end edge ([progress] = 1f).
 *
 * The position is computed in pixels from the parent's width during the placement phase,
 * so the thumb can never change the size of the switch, and animating [progress] does
 * not cause recomposition. Placement is layout-direction aware.
 */
internal fun Modifier.horizontalThumbPosition(progress: () -> Float): Modifier =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints.copy(minWidth = 0))
        val trackWidth = if (constraints.hasBoundedWidth) constraints.maxWidth else placeable.width

        layout(trackWidth, placeable.height) {
            val x = ((trackWidth - placeable.width) * progress()).roundToInt()
            placeable.placeRelative(x, 0)
        }
    }
