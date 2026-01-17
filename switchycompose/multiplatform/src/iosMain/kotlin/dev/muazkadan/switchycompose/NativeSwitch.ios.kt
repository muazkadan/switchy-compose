package dev.muazkadan.switchycompose

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
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

private class SwitchTarget(
    private val onCheckedChange: ((Boolean) -> Unit)?
) : NSObject() {
    @ObjCAction
    fun switchValueChanged(sender: UISwitch) {
        onCheckedChange?.invoke(sender.isOn())
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class, ExperimentalComposeUiApi::class)
@Composable
actual fun NativeSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier,
    enabled: Boolean
) {
    // Remember the target to ensure it's retained properly
    val target = remember(onCheckedChange) {
        SwitchTarget(onCheckedChange)
    }

    UIKitView(
        factory = {
            val switch = UISwitch()

            // Set initial state
            switch.setOn(checked, animated = false)
            switch.setEnabled(enabled)

            // Size the switch to fit its content
            switch.sizeToFit()

            // Add target-action for value changes
            onCheckedChange?.let {
                switch.addTarget(
                    target = target,
                    action = platform.objc.sel_registerName("switchValueChanged:"),
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

            // Update target-action when onCheckedChange changes
            switch.removeTarget(target, action = null, forControlEvents = UIControlEventValueChanged)
            onCheckedChange?.let {
                switch.addTarget(
                    target = target,
                    action = platform.objc.sel_registerName("switchValueChanged:"),
                    forControlEvents = UIControlEventValueChanged
                )
            }
        },
        properties = UIKitInteropProperties(
            placedAsOverlay = true,
        )
    )
}