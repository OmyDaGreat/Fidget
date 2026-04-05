package xyz.malefic.fidget

import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Android implementation of FidgetWidget using Jetpack Glance.
 */
@Composable
internal actual fun <T : WidgetData> FidgetWidgetInternal(
    configuration: WidgetConfig,
    dataProvider: WidgetDataProvider<T>?,
    content: @Composable (context: WidgetContext<T>) -> Unit,
) {
    // On Android, we store the configuration and content for use by the GlanceAppWidget
    // This is called during the initial widget setup in the app
    FidgetWidgetRegistry.register(configuration, dataProvider, content)
}

/**
 * Registry for Fidget widgets on Android.
 * Stores widget configurations and content for retrieval by GlanceAppWidget.
 */
internal object FidgetWidgetRegistry {
    private val widgets = mutableMapOf<String, WidgetEntry<*>>()

    fun <T : WidgetData> register(
        configuration: WidgetConfig,
        dataProvider: WidgetDataProvider<T>?,
        content: @Composable (context: WidgetContext<T>) -> Unit,
    ) {
        val key = configuration.displayName
        widgets[key] = WidgetEntry(configuration, dataProvider, content)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : WidgetData> get(key: String): WidgetEntry<T>? = widgets[key] as? WidgetEntry<T>

    fun getAll(): List<WidgetEntry<*>> = widgets.values.toList()

    data class WidgetEntry<T : WidgetData>(
        val configuration: WidgetConfig,
        val dataProvider: WidgetDataProvider<T>?,
        val content: @Composable (context: WidgetContext<T>) -> Unit,
    )
}

/**
 * Base GlanceAppWidget for Fidget widgets.
 * Create a subclass for each widget in your app.
 */
abstract class FidgetGlanceWidget<T : WidgetData>(
    private val widgetKey: String,
) : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: android.content.Context,
        id: GlanceId,
    ) {
        val entry =
            FidgetWidgetRegistry.get<T>(widgetKey)
                ?: error("Widget not registered: $widgetKey")

        val data =
            withContext(Dispatchers.IO) {
                entry.dataProvider?.provide()
            }

        provideContent {
            // Determine the widget family based on size mode
            // For now, default to Medium - can be enhanced with size detection
            val family = WidgetFamily.Medium

            val widgetContext = WidgetContext(family = family, data = data)

            androidx.glance.appwidget.components.Scaffold(
                modifier = GlanceModifier,
                content = {
                    entry.content(widgetContext)
                },
            )
        }
    }
}

/**
 * Base receiver for Fidget widgets.
 * Extend this class in your app for each widget.
 */
abstract class FidgetGlanceReceiver<T : WidgetData>(
    private val widgetKey: String,
) : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = object : FidgetGlanceWidget<T>(widgetKey) {}
}
