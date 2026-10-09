package dev.muazkadan.switchycompose

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLInputElement
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

// HtmlSwitch is an HtmlElementView, which headless Compose UI tests can't host, so these
// tests drive HtmlSwitchController with real DOM elements in the browser.
class HtmlSwitchControllerTest {

    private lateinit var controller: HtmlSwitchController

    @BeforeTest
    fun setUp() {
        controller = HtmlSwitchController()
        document.body!!.appendChild(controller.container)
    }

    @AfterTest
    fun tearDown() {
        controller.container.remove()
    }

    @Test
    fun rendersAccessibleSwitchInput() {
        val input = controller.input

        assertEquals("checkbox", input.type)
        assertEquals("switch", input.getAttribute("role"))
        assertTrue(input.hasAttribute("switch"))
        assertEquals(controller.container, input.parentElement)
    }

    @Test
    fun clickRequestsToggleWithoutChangingInput() {
        val requests = mutableListOf<Boolean>()
        controller.update(checked = false, enabled = true, onCheckedChange = { requests += it })

        controller.input.click()

        assertEquals(listOf(true), requests)
        assertFalse(controller.input.checked, "input should only follow update")
    }

    @Test
    fun clickWhileCheckedRequestsUncheck() {
        val requests = mutableListOf<Boolean>()
        controller.update(checked = true, enabled = true, onCheckedChange = { requests += it })

        controller.input.click()

        assertEquals(listOf(false), requests)
        assertTrue(controller.input.checked)
    }

    @Test
    fun updateAppliesStateWithoutNotifying() {
        val requests = mutableListOf<Boolean>()

        controller.update(checked = true, enabled = true, onCheckedChange = { requests += it })
        controller.update(checked = false, enabled = true, onCheckedChange = { requests += it })
        controller.update(checked = true, enabled = true, onCheckedChange = { requests += it })

        assertTrue(requests.isEmpty())
        assertTrue(controller.input.checked)
    }

    @Test
    fun latestCallbackIsUsed() {
        val first = mutableListOf<Boolean>()
        val second = mutableListOf<Boolean>()

        controller.update(checked = false, enabled = true, onCheckedChange = { first += it })
        controller.update(checked = false, enabled = true, onCheckedChange = { second += it })
        controller.input.click()

        assertTrue(first.isEmpty())
        assertEquals(listOf(true), second)
    }

    @Test
    fun nullCallbackMakesSwitchNonInteractiveButNotDisabled() {
        controller.update(checked = false, enabled = true, onCheckedChange = null)

        controller.input.click()

        assertFalse(controller.input.checked)
        assertFalse(controller.input.disabled)
        assertEquals("true", controller.input.getAttribute("aria-readonly"))
    }

    @Test
    fun callbackRestoresInteraction() {
        controller.update(checked = false, enabled = true, onCheckedChange = null)
        controller.update(checked = false, enabled = true, onCheckedChange = { })

        assertNull(controller.input.getAttribute("aria-readonly"))
    }

    @Test
    fun disabledSwitchIgnoresClicks() {
        val requests = mutableListOf<Boolean>()
        controller.update(checked = false, enabled = false, onCheckedChange = { requests += it })

        controller.input.click()

        assertTrue(controller.input.disabled)
        assertTrue(requests.isEmpty())
        assertFalse(controller.input.checked)
    }

    @Test
    fun fallbackStylesInputAsSwitch() {
        val fallback = attachedController(nativeSwitchSupported = false)
        val input = fallback.input

        assertTrue(input.classList.contains(HtmlSwitchStyles.CLASS_NAME))
        assertEquals("none", computed(input, "appearance"))
        assertEquals("40px", computed(input, "width"))
        assertEquals("24px", computed(input, "height"))

        fallback.container.remove()
    }

    @Test
    fun fallbackReflectsCheckedState() {
        val fallback = attachedController(nativeSwitchSupported = false)
        val input = fallback.input

        fallback.update(checked = false, enabled = true, onCheckedChange = { })
        val off = computed(input, "background-color") to computed(input, "background-position")
        fallback.update(checked = true, enabled = true, onCheckedChange = { })
        val on = computed(input, "background-color") to computed(input, "background-position")

        assertNotEquals(off.first, on.first, "track colour should change")
        assertNotEquals(off.second, on.second, "thumb should move")

        fallback.container.remove()
    }

    @Test
    fun stylesheetIsInstalledOnce() {
        val first = attachedController(nativeSwitchSupported = false)
        val second = attachedController(nativeSwitchSupported = false)

        assertEquals(
            1,
            document.querySelectorAll("#${HtmlSwitchStyles.STYLE_ELEMENT_ID}").length,
        )

        first.container.remove()
        second.container.remove()
    }

    @Test
    fun nativeSwitchIsLeftUnstyled() {
        val native = attachedController(nativeSwitchSupported = true)

        assertFalse(native.input.classList.contains(HtmlSwitchStyles.CLASS_NAME))

        native.container.remove()
    }

    private fun attachedController(nativeSwitchSupported: Boolean) =
        HtmlSwitchController(nativeSwitchSupported).also {
            // Read final values instead of mid-transition ones
            it.input.style.setProperty("transition", "none")
            document.body!!.appendChild(it.container)
        }

    private fun computed(input: HTMLInputElement, property: String): String =
        window.getComputedStyle(input).getPropertyValue(property)
}
