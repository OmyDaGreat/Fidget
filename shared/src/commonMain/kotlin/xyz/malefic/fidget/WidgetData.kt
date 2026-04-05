package xyz.malefic.fidget

/**
 * Base interface for widget data/state.
 * Implement this interface for your widget's data model.
 */
interface WidgetData

/**
 * Provides data for widgets.
 * Implementations should fetch and prepare data for widget rendering.
 */
interface WidgetDataProvider<T : WidgetData> {
    /**
     * Fetches the current data for the widget.
     * @return The widget data, or null if data is unavailable.
     */
    suspend fun provide(): T?
}

/**
 * Empty widget data for widgets that don't need external data.
 */
object EmptyWidgetData : WidgetData
