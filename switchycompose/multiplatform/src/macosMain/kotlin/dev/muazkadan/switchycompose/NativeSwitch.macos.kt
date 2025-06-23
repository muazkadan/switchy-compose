package dev.muazkadan.switchycompose

import androidx.compose.material3.Switch

@androidx.compose.runtime.Composable
actual fun NativeSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: androidx.compose.ui.Modifier,
    enabled: Boolean
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled
    )
}