package dev.muazkadan.switchycompose

import kotlinx.browser.document
import org.w3c.dom.HTMLStyleElement

/** Whether the browser draws `<input type="checkbox" switch>` as a native switch. */
internal expect fun isNativeSwitchInputSupported(): Boolean

/**
 * Switch styling for [HtmlSwitch] in browsers without a native switch control.
 *
 * It styles the same `<input>`, so it keeps native focus, keyboard and accessibility behaviour.
 * Colours can be overridden with the `--switchy-html-switch-on`, `--switchy-html-switch-off`,
 * `--switchy-html-switch-thumb` and `--switchy-html-switch-focus` custom properties.
 */
internal object HtmlSwitchStyles {

    const val CLASS_NAME = "switchy-html-switch"
    const val STYLE_ELEMENT_ID = "switchy-compose-html-switch-styles"

    // The thumb is a background gradient: ::before/::after aren't reliably rendered on <input>
    private val css = """
        .$CLASS_NAME {
          -webkit-appearance: none;
          appearance: none;
          box-sizing: border-box;
          flex: none;
          width: 40px;
          height: 24px;
          margin: 0;
          border-radius: 12px;
          background-color: var(--switchy-html-switch-off, #c7c7cc);
          background-image: radial-gradient(circle closest-side, var(--switchy-html-switch-thumb, #ffffff) 9px, transparent 10px);
          background-repeat: no-repeat;
          background-size: 24px 24px;
          background-position: left center;
          transition: background-color 150ms ease, background-position 150ms ease;
        }
        .$CLASS_NAME:checked {
          background-color: var(--switchy-html-switch-on, #34c759);
          background-position: right center;
        }
        .$CLASS_NAME:disabled {
          opacity: 0.38;
        }
        .$CLASS_NAME:focus-visible {
          outline: 2px solid var(--switchy-html-switch-focus, Highlight);
          outline-offset: 2px;
        }
        @media (prefers-reduced-motion: reduce) {
          .$CLASS_NAME { transition: none; }
        }
        @media (forced-colors: active) {
          .$CLASS_NAME { -webkit-appearance: auto; appearance: auto; }
        }
    """.trimIndent()

    /** Adds the stylesheet to the document once. */
    fun install() {
        if (document.getElementById(STYLE_ELEMENT_ID) != null) return
        val style = document.createElement("style") as HTMLStyleElement
        style.id = STYLE_ELEMENT_ID
        style.textContent = css
        document.head!!.appendChild(style)
    }
}
