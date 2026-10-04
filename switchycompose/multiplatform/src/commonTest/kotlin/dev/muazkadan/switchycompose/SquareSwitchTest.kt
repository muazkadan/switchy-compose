package dev.muazkadan.switchycompose

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
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
class SquareSwitchTest {

    private val switchTag = "squareSwitch"

    @Test
    fun testInitialStateUnchecked() = runComposeUiTest {
        setContent {
            MaterialTheme {
                SquareSwitch(
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
                SquareSwitch(
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
                SquareSwitch(
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
        var callbackValue: Boolean? = null
        val checkedState = mutableStateOf(false)
        setContent {
            MaterialTheme {
                SquareSwitch(
                    modifier = Modifier.testTag(switchTag),
                    checked = checkedState.value,
                    onCheckedChange = {
                        callbackValue = it
                        checkedState.value = it
                    }
                )
            }
        }

        // Click to toggle
        onNodeWithTag(switchTag).performClick()
        assertEquals(true, callbackValue)

        // Click again to toggle back
        onNodeWithTag(switchTag).performClick()
        assertEquals(false, callbackValue)
    }

    @Test
    fun testDefaultSize() = runComposeUiTest {
        setContent {
            MaterialTheme {
                SquareSwitch(
                    modifier = Modifier.testTag(switchTag),
                    checked = false,
                    onCheckedChange = { }
                )
            }
        }

        onNodeWithTag(switchTag)
            .assertWidthIsEqualTo(80.dp)
            .assertHeightIsEqualTo(40.dp)
    }

    @Test
    fun testCallerSizeOverridesDefaultSize() = runComposeUiTest {
        setContent {
            MaterialTheme {
                SquareSwitch(
                    modifier = Modifier
                        .testTag(switchTag)
                        .size(width = 120.dp, height = 50.dp),
                    checked = true,
                    onCheckedChange = { }
                )
            }
        }

        onNodeWithTag(switchTag)
            .assertWidthIsEqualTo(120.dp)
            .assertHeightIsEqualTo(50.dp)
    }

    @Test
    fun testWidthStaysStableWhenToggled() = runComposeUiTest {
        assertWidthStableWhenToggled(tag = switchTag, density = 1.33125f) { checked, onCheckedChange ->
            SquareSwitch(
                modifier = Modifier.testTag(switchTag),
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }

    @Test
    fun testDisabledStateUnchecked() = runComposeUiTest {
        val checkedState = mutableStateOf(false)
        var callbackCalled = false
        setContent {
            MaterialTheme {
                SquareSwitch(
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
                SquareSwitch(
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

    @Test
    fun testNullOnCheckedChangeCallback() = runComposeUiTest {
        setContent {
            MaterialTheme {
                SquareSwitch(
                    modifier = Modifier.testTag(switchTag),
                    checked = false,
                    onCheckedChange = null
                )
            }
        }

        // Should be displayed
        // Note: We can't use assertIsOff() because the component doesn't have toggleable
        // semantics when onCheckedChange is null
        onNodeWithTag(switchTag).assertIsDisplayed()

        // Click should be a no-op since callback is null
        onNodeWithTag(switchTag).performClick()

        // Should still be displayed after click
        onNodeWithTag(switchTag).assertIsDisplayed()
    }
}
