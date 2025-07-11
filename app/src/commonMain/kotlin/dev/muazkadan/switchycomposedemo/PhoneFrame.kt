package dev.muazkadan.switchycomposedemo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PhoneFrame(
    modifier: Modifier = Modifier,
    phoneColor: Color = Color.Black,
    screenColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Phone outer frame with aspect ratio
        Surface(
            modifier = Modifier
                .aspectRatio(0.5f) // Phone aspect ratio (width:height = 1:2)
                .fillMaxHeight(0.8f), // Limit max height to 80% of available space
            shape = RoundedCornerShape(32.dp),
            color = phoneColor,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top speaker/notch area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .width(120.dp)
                            .height(20.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color.Black.copy(alpha = 0.3f)
                    ) {}
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Screen area - this is where your content goes
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = screenColor
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        content()
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom home indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .width(134.dp)
                            .height(4.dp),
                        shape = RoundedCornerShape(2.dp),
                        color = Color.White.copy(alpha = 0.3f)
                    ) {}
                }
            }
        }
    }
}

@Preview
@Composable
private fun MobilePhoneFramePreview() {
    MaterialTheme {
        PhoneFrame {
           App()
        }
    }
}
