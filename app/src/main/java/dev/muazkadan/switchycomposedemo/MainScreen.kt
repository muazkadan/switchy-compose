package dev.muazkadan.switchycomposedemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muazkadan.switchycompose.ColoredSwitch
import dev.muazkadan.switchycompose.ISwitch
import dev.muazkadan.switchycompose.IconISwitch
import dev.muazkadan.switchycompose.TextSwitch

@Composable
fun MainScreen() {
    var switchValue by rememberSaveable { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(
            modifier = Modifier.size(16.dp)
        )
        TextSwitch(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            switchValue = switchValue,
            onValueChanged = {
                switchValue = it
            },
        )
        Spacer(
            modifier = Modifier.size(16.dp)
        )
        ColoredSwitch(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            switchValue = switchValue,
            onValueChanged = {
                switchValue = it
            },
        )
        Spacer(
            modifier = Modifier.size(16.dp)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            ISwitch(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                switchValue = switchValue,
                onValueChanged = {
                    switchValue = it
                },
            )
            IconISwitch(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                switchValue = switchValue,
                onValueChanged = {
                    switchValue = it
                },
            )
        }

    }
}