package dev.muazkadan.switchycompose

import androidx.compose.runtime.Composable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TextSwitch(
    modifier: Modifier = Modifier,
    buttonHeight: Dp = ButtonDefaults.MinHeight,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    switchValue: Boolean,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    borderColor: Color = MaterialTheme.colorScheme.primaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    positiveText: String = "Yes",
    negativeText: String = "No",
    onValueChanged: (Boolean) -> Unit,
) {
    var width by remember { (mutableStateOf(0.dp)) }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    var switchClicked by remember {
        mutableStateOf(switchValue)
    }

    var padding by remember {
        mutableStateOf(0.dp)
    }

    padding = if (switchClicked) 0.dp else width - (width / 2)

    val animateSize by animateDpAsState(
        targetValue = if (switchClicked) 0.dp else padding,
        tween(
            durationMillis = 333,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    val localDensity = LocalDensity.current
    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = buttonHeight * 2,
                minHeight = buttonHeight
            )
            .onGloballyPositioned {
                width = with(localDensity) {
                    it.size.width.toDp()
                }
            }
            .height(buttonHeight)
            .clip(shape = shape)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                switchClicked = !switchClicked
                onValueChanged(switchClicked)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(animateSize)
                    .background(Color.Transparent)
            )
            Box(
                modifier = Modifier
                    .height(buttonHeight)
                    .width(width / 2)
                    .clip(shape = shape)
                    .background(color)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = positiveText,
                textAlign = TextAlign.Center,
                color = textColor
            )
            Text(
                modifier = Modifier.weight(1f),
                text = negativeText,
                textAlign = TextAlign.Center,
                color = textColor
            )
        }
    }
}


@Preview
@Composable
fun TextSwitchPreview() {
    TextSwitch(
        switchValue = true,
        onValueChanged = {}
    )
}