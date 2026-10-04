package dev.muazkadan.switchycompose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Density
import kotlin.test.assertEquals

/**
 * Toggles a switch on and off and asserts that its width never changes.
 *
 * Use a [density] at which the switch's dp sizes don't convert to whole pixels
 * (e.g. 1.33125f, Android's tvdpi), so rounding problems show up (see issue #70).
 */
@OptIn(ExperimentalTestApi::class)
internal fun ComposeUiTest.assertWidthStableWhenToggled(
    tag: String,
    density: Float,
    content: @Composable (checked: Boolean, onCheckedChange: (Boolean) -> Unit) -> Unit,
) {
    setContent {
        var checked by remember { mutableStateOf(false) }
        CompositionLocalProvider(LocalDensity provides Density(density)) {
            MaterialTheme {
                content(checked) { checked = it }
            }
        }
    }

    waitForIdle()
    val initialWidth = onNodeWithTag(tag).fetchSemanticsNode().boundsInRoot.width

    onNodeWithTag(tag).performClick()
    waitForIdle()
    assertEquals(initialWidth, onNodeWithTag(tag).fetchSemanticsNode().boundsInRoot.width, "width after checking")

    onNodeWithTag(tag).performClick()
    waitForIdle()
    assertEquals(initialWidth, onNodeWithTag(tag).fetchSemanticsNode().boundsInRoot.width, "width after unchecking")
}
