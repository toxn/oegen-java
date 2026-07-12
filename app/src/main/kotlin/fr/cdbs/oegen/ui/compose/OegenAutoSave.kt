package fr.cdbs.oegen.ui.compose

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Debounced auto-save (~1.5s after the last change).
 */
class OegenAutoSave(
    private val debounceMs: Long = 1500L,
    private val save: () -> Boolean,
) {
    private val executor = Executors.newSingleThreadScheduledExecutor { runnable ->
        Thread(runnable, "oegen-autosave").apply { isDaemon = true }
    }

    @Volatile
    private var pending: ScheduledFuture<*>? = null

    fun schedule() {
        pending?.cancel(false)
        pending = executor.schedule(
            {
                save()
                pending = null
            },
            debounceMs,
            TimeUnit.MILLISECONDS,
        )
    }

    fun flush() {
        pending?.cancel(false)
        pending = null
        save()
    }

    fun cancelPending() {
        pending?.cancel(false)
        pending = null
    }

    fun shutdown() {
        cancelPending()
        executor.shutdownNow()
    }
}
