package dev.muazkadan.switchycompose

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.UIKit.UISwitch
import platform.UIKit.UIControlEventValueChanged
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
actual fun NativeSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier,
    enabled: Boolean
) {
    UIKitView(
        factory = {
            val switch = UISwitch()

            // Create a target object to handle the callback
            val target = object : NSObject() {
                @ObjCAction
                fun switchValueChanged() {
                    onCheckedChange?.invoke(switch.isOn())
                }
            }

            // Set initial state
            switch.setOn(checked, animated = false)
            switch.setEnabled(enabled)

            // Size the switch to fit its content
            switch.sizeToFit()

            // Add target-action for value changes
            onCheckedChange?.let {
                switch.addTarget(
                    target = target,
                    action = platform.objc.sel_registerName("switchValueChanged"),
                    forControlEvents = UIControlEventValueChanged
                )
            }

            switch
        },
        modifier = modifier
            // Provide default intrinsic size for UISwitch (51x31 points)
            .size(width = 51.dp, height = 31.dp),
        update = { view ->
            val switch = view

            // Update switch state if it differs from current state
            if (switch.isOn() != checked) {
                switch.setOn(checked, animated = true)
            }

            // Update enabled state
            switch.setEnabled(enabled)
        },
        properties = UIKitInteropProperties(
            isInteractive = true,
            isNativeAccessibilityEnabled = true
        )
    )
}