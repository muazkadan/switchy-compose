package dev.muazkadan.switchycompose

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.unit.dp
import javax.swing.JToggleButton

@Composable
actual fun NativeSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier,
    enabled: Boolean,
) {
    SwingPanel(
        factory = { ControlledToggleButton() },
        update = { button ->
            button.onCheckedChange = onCheckedChange
            button.setChecked(checked)
            button.isEnabled = enabled
        },
        modifier = modifier.size(51.dp, 31.dp)
    )
}

/**
 * A [JToggleButton] whose selection is controlled by Compose, like Material3's Switch.
 *
 * User interaction (mouse, keyboard, accessibility) doesn't change the selection. It requests
 * the new value through [onCheckedChange], and the button only changes when [setChecked] is
 * called with the new state. With a null [onCheckedChange] the button can't be toggled.
 */
internal class ControlledToggleButton : JToggleButton() {

    var onCheckedChange: ((Boolean) -> Unit)? = null

    private val controlledModel = ControlledToggleModel()

    init {
        model = controlledModel
    }

    /** Shows [checked] without notifying [onCheckedChange]. */
    fun setChecked(checked: Boolean) {
        controlledModel.applySelected(checked)
    }

    private inner class ControlledToggleModel : ToggleButtonModel() {
        // Every user-driven toggle reaches the model through setSelected
        override fun setSelected(b: Boolean) {
            if (b != isSelected) {
                onCheckedChange?.invoke(b)
            }
        }

        fun applySelected(b: Boolean) {
            super.setSelected(b)
        }
    }
}
