package xyz.malefic.fidget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIViewController

/**
 * iOS implementation of FidgetWidget.
 *
 * On iOS, widgets are rendered using Compose Multiplatform's native UIView rendering.
 * This implementation uses ComposeUIViewController to create native iOS views with
 * full interactivity support (buttons, gestures, animations, etc.).
 */
@Composable
internal actual fun <T : WidgetData> FidgetWidgetInternal(
    configuration: WidgetConfig,
    dataProvider: WidgetDataProvider<T>?,
    content: @Composable (context: WidgetContext<T>) -> Unit,
) {
    // Register widget with iOS bridge
    remember(configuration.displayName) {
        FidgetWidgetBridge.registerWidget(
            configuration = configuration,
            dataProvider = dataProvider,
            content = content,
        )
    }
}

/**
 * Bridge between Kotlin/Compose and iOS WidgetKit.
 *
 * This object maintains a registry of widgets and provides functions
 * that can be called from Swift to render widget content as native UIViews.
 */
object FidgetWidgetBridge {
    private val widgets = mutableMapOf<String, WidgetEntry<*>>()

    fun <T : WidgetData> registerWidget(
        configuration: WidgetConfig,
        dataProvider: WidgetDataProvider<T>?,
        content: @Composable (context: WidgetContext<T>) -> Unit,
    ) {
        val key = configuration.displayName
        widgets[key] = WidgetEntry(configuration, dataProvider, content)
    }

    /**
     * Get all registered widgets.
     * Called from Swift to enumerate available widgets.
     */
    fun getAllWidgets(): List<WidgetConfiguration> =
        widgets.values.map { entry ->
            WidgetConfiguration(
                displayName = entry.configuration.displayName,
                description = entry.configuration.description,
                supportedFamilies = entry.configuration.supportedFamilies.map { it.toIOSFamily() },
            )
        }

    /**
     * Creates a UIViewController that renders the widget using Compose Multiplatform.
     * This provides full interactivity - buttons, gestures, animations all work!
     *
     * Data fetching happens automatically - this function handles it internally.
     *
     * Called from Swift to create the widget view for WidgetKit.
     *
     * @param widgetName The name of the widget to render
     * @param family The iOS widget family (e.g., "systemSmall", "systemMedium")
     * @return A UIViewController containing the Compose widget, or null if widget not found
     */
    @OptIn(ExperimentalForeignApi::class)
    fun createWidgetViewController(
        widgetName: String,
        family: String,
    ): UIViewController? {
        @Suppress("UNCHECKED_CAST")
        val entry = widgets[widgetName] as? WidgetEntry<WidgetData> ?: return null

        val widgetFamily = parseFamily(family)

        // Create UIViewController with Compose content
        // Data fetching happens automatically inside WidgetContentWithData
        return ComposeUIViewController {
            WidgetContentWithData(
                widgetName = widgetName,
                family = widgetFamily,
                dataProvider = entry.dataProvider,
                content = entry.content,
            )
        }
    }

    /**
     * Fetches widget data asynchronously.
     * This is a suspend function that Swift can await using async/await.
     *
     * Called from Swift timeline provider to get fresh data for timeline entries.
     *
     * @param widgetName The name of the widget
     * @return The widget data, or null if no data provider or fetch failed
     */
    suspend fun getWidgetData(widgetName: String): WidgetData? {
        @Suppress("UNCHECKED_CAST")
        val entry = widgets[widgetName] as? WidgetEntry<WidgetData> ?: return null

        return try {
            entry.dataProvider?.provide()
        } catch (e: Exception) {
            println("Error fetching widget data for $widgetName: ${e.message}")
            null
        }
    }

    /**
     * Get widget configuration by name.
     * Useful for accessing update policies and other metadata from Swift.
     */
    fun getWidgetConfiguration(widgetName: String): WidgetConfiguration? {
        val entry = widgets[widgetName] ?: return null
        return WidgetConfiguration(
            displayName = entry.configuration.displayName,
            description = entry.configuration.description,
            supportedFamilies = entry.configuration.supportedFamilies.map { it.toIOSFamily() },
        )
    }

    private fun parseFamily(family: String): WidgetFamily =
        when (family) {
            "systemSmall" -> WidgetFamily.Small
            "systemMedium" -> WidgetFamily.Medium
            "systemLarge" -> WidgetFamily.Large
            "systemExtraLarge" -> WidgetFamily.ExtraLarge
            "accessoryRectangular" -> WidgetFamily.AccessoryRectangular
            "accessoryCircular" -> WidgetFamily.AccessoryCircular
            "accessoryInline" -> WidgetFamily.AccessoryInline
            else -> WidgetFamily.Medium
        }

    private fun WidgetFamily.toIOSFamily(): String =
        when (this) {
            WidgetFamily.Small -> "systemSmall"
            WidgetFamily.Medium -> "systemMedium"
            WidgetFamily.Large -> "systemLarge"
            WidgetFamily.ExtraLarge -> "systemExtraLarge"
            WidgetFamily.AccessoryRectangular -> "accessoryRectangular"
            WidgetFamily.AccessoryCircular -> "accessoryCircular"
            WidgetFamily.AccessoryInline -> "accessoryInline"
        }

    @Composable
    private fun <T : WidgetData> WidgetContentWithData(
        widgetName: String,
        family: WidgetFamily,
        dataProvider: WidgetDataProvider<T>?,
        content: @Composable (context: WidgetContext<T>) -> Unit,
    ) {
        var data by remember { mutableStateOf<T?>(null) }
        var isLoading by remember { mutableStateOf(dataProvider != null) }

        // Fetch data automatically on composition
        LaunchedEffect(widgetName) {
            if (dataProvider != null) {
                try {
                    data = dataProvider.provide()
                } catch (e: Exception) {
                    println("Error loading widget data: ${e.message}")
                } finally {
                    isLoading = false
                }
            }
        }

        // Show content even while loading (with null data)
        // Widgets can handle loading state themselves
        val context = WidgetContext(family = family, data = data)
        content(context)
    }

    internal data class WidgetEntry<T : WidgetData>(
        val configuration: WidgetConfig,
        val dataProvider: WidgetDataProvider<T>?,
        val content: @Composable (context: WidgetContext<T>) -> Unit,
    )
}

/**
 * Widget configuration exposed to iOS.
 * Contains metadata about the widget for WidgetKit configuration.
 */
data class WidgetConfiguration(
    val displayName: String,
    val description: String,
    val supportedFamilies: List<String>,
)
