package xyz.malefic.fidget

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * Defines how and when a widget should update its content.
 */
sealed class UpdatePolicy {
    /**
     * Widget updates at fixed intervals.
     * @param interval The duration between updates.
     */
    data class Periodic(val interval: Duration = 15.minutes) : UpdatePolicy()

    /**
     * Widget updates at specific times of day.
     * @param updateTimes List of times (in minutes since midnight) to update.
     */
    data class Timeline(val updateTimes: List<Int>) : UpdatePolicy()

    /**
     * Widget updates only when explicitly triggered by the app.
     */
    data object Manual : UpdatePolicy()

    /**
     * Widget updates when certain conditions are met.
     * @param description A description of the update conditions.
     */
    data class Conditional(val description: String) : UpdatePolicy()
}
