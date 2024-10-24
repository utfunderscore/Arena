package org.readutf.game.engine.event.annotation

import io.github.oshai.kotlinlogging.KotlinLogging
import net.minestom.server.event.Event
import org.readutf.game.engine.event.listener.GameListener
import org.readutf.game.engine.event.listener.RegisteredListener
import org.readutf.game.engine.types.Result
import org.readutf.game.engine.types.toSuccess
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf

private val logger = KotlinLogging.logger {}

/**
 * Scans the given object for methods annotated with `@EventListener` that have a single
 * parameter which is a subclass of `Event`. It returns a `Result` containing a list of
 * `GameListener` instances for each of these methods.
 *
 * @param toScan The object to scan for annotated methods.
 * @return A `Result` containing a list of `GameListener` instances.
 */
@Suppress("UNCHECKED_CAST")
fun scan(toScan: Any): Result<Map<KClass<out Event>, RegisteredListener>> {
    val listeners = mutableMapOf<KClass<out Event>, RegisteredListener>()
    val clazz = toScan::class

    for (function in clazz.functions) {
        val eventListener =
            function.findAnnotation<EventListener>()
                ?: //            logger.debug { "Function ${function.name} is not an event listener" }
                continue
        if (function.parameters.size != 2) {
            logger.debug { "Function ${function.name} has incorrect parameter count" }
            continue
        }

        val parameter = function.parameters[1]
        val classifier = parameter.type.classifier
        if (classifier !is KClass<*> || !classifier.isSubclassOf(Event::class)) {
            logger.debug { "Function ${function.name} has incorrect parameter type" }
            continue
        }

        logger.debug { "Found event listener: ${function.name}" }
        val gameListener = GameListener { event -> function.call(toScan, event) }
        val registeredListener =
            RegisteredListener(
                gameListener = gameListener,
                ignoreCancelled = eventListener.ignoreCancelled,
                ignoreSpectators = eventListener.ignoreSpectators,
                priority = eventListener.priority,
            )
        listeners[classifier as KClass<out Event>] = registeredListener
    }

    return listeners.toSuccess()
}
