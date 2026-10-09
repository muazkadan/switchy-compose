package dev.muazkadan.switchycompose

import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.lang.reflect.InvocationTargetException
import javax.swing.KeyStroke
import javax.swing.SwingUtilities
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

// NativeSwitch on desktop is a SwingPanel, which headless Compose UI tests can't host,
// so these tests drive the Swing button directly on the event dispatch thread.
class ControlledToggleButtonTest {

    // Rethrow failures from the EDT unwrapped, so assertion messages reach the test report
    private fun onEdt(block: () -> Unit) {
        try {
            SwingUtilities.invokeAndWait(block)
        } catch (e: InvocationTargetException) {
            throw e.cause ?: e
        }
    }

    @Test
    fun clickRequestsToggleWithoutChangingSelection() = onEdt {
        val requests = mutableListOf<Boolean>()
        val button = ControlledToggleButton().apply {
            onCheckedChange = { requests += it }
            setChecked(false)
        }

        button.doClick()

        assertEquals(listOf(true), requests)
        assertFalse(button.isSelected, "selection should only follow setChecked")
    }

    @Test
    fun clickWhileCheckedRequestsUncheck() = onEdt {
        val requests = mutableListOf<Boolean>()
        val button = ControlledToggleButton().apply {
            onCheckedChange = { requests += it }
            setChecked(true)
        }

        button.doClick()

        assertEquals(listOf(false), requests)
        assertTrue(button.isSelected)
    }

    @Test
    fun setCheckedDoesNotNotifyCallback() = onEdt {
        val requests = mutableListOf<Boolean>()
        val button = ControlledToggleButton().apply {
            onCheckedChange = { requests += it }
        }

        button.setChecked(true)
        button.setChecked(false)
        button.setChecked(true)

        assertTrue(requests.isEmpty())
        assertTrue(button.isSelected)
    }

    @Test
    fun nullCallbackMakesButtonNonInteractive() = onEdt {
        val button = ControlledToggleButton().apply {
            onCheckedChange = null
            setChecked(false)
        }

        button.doClick()

        assertFalse(button.isSelected)
    }

    @Test
    fun latestCallbackIsUsed() = onEdt {
        val first = mutableListOf<Boolean>()
        val second = mutableListOf<Boolean>()
        val button = ControlledToggleButton().apply {
            onCheckedChange = { first += it }
        }

        button.onCheckedChange = { second += it }
        button.doClick()

        assertTrue(first.isEmpty())
        assertEquals(listOf(true), second)
    }

    @Test
    fun disabledButtonIgnoresClicks() = onEdt {
        val requests = mutableListOf<Boolean>()
        val button = ControlledToggleButton().apply {
            onCheckedChange = { requests += it }
            isEnabled = false
        }

        button.doClick()

        assertTrue(requests.isEmpty())
        assertFalse(button.isSelected)
    }

    @Test
    fun keyboardActivationRequestsToggle() = onEdt {
        val requests = mutableListOf<Boolean>()
        val button = ControlledToggleButton().apply {
            onCheckedChange = { requests += it }
        }
        val inputMap = button.getInputMap()
        val pressed = inputMap.get(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false))
        val released = inputMap.get(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true))

        button.actionMap.get(pressed).actionPerformed(ActionEvent(button, ActionEvent.ACTION_PERFORMED, ""))
        button.actionMap.get(released).actionPerformed(ActionEvent(button, ActionEvent.ACTION_PERFORMED, ""))

        assertEquals(listOf(true), requests)
        assertFalse(button.isSelected)
    }
}
