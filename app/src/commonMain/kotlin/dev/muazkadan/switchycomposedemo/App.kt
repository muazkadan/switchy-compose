package dev.muazkadan.switchycomposedemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muazkadan.switchycompose.ColoredSwitch
import dev.muazkadan.switchycompose.CustomISwitch
import dev.muazkadan.switchycompose.CustomSwitch
import dev.muazkadan.switchycompose.ISwitch
import dev.muazkadan.switchycompose.IconISwitch
import dev.muazkadan.switchycompose.NativeSwitch
import dev.muazkadan.switchycompose.SquareSwitch
import dev.muazkadan.switchycompose.TextSwitch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var switchValue by rememberSaveable { mutableStateOf(false) }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier.size(16.dp)
            )
            Text(text = "TextSwitch")
            TextSwitch(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                checked = switchValue,
                onCheckedChange = {
                    switchValue = it
                },
            )
            Spacer(
                modifier = Modifier.size(16.dp)
            )
            Text(text = "ColoredSwitch")
            ColoredSwitch(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                checked = switchValue,
                onCheckedChange = {
                    switchValue = it
                },
            )
            Spacer(
                modifier = Modifier.size(16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "ISwitch")
                Text(text = "IconISwitch")
                Text(text = "CustomISwitch")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ISwitch(
                    checked = switchValue,
                    onCheckedChange = {
                        switchValue = it
                    },
                )
                IconISwitch(
                    checked = switchValue,
                    onCheckedChange = {
                        switchValue = it
                    },
                )
                CustomISwitch(
                    checked = switchValue,
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
                        switchValue = it
                    }
                )
            }
            Spacer(
                modifier = Modifier.size(16.dp)
            )
            Text(text = "CustomSwitch")
            CustomSwitch(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                checked = switchValue,
                onCheckedChange = {
                    switchValue = it
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
            Spacer(
                modifier = Modifier.size(16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Square Switch")
                Text(text = "Native Switch")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SquareSwitch(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    checked = switchValue,
                    onCheckedChange = {
                        switchValue = it
                    }
                )
                NativeSwitch(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    checked = switchValue,
                    onCheckedChange = {
                        switchValue = it
                    }
                )
            }

        }
    }
}