package xyz.malefic.fidget.samples

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import xyz.malefic.fidget.FidgetWidget
import xyz.malefic.fidget.UpdatePolicy
import xyz.malefic.fidget.WidgetConfig
import xyz.malefic.fidget.WidgetFamily
import kotlin.time.Duration.Companion.minutes

/**
 * A clock widget that displays the current time.
 * Demonstrates multi-size support and periodic updates.
 */
@Composable
fun ClockWidget() = FidgetWidget(
    configuration = WidgetConfig(
        displayName = "Clock",
        description = "Shows the current time",
        supportedFamilies = listOf(
            WidgetFamily.Small,
            WidgetFamily.Medium
        ),
        updatePolicy = UpdatePolicy.Periodic(interval = 1.minutes)
    )
) { family ->
    when (family) {
        WidgetFamily.Small -> ClockSmall()
        WidgetFamily.Medium -> ClockMedium()
        else -> ClockSmall()
    }
}

@Composable
private fun ClockSmall() {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = "Clock",
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "12:30",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ClockMedium() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = "Clock",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "12:30",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "PM",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Monday, April 5",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
