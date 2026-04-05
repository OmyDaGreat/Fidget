package xyz.malefic.fidget

import androidx.compose.runtime.Composable

/**
 * Context provided to widget content composables.
 *
 * @param family The widget size/family being rendered.
 * @param data The widget's data, if available.
 */
data class WidgetContext<T : WidgetData>(
    val family: WidgetFamily,
    val data: T?
)

/**
 * Creates a Fidget widget with the specified configuration.
 *
 * This is the main entry point for defining widgets. The widget content is a Composable
 * function that receives the widget family and data, allowing you to render different
 * layouts based on size.
 *
 * Example usage:
 * ```kotlin
 * @Composable
 * fun MyWidget() = FidgetWidget(
 *     configuration = WidgetConfig(
 *         displayName = "My Widget",
 *         description = "A sample widget",
 *         supportedFamilies = listOf(WidgetFamily.Small, WidgetFamily.Medium)
 *     )
 * ) { context ->
 *     when (context.family) {
 *         WidgetFamily.Small -> SmallLayout()
 *         WidgetFamily.Medium -> MediumLayout()
 *         else -> SmallLayout()
 *     }
 * }
 * ```
 *
 * @param configuration The widget configuration (name, sizes, update policy, etc.)
 * @param dataProvider Optional data provider for dynamic widget content
 * @param content The composable content of the widget, receiving a WidgetContext
 */
@Composable
fun <T : WidgetData> FidgetWidget(
    configuration: WidgetConfig,
    dataProvider: WidgetDataProvider<T>? = null,
    content: @Composable (context: WidgetContext<T>) -> Unit
) {
    FidgetWidgetInternal(
        configuration = configuration,
        dataProvider = dataProvider,
        content = content
    )
}

/**
 * Overload for widgets without data.
 */
@Composable
fun FidgetWidget(
    configuration: WidgetConfig,
    content: @Composable (family: WidgetFamily) -> Unit
) {
    FidgetWidget<EmptyWidgetData>(
        configuration = configuration,
        dataProvider = null
    ) { context ->
        content(context.family)
    }
}

/**
 * Internal platform-specific implementation of FidgetWidget.
 * This will have expect/actual implementations for Android and iOS.
 */
@Composable
internal expect fun <T : WidgetData> FidgetWidgetInternal(
    configuration: WidgetConfig,
    dataProvider: WidgetDataProvider<T>?,
    content: @Composable (context: WidgetContext<T>) -> Unit
)
