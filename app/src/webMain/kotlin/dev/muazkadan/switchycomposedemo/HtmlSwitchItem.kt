package dev.muazkadan.switchycomposedemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muazkadan.switchycompose.ExperimentalSwitchyApi
import dev.muazkadan.switchycompose.HtmlSwitch

/** Demo item for the web-only [HtmlSwitch]. */
@OptIn(ExperimentalSwitchyApi::class)
internal fun LazyGridScope.htmlSwitchItem() {
    item {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "HtmlSwitch")
            var htmlSwitchValue by rememberSaveable { mutableStateOf(false) }
            HtmlSwitch(
                checked = htmlSwitchValue,
                onCheckedChange = {
                    htmlSwitchValue = it
                },
                contentDescription = "HtmlSwitch"
            )
        }
    }
}
