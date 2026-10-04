package dev.muazkadan.switchycompose

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class ColoredSwitchTest {

    @Test
    fun testInitialStateUnchecked() = runComposeUiTest {
        setContent {
            MaterialTheme {
                ColoredSwitch(
                    checked = false,
                    onCheckedChange = {},
                    modifier = Modifier.testTag("switchUnchecked"),
                )
            }
        }
        onNodeWithTag("switchUnchecked").assertIsDisplayed().assertIsOff()
    }

    @Test
    fun testInitialStateChecked() = runComposeUiTest {
        setContent {
            MaterialTheme {
                ColoredSwitch(
                    checked = true,
                    onCheckedChange = {},
                    modifier = Modifier.testTag("switchChecked")
                )
            }
        }
        onNodeWithTag("switchChecked").assertIsDisplayed().assertIsOn()
    }

    @Test
    fun testStateChangeOnClick() = runComposeUiTest {
        setContent {
            MaterialTheme {
                val isChecked = remember { mutableStateOf(false) }
                ColoredSwitch(
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = it },
                    modifier = Modifier.testTag("switch")
                )
            }
        }
        onNodeWithTag("switch").assertIsOff().performClick().assertIsOn()
    }

    @Test
    fun testOnCheckedChangeCallback() = runComposeUiTest {
        var callbackValue = false
        setContent {
            MaterialTheme {
                ColoredSwitch(
                    checked = false,
                    onCheckedChange = { callbackValue = it },
                    modifier = Modifier.testTag("switch")
                )
            }
        }
        onNodeWithTag("switch").performClick()
        assertEquals(true, callbackValue)
    }

    @Test
    fun testDefaultSize() = runComposeUiTest {
        setContent {
            MaterialTheme {
                ColoredSwitch(
                    modifier = Modifier.testTag("switch"),
                    checked = false,
                    onCheckedChange = { }
                )
            }
        }

        onNodeWithTag("switch")
            .assertWidthIsEqualTo(80.dp)
            .assertHeightIsEqualTo(40.dp)
    }

    @Test
    fun testCallerSizeOverridesDefaultSize() = runComposeUiTest {
        setContent {
            MaterialTheme {
                ColoredSwitch(
                    modifier = Modifier
                        .testTag("switch")
                        .size(width = 120.dp, height = 50.dp),
                    checked = true,
                    onCheckedChange = { }
                )
            }
        }

        onNodeWithTag("switch")
            .assertWidthIsEqualTo(120.dp)
            .assertHeightIsEqualTo(50.dp)
    }

    @Test
    fun testWidthStaysStableWhenToggled() = runComposeUiTest {
        assertWidthStableWhenToggled(tag = "switch", density = 1.33125f) { checked, onCheckedChange ->
            ColoredSwitch(
                modifier = Modifier.testTag("switch"),
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }

    @Test
    fun testDisabledStateUnchecked() = runComposeUiTest {
        setContent {
            MaterialTheme {
                ColoredSwitch(
                    checked = false,
                    onCheckedChange = {},
                    enabled = false,
                    modifier = Modifier.testTag("switchDisabledUnchecked")
                )
            }
        }
        onNodeWithTag("switchDisabledUnchecked").assertIsNotEnabled().assertIsOff()
    }

    @Test
    fun testDisabledStateChecked() = runComposeUiTest {
        setContent {
            MaterialTheme {
                ColoredSwitch(
                    checked = true,
                    onCheckedChange = {},
                    enabled = false,
                    modifier = Modifier.testTag("switchDisabledChecked")
                )
            }
        }
        onNodeWithTag("switchDisabledChecked").assertIsNotEnabled().assertIsOn()
    }
}
