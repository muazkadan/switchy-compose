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
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UISwitch
import platform.darwin.NSObject
import platform.objc.sel_registerName

/**
 * Bridges a [UISwitch] to Compose. One instance stays attached to the switch for its whole
 * lifetime and always forwards to the latest [onCheckedChange].
 */
@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class)
internal class NativeSwitchController : NSObject() {

    var onCheckedChange: ((Boolean) -> Unit)? = null

    fun attach(switch: UISwitch) {
        switch.addTarget(
            target = this,
            action = sel_registerName("switchValueChanged:"),
            forControlEvents = UIControlEventValueChanged
        )
    }

    fun update(
        switch: UISwitch,
        checked: Boolean,
        enabled: Boolean,
        onCheckedChange: ((Boolean) -> Unit)?,
    ) {
        this.onCheckedChange = onCheckedChange
        if (switch.isOn() != checked) {
            switch.setOn(checked, animated = true)
        }
        switch.setEnabled(enabled)
        // Like Material3's Switch, a null callback makes the switch non-interactive
        // without drawing it as disabled
        switch.setUserInteractionEnabled(onCheckedChange != null)
    }

    @ObjCAction
    fun switchValueChanged(sender: UISwitch) {
        onCheckedChange?.invoke(sender.isOn())
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun NativeSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier,
    enabled: Boolean
) {
    // Retained for the switch's lifetime; UIKit only holds a weak reference to targets
    val controller = remember { NativeSwitchController() }

    UIKitView(
        factory = {
            UISwitch().apply {
                setOn(checked, animated = false)
                controller.attach(this)
            }
        },
        modifier = modifier
            // Default intrinsic size of UISwitch (51x31 points)
            .size(width = 51.dp, height = 31.dp),
        update = { switch ->
            controller.update(switch, checked, enabled, onCheckedChange)
        },
        properties = UIKitInteropProperties(
            placedAsOverlay = true,
        )
    )
}
