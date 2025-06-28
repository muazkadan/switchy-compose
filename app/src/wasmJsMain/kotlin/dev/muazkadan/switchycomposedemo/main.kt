package dev.muazkadan.switchycomposedemo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Hide PhoneFrame on smaller screens (phones)
            // Show PhoneFrame only on larger screens (tablets/desktop)
            if (maxWidth >= 412.dp && maxHeight >= 791.dp) {
                // Large screen - show PhoneFrame with App inside
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    PhoneFrame {
                        App()
                    }
                }
            } else {
                // Small screen (phone) - show App directly without PhoneFrame
                App()
            }
        }
    }
}