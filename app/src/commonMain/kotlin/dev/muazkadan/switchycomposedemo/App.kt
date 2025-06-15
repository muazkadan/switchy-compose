package dev.muazkadan.switchycomposedemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import dev.muazkadan.switchycompose.ColoredSwitch
import dev.muazkadan.switchycompose.CustomISwitch
import dev.muazkadan.switchycompose.CustomSwitch
import dev.muazkadan.switchycompose.ISwitch
import dev.muazkadan.switchycompose.IconISwitch
import dev.muazkadan.switchycompose.HeartSwitch
import dev.muazkadan.switchycompose.MorphingSwitch
import dev.muazkadan.switchycompose.NativeSwitch
import dev.muazkadan.switchycompose.SquareSwitch
import dev.muazkadan.switchycompose.TextSwitch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
internal fun App() {
    MaterialTheme {
        LazyVerticalGrid(
            columns = Adaptive(minSize = 150.dp),
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "TextSwitch")
                    var textSwitchValue by rememberSaveable { mutableStateOf(false) }
                    TextSwitch(
                        checked = textSwitchValue,
                        onCheckedChange = {
                            textSwitchValue = it
                        },
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "ColoredSwitch")
                    var coloredSwitchValue by rememberSaveable { mutableStateOf(false) }
                    ColoredSwitch(
                        checked = coloredSwitchValue,
                        onCheckedChange = {
                            coloredSwitchValue = it
                        },
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "ISwitch")
                    var iSwitchValue by rememberSaveable { mutableStateOf(false) }
                    ISwitch(
                        checked = iSwitchValue,
                        onCheckedChange = {
                            iSwitchValue = it
                        },
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "IconISwitch")
                    var iconISwitchValue by rememberSaveable { mutableStateOf(false) }
                    IconISwitch(
                        checked = iconISwitchValue,
                        onCheckedChange = {
                            iconISwitchValue = it
                        },
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(8.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "CustomISwitch")
                    var customISwitchValue by rememberSaveable { mutableStateOf(false) }
                    CustomISwitch(
                        checked = customISwitchValue,
                        positiveContent = {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = null
                            )
                        },
                        negativeContent = {
                            Text(text = "OFF")
                        },
                        onCheckedChange = {
                            customISwitchValue = it
                        }
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "HeartSwitch")
                    var heartSwitchValue by rememberSaveable { mutableStateOf(false) }
                    HeartSwitch(
                        checked = heartSwitchValue,
                        onCheckedChange = {
                            heartSwitchValue = it
                        }
                    )
                }
            }
            item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "CustomSwitch")
                    var customSwitchValue by rememberSaveable { mutableStateOf(false) }
                    CustomSwitch(
                        checked = customSwitchValue,
                        onCheckedChange = {
                            customSwitchValue = it
                        },
                        positiveContent = {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = null,
                            )
                        },
                        negativeContent = {
                            Text(
                                "False"
                            )
                        }
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Square Switch")
                    var squareSwitchValue by rememberSaveable { mutableStateOf(false) }
                    SquareSwitch(
                        checked = squareSwitchValue,
                        onCheckedChange = {
                            squareSwitchValue = it
                        }
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Native Switch")
                    var nativeSwitchValue by rememberSaveable { mutableStateOf(false) }
                    NativeSwitch(
                        checked = nativeSwitchValue,
                        onCheckedChange = {
                            nativeSwitchValue = it
                        }
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "CustomISwitch")
                    var customISwitchValue by rememberSaveable { mutableStateOf(false) }
                    CustomISwitch(
                        checked = customISwitchValue,
                        positiveContent = {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = null
                            )
                        },
                        negativeContent = {
                            Text(text = "OFF")
                        },
                        onCheckedChange = {
                            customISwitchValue = it
                        }
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "MorphingSwitch")
                    var morphingSwitchValue by rememberSaveable { mutableStateOf(false) }
                    MorphingSwitch(
                        checked = morphingSwitchValue,
                        onCheckedChange = {
                            morphingSwitchValue = it
                        }
                    )
                }
            }
        }
    }
}