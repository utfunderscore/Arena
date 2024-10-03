package org.readutf.game.engine.event.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class EventListener(
    val ignoreCancelled: Boolean = false,
)