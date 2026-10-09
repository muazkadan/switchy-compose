package dev.muazkadan.switchycompose

import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UISwitch
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

// NativeSwitch on iOS is a UIKitView, which headless Compose UI tests can't host,
// so these tests drive NativeSwitchController with a real UISwitch.
class NativeSwitchControllerTest {

    private fun attachedSwitch(controller: NativeSwitchController) =
        UISwitch().also { controller.attach(it) }

    @Test
    fun valueChangeForwardsToLatestCallback() {
        val controller = NativeSwitchController()
        val switch = attachedSwitch(controller)
        val first = mutableListOf<Boolean>()
        val second = mutableListOf<Boolean>()

        controller.update(switch, checked = false, enabled = true, onCheckedChange = { first += it })
        controller.update(switch, checked = false, enabled = true, onCheckedChange = { second += it })
        switch.setOn(true, animated = false)
        controller.switchValueChanged(switch)

        assertTrue(first.isEmpty())
        assertEquals(listOf(true), second)
    }

    @Test
    fun callbackChangesKeepASingleTarget() {
        val controller = NativeSwitchController()
        val switch = attachedSwitch(controller)

        repeat(3) {
            controller.update(switch, checked = false, enabled = true, onCheckedChange = { })
        }

        assertEquals(1, switch.allTargets.size)
        assertEquals(
            listOf("switchValueChanged:"),
            switch.actionsForTarget(controller, UIControlEventValueChanged)
        )
    }

    @Test
    fun nullCallbackMakesSwitchNonInteractiveButNotDisabled() {
        val controller = NativeSwitchController()
        val switch = attachedSwitch(controller)

        controller.update(switch, checked = true, enabled = true, onCheckedChange = null)

        assertFalse(switch.isUserInteractionEnabled())
        assertTrue(switch.isEnabled())
    }

    @Test
    fun callbackRestoresInteraction() {
        val controller = NativeSwitchController()
        val switch = attachedSwitch(controller)

        controller.update(switch, checked = false, enabled = true, onCheckedChange = null)
        controller.update(switch, checked = false, enabled = true, onCheckedChange = { })

        assertTrue(switch.isUserInteractionEnabled())
    }

    @Test
    fun updateAppliesCheckedAndEnabledWithoutNotifying() {
        val controller = NativeSwitchController()
        val switch = attachedSwitch(controller)
        val requests = mutableListOf<Boolean>()

        controller.update(switch, checked = true, enabled = false, onCheckedChange = { requests += it })

        assertTrue(switch.isOn())
        assertFalse(switch.isEnabled())
        assertTrue(requests.isEmpty())
    }
}
