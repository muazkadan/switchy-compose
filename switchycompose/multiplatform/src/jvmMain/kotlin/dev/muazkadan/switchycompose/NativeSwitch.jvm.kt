package dev.muazkadan.switchycompose

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.unit.dp
import java.awt.event.ItemEvent
import javax.swing.JToggleButton

@Composable
actual fun NativeSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier,
    enabled: Boolean,
) {
    val rememberedListener = remember(onCheckedChange) { onCheckedChange }

    SwingPanel(
        factory = {
            JToggleButton().apply {
                isSelected = checked
                isEnabled = enabled
                addItemListener { event ->
                    if (event.stateChange == ItemEvent.SELECTED || event.stateChange == ItemEvent.DESELECTED) {
                        rememberedListener?.invoke(isSelected)
                    }
                }
            }
        },
        update = { button ->
            if (button.isSelected != checked) {
                button.isSelected = checked
            }
            button.isEnabled = enabled
        },
        modifier = modifier.size(51.dp, 31.dp)
    )
}