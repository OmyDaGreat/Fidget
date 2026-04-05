package xyz.malefic.fidget

/**
 * Represents the different sizes/families a widget can support.
 * Maps to iOS WidgetFamily and Android Glance widget sizes.
 */
enum class WidgetFamily {
    /**
     * Small widget size.
     * - iOS: systemSmall (2x2 grid on most devices)
     * - Android: Typically 2x2 cells
     */
    Small,

    /**
     * Medium widget size.
     * - iOS: systemMedium (4x2 grid on most devices)
     * - Android: Typically 4x2 cells
     */
    Medium,

    /**
     * Large widget size.
     * - iOS: systemLarge (4x4 grid on most devices)
     * - Android: Typically 4x4 cells
     */
    Large,

    /**
     * Extra large widget size (Android 12+, iOS 15+).
     * - iOS: systemExtraLarge (iPad only)
     * - Android: 4x5 or larger cells
     */
    ExtraLarge,

    /**
     * Rectangular widget for lock screen (iOS 16+).
     * Not applicable on Android.
     */
    AccessoryRectangular,

    /**
     * Circular widget for lock screen (iOS 16+).
     * Not applicable on Android.
     */
    AccessoryCircular,

    /**
     * Inline widget for lock screen (iOS 16+).
     * Not applicable on Android.
     */
    AccessoryInline
}
