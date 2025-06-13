package dev.muazkadan.switchycompose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class MorphingSwitchTest {

    @Test
    fun testInitialStateUnchecked() = runComposeUiTest {
        setContent {
            MaterialTheme {
                MorphingSwitch(
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
                MorphingSwitch(
                    checked = true,
                    onCheckedChange = {},
                    modifier = Modifier.testTag("switchChecked")
                )
            }
        }
        onNodeWithTag("switchChecked").assertIsDisplayed().assertIsOn()
    }

    @Test
    fun testDisabledStateUnchecked() = runComposeUiTest {
        setContent {
            MaterialTheme {
                MorphingSwitch(
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
                MorphingSwitch(
                    checked = true,
                    onCheckedChange = {},
                    enabled = false,
                    modifier = Modifier.testTag("switchDisabledChecked")
                )
            }
        }
        onNodeWithTag("switchDisabledChecked").assertIsNotEnabled().assertIsOn()
    }

    @Test
    fun testStateChangeOnClick() = runComposeUiTest {
        setContent {
            MaterialTheme {
                val isChecked = remember { mutableStateOf(false) }
                MorphingSwitch(
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
                MorphingSwitch(
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
    fun testConfigurationNoPulseAnimation() = runComposeUiTest {
        setContent {
            MaterialTheme {
                val isChecked = remember { mutableStateOf(false) }
                MorphingSwitch(
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = it },
                    enablePulseAnimation = false,
                    modifier = Modifier.testTag("switchNoPulse")
                )
            }
        }
        onNodeWithTag("switchNoPulse").assertIsDisplayed().assertIsOff().performClick().assertIsOn()
    }

    @Test
    fun testConfigurationNoMorphing() = runComposeUiTest {
        setContent {
            MaterialTheme {
                val isChecked = remember { mutableStateOf(false) }
                MorphingSwitch(
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = it },
                    enableMorphing = false,
                    modifier = Modifier.testTag("switchNoMorphing")
                )
            }
        }
        onNodeWithTag("switchNoMorphing").assertIsDisplayed().assertIsOff().performClick().assertIsOn()
    }
}
