package dev.muazkadan.switchycompose

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.HtmlElementView
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event

/**
 * A switch rendered as a native HTML `<input type="checkbox" role="switch" switch>`.
 *
 * Browsers that support the `switch` attribute (such as Safari) draw it as a switch; others
 * draw a checkbox. Assistive technologies announce it as a switch either way.
 *
 * The element is placed over the Compose canvas (see [HtmlElementView]): Compose content can't
 * be drawn on top of it, and it receives input in its area instead of Compose.
 *
 * @param checked The current checked state of the switch.
 * @param onCheckedChange Callback invoked when the user toggles the switch. If null, the switch will be non-interactive.
 * @param modifier The modifier to be applied to the switch.
 * @param enabled Whether the switch is enabled and can be interacted with. Default is true.
 */
@ExperimentalSwitchyApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HtmlSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    // The element isn't reused (no onReset), so it lives exactly as long as this controller
    val controller = remember { HtmlSwitchController() }

    HtmlElementView(
        factory = { controller.container },
        modifier = modifier.size(width = 51.dp, height = 31.dp),
        update = { controller.update(checked, enabled, onCheckedChange) },
    )
}

/**
 * Owns the switch's DOM elements and keeps the `<input>` controlled by Compose: user clicks
 * (including keyboard and assistive activation, which browsers deliver as clicks) don't change
 * it; they request the new value through [onCheckedChange], and the input only changes when
 * [update] is called with the new state.
 */
internal class HtmlSwitchController {

    val input: HTMLInputElement = (document.createElement("input") as HTMLInputElement).apply {
        type = "checkbox"
        setAttribute("role", "switch")
        setAttribute("switch", "")
        style.margin = "0"
        addEventListener("click", ::onClick)
    }

    /** Fills the area Compose gives the interop view and centres the input in it. */
    val container: HTMLDivElement = (document.createElement("div") as HTMLDivElement).apply {
        style.width = "100%"
        style.height = "100%"
        style.display = "flex"
        style.setProperty("align-items", "center")
        style.setProperty("justify-content", "center")
        appendChild(input)
    }

    var onCheckedChange: ((Boolean) -> Unit)? = null
        private set

    private var checked = false

    fun update(checked: Boolean, enabled: Boolean, onCheckedChange: ((Boolean) -> Unit)?) {
        this.checked = checked
        this.onCheckedChange = onCheckedChange
        input.checked = checked
        input.disabled = !enabled
        if (onCheckedChange == null) {
            // Like Material3's Switch, a null callback makes it non-interactive without
            // drawing it as disabled
            input.setAttribute("aria-readonly", "true")
            input.style.cursor = "default"
        } else {
            input.removeAttribute("aria-readonly")
            input.style.cursor = "pointer"
        }
    }

    private fun onClick(event: Event) {
        // Undo the browser's own toggle; the input only changes through update
        event.preventDefault()
        onCheckedChange?.invoke(!checked)
    }
}
