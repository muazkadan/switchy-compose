package dev.muazkadan.switchycompose

import androidx.compose.runtime.Composable

/**
 * A composable function that provides a platform-specific native switch implementation.
 * On Android, it uses Material3's Switch component, while on iOS, it uses UIKit's UISwitch.
 *
 * @param checked The current checked state of the switch.
 * @param onCheckedChange Callback invoked when the switch state changes. If null, the switch will be non-interactive.
 * @param modifier The modifier to be applied to the switch.
 * @param enabled Whether the switch is enabled and can be interacted with. Default is true.
 */
@Composable
expect fun NativeSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    enabled: Boolean = true,
)