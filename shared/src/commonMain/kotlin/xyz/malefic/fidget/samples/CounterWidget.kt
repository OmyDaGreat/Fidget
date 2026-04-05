package xyz.malefic.fidget.samples

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import xyz.malefic.fidget.FidgetWidget
import xyz.malefic.fidget.WidgetConfig
import xyz.malefic.fidget.WidgetFamily

/**
 * A simple counter widget that displays a number.
 * This demonstrates the most basic widget configuration.
 */
@Composable
fun CounterWidget(count: Int = 0) = FidgetWidget(
    configuration = WidgetConfig(
        displayName = "Counter",
        description = "A simple counter widget",
        supportedFamilies = listOf(WidgetFamily.Small)
    )
) { family ->
    CounterContent(count)
}

@Composable
private fun CounterContent(count: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Counter",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
