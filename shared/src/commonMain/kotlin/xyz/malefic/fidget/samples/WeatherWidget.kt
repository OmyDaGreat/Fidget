package xyz.malefic.fidget.samples

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import xyz.malefic.fidget.*
import kotlin.time.Duration.Companion.minutes

/**
 * Weather widget data model.
 */
data class WeatherData(
    val temperature: Int,
    val condition: String,
    val location: String
) : WidgetData

/**
 * Example weather data provider.
 * In a real app, this would fetch data from a weather API.
 */
class WeatherDataProvider : WidgetDataProvider<WeatherData> {
    override suspend fun provide(): WeatherData {
        // Simulate API call
        return WeatherData(
            temperature = 72,
            condition = "Sunny",
            location = "San Francisco"
        )
    }
}

/**
 * A weather widget that displays current conditions.
 * Demonstrates using a data provider for dynamic content.
 */
@Composable
fun WeatherWidget() = FidgetWidget(
    configuration = WidgetConfig(
        displayName = "Weather",
        description = "Shows current weather conditions",
        supportedFamilies = listOf(
            WidgetFamily.Small,
            WidgetFamily.Medium,
            WidgetFamily.Large
        ),
        updatePolicy = UpdatePolicy.Periodic(interval = 30.minutes)
    ),
    dataProvider = WeatherDataProvider()
) { context ->
    val weather = context.data ?: WeatherData(
        temperature = 0,
        condition = "Loading...",
        location = "Unknown"
    )

    when (context.family) {
        WidgetFamily.Small -> WeatherSmall(weather)
        WidgetFamily.Medium -> WeatherMedium(weather)
        WidgetFamily.Large -> WeatherLarge(weather)
        else -> WeatherSmall(weather)
    }
}

@Composable
private fun WeatherSmall(weather: WeatherData) {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.WbSunny,
                contentDescription = weather.condition,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${weather.temperature}°",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun WeatherMedium(weather: WeatherData) {
    Row(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.WbSunny,
            contentDescription = weather.condition,
            modifier = Modifier.size(64.dp)
        )
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "${weather.temperature}°",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = weather.condition,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = weather.location,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun WeatherLarge(weather: WeatherData) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = weather.location,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Icon(
            imageVector = Icons.Default.WbSunny,
            contentDescription = weather.condition,
            modifier = Modifier.size(96.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${weather.temperature}°",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = weather.condition,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeatherDetail("High", "75°")
            WeatherDetail("Low", "68°")
            WeatherDetail("Humidity", "45%")
        }
    }
}

@Composable
private fun WeatherDetail(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
