package xyz.malefic.fidget

/**
 * Configuration for a Fidget widget.
 *
 * @param displayName The user-facing name of the widget shown in the widget gallery.
 * @param description A brief description of what the widget does.
 * @param supportedFamilies The widget sizes this widget supports.
 * @param updatePolicy How the widget should update its content.
 * @param configurable Whether the widget supports user configuration.
 */
data class WidgetConfig(
    val displayName: String,
    val description: String = "",
    val supportedFamilies: List<WidgetFamily> = listOf(
        WidgetFamily.Small,
        WidgetFamily.Medium,
        WidgetFamily.Large
    ),
    val updatePolicy: UpdatePolicy = UpdatePolicy.Periodic(),
    val configurable: Boolean = false
)
