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
class ISwitchTest {

    private val switchTag = "iSwitch"

    @Test
    fun testInitialStateUnchecked() = runComposeUiTest {
        setContent {
            MaterialTheme {
                ISwitch(
                    modifier = Modifier.testTag(switchTag),
                    checked = false,
                    onCheckedChange = { }
                )
            }
        }
        onNodeWithTag(switchTag).assertIsDisplayed().assertIsOff()
    }

    @Test
    fun testInitialStateChecked() = runComposeUiTest {
        setContent {
            MaterialTheme {
                ISwitch(
                    modifier = Modifier.testTag(switchTag),
                    checked = true,
                    onCheckedChange = { }
                )
            }
        }
        onNodeWithTag(switchTag).assertIsDisplayed().assertIsOn()
    }

    @Test
    fun testToggle() = runComposeUiTest {
        val checkedState = mutableStateOf(false)
        setContent {
            MaterialTheme {
                ISwitch(
                    modifier = Modifier.testTag(switchTag),
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it }
                )
            }
        }

        // Initial state: Off
        onNodeWithTag(switchTag).assertIsOff()
        assertEquals(false, checkedState.value)

        // Click to turn On
        onNodeWithTag(switchTag).performClick()
        onNodeWithTag(switchTag).assertIsOn()
        assertEquals(true, checkedState.value)

        // Click to turn Off
        onNodeWithTag(switchTag).performClick()
        onNodeWithTag(switchTag).assertIsOff()
        assertEquals(false, checkedState.value)
    }

    @Test
    fun testOnCheckedChangeCallback() = runComposeUiTest {
        var callbackValue: Boolean? = null // Use nullable to ensure it's set by the callback
        val initialCheckedState = false
        setContent {
            MaterialTheme {
                ISwitch(
                    modifier = Modifier.testTag(switchTag),
                    checked = initialCheckedState,
                    onCheckedChange = { callbackValue = it }
                )
            }
        }

        // Click to toggle
        onNodeWithTag(switchTag).performClick()
        assertEquals(!initialCheckedState, callbackValue)

        // Click again to toggle back
        onNodeWithTag(switchTag).performClick()
        assertEquals(initialCheckedState, callbackValue)
    }

    @Test
    fun testDisabledStateUnchecked() = runComposeUiTest {
        val checkedState = mutableStateOf(false)
        var callbackCalled = false
        setContent {
            MaterialTheme {
                ISwitch(
                    modifier = Modifier.testTag(switchTag),
                    checked = checkedState.value,
                    onCheckedChange = {
                        checkedState.value = it
                        callbackCalled = true
                    },
                    enabled = false
                )
            }
        }

        onNodeWithTag(switchTag).assertIsDisplayed().assertIsNotEnabled().assertIsOff()

        // Attempt to click, should not change state or call callback
        onNodeWithTag(switchTag).performClick()
        onNodeWithTag(switchTag).assertIsOff()
        assertEquals(false, checkedState.value)
        assertEquals(false, callbackCalled)
    }

    @Test
    fun testDisabledStateChecked() = runComposeUiTest {
        val checkedState = mutableStateOf(true)
        var callbackCalled = false
        setContent {
            MaterialTheme {
                ISwitch(
                    modifier = Modifier.testTag(switchTag),
                    checked = checkedState.value,
                    onCheckedChange = {
                        checkedState.value = it
                        callbackCalled = true
                     },
                    enabled = false
                )
            }
        }

        onNodeWithTag(switchTag).assertIsDisplayed().assertIsNotEnabled().assertIsOn()

        // Attempt to click, should not change state or call callback
        onNodeWithTag(switchTag).performClick()
        onNodeWithTag(switchTag).assertIsOn()
        assertEquals(true, checkedState.value)
        assertEquals(false, callbackCalled)
    }
}
