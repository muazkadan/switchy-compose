package dev.muazkadan.switchycompose

import androidx.compose.runtime.Composable

/**
 * A composable function that provides a platform-specific native switch implementation:
 *
 * - iOS: UIKit's `UISwitch`
 * - Desktop (JVM): a Swing `JToggleButton`
 * - Android, macOS, JS and Wasm: Material3's [androidx.compose.material3.Switch]
 *
 * @param checked The current checked state of the switch.
 * @param onCheckedChange Callback invoked when the user toggles the switch. If null, the switch will be non-interactive.
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