package dev.muazkadan.switchycompose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import dev.muazkadan.switchycompose.customization.Customization
import dev.muazkadan.switchycompose.customization.DisabledState
import dev.muazkadan.switchycompose.customization.EnabledState
import dev.muazkadan.switchycompose.customization.ThumbIcon
import kotlin.test.Test
import kotlin.test.assertEquals

class ColoredSwitchTest {

    @get:org.junit.Rule
    val rule = createComposeRule()

    // Define colors at the class level for easier reuse and modification
    private val checkedThumbColor = Color.Green
    private val uncheckedThumbColor = Color.Red
    private val disabledCheckedThumbColor = Color.DarkGray // Adjusted for potential contrast issues
    private val disabledUncheckedThumbColor = Color.Gray // Adjusted for potential contrast issues

    private val trackCheckedColor = Color.LightGray
    private val trackUncheckedColor = Color.LightGray // Assuming same for simplicity
    private val disabledTrackCheckedColor = Color.DimGray
    private val disabledTrackUncheckedColor = Color.DimGray


    private fun assertThumbColor(expectedColor: Color, testTag: String = "switch") {
        val imageBitmap = rule.onNodeWithTag(testTag).captureToImage()
        val pixelMap = imageBitmap.toPixelMap()
        // Check the center pixel of the thumb area.
        // This is an approximation. The exact position might need adjustment based on switch design.
        // For a typical switch, the thumb is a circle within the component.
        // We'll pick a point that's likely within the thumb.
        val centerX = imageBitmap.width / 2
        val centerY = imageBitmap.height / 2
        val actualColor = pixelMap[centerX, centerY]
        assertEquals(expectedColor, actualColor, "Thumb color does not match expected.")
    }


    @Test
    fun testInitialStateUncheckedAndColor() {
        rule.setContent {
            MaterialTheme {
                ColoredSwitch(
                    checked = false,
                    onCheckedChange = {},
                    modifier = Modifier.testTag("switchUncheckedColor"),
                    customization = Customization(
                        enabledState = EnabledState(
                            checkedColor = trackCheckedColor,
                            uncheckedColor = trackUncheckedColor,
                            thumbIcon = ThumbIcon(
                                iconColorEnabled = checkedThumbColor,
                                iconColorDisabled = uncheckedThumbColor,
                            )
                        )
                    )
                )
            }
        }
        rule.onNodeWithTag("switchUncheckedColor").assertIsDisplayed().assertIsOff()
        // The ColoredSwitch uses iconColorDisabled for the thumb when unchecked and enabled
        assertThumbColor(expectedColor = uncheckedThumbColor, testTag = "switchUncheckedColor")
    }

    @Test
    fun testInitialStateCheckedAndColor() {
        rule.setContent {
            MaterialTheme {
                ColoredSwitch(
                    checked = true,
                    onCheckedChange = {},
                    modifier = Modifier.testTag("switchCheckedColor"),
                    customization = Customization(
                        enabledState = EnabledState(
                            checkedColor = trackCheckedColor,
                            uncheckedColor = trackUncheckedColor,
                             thumbIcon = ThumbIcon(
                                iconColorEnabled = checkedThumbColor,
                                iconColorDisabled = uncheckedThumbColor,
                            )
                        )
                    )
                )
            }
        }
        rule.onNodeWithTag("switchCheckedColor").assertIsDisplayed().assertIsOn()
        // The ColoredSwitch uses iconColorEnabled for the thumb when checked and enabled
        assertThumbColor(expectedColor = checkedThumbColor, testTag = "switchCheckedColor")
    }
    @Test
    fun testStateChangeOnClick() {
        rule.setContent {
            MaterialTheme {
                val isChecked = remember { mutableStateOf(false) }
                ColoredSwitch(
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = it },
                    modifier = Modifier.testTag("switch"),
                    customization = Customization(
                        enabledState = EnabledState(
                            checkedColor = trackCheckedColor, // Placeholder, not directly tested here
                            uncheckedColor = trackUncheckedColor, // Placeholder
                            thumbIcon = ThumbIcon(
                                iconColorEnabled = checkedThumbColor,
                                iconColorDisabled = uncheckedThumbColor
                            )
                        )
                    )
                )
            }
        }
        rule.onNodeWithTag("switch").assertIsOff().performClick().assertIsOn()
    }

    @Test
    fun testOnCheckedChangeCallback() {
        var callbackValue = false
        rule.setContent {
            MaterialTheme {
                ColoredSwitch(
                    checked = false,
                    onCheckedChange = { callbackValue = it },
                    modifier = Modifier.testTag("switch"),
                     customization = Customization(
                        enabledState = EnabledState(
                            checkedColor = trackCheckedColor,
                            uncheckedColor = trackUncheckedColor,
                            thumbIcon = ThumbIcon(
                                iconColorEnabled = checkedThumbColor,
                                iconColorDisabled = uncheckedThumbColor
                            )
                        )
                    )
                )
            }
        }
        rule.onNodeWithTag("switch").performClick()
        assertEquals(true, callbackValue)
    }

    @Test
    fun testDisabledStateUncheckedAndColor() {
        rule.setContent {
            MaterialTheme {
                ColoredSwitch(
                    checked = false,
                    onCheckedChange = {},
                    enabled = false,
                    modifier = Modifier.testTag("switchDisabledUnchecked"),
                    customization = Customization(
                        enabledState = EnabledState( // Provide enabled state colors as they are mandatory
                            checkedColor = trackCheckedColor,
                            uncheckedColor = trackUncheckedColor,
                             thumbIcon = ThumbIcon(
                                iconColorEnabled = checkedThumbColor, // Wont be used
                                iconColorDisabled = uncheckedThumbColor // Wont be used
                            )
                        ),
                        disabledState = DisabledState(
                            disabledCheckedColor = disabledTrackCheckedColor,
                            disabledUncheckedColor = disabledTrackUncheckedColor,
                            disabledThumbIcon = ThumbIcon( // Use disabled thumb icon colors
                                iconColorEnabled = disabledCheckedThumbColor, // For checked+disabled
                                iconColorDisabled = disabledUncheckedThumbColor // For unchecked+disabled
                            )
                        )
                    )
                )
            }
        }
        rule.onNodeWithTag("switchDisabledUnchecked").assertIsNotEnabled().assertIsOff()
        assertThumbColor(expectedColor = disabledUncheckedThumbColor, testTag = "switchDisabledUnchecked")
    }

    @Test
    fun testDisabledStateCheckedAndColor() {
        rule.setContent {
            MaterialTheme {
                ColoredSwitch(
                    checked = true,
                    onCheckedChange = {},
                    enabled = false,
                    modifier = Modifier.testTag("switchDisabledChecked"),
                    customization = Customization(
                         enabledState = EnabledState(
                            checkedColor = trackCheckedColor,
                            uncheckedColor = trackUncheckedColor,
                             thumbIcon = ThumbIcon(
                                iconColorEnabled = checkedThumbColor,
                                iconColorDisabled = uncheckedThumbColor
                            )
                        ),
                        disabledState = DisabledState(
                            disabledCheckedColor = disabledTrackCheckedColor,
                            disabledUncheckedColor = disabledTrackUncheckedColor,
                            disabledThumbIcon = ThumbIcon(
                                iconColorEnabled = disabledCheckedThumbColor,
                                iconColorDisabled = disabledUncheckedThumbColor
                            )
                        )
                    )
                )
            }
        }
        rule.onNodeWithTag("switchDisabledChecked").assertIsNotEnabled().assertIsOn()
        assertThumbColor(expectedColor = disabledCheckedThumbColor, testTag = "switchDisabledChecked")
    }
}
