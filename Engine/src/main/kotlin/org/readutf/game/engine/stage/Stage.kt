package org.readutf.game.engine.stage

import net.minestom.server.event.Event
import org.readutf.game.engine.Game
import org.readutf.game.engine.arena.Arena
import org.readutf.game.engine.event.GameEventManager
import org.readutf.game.engine.event.annotation.scan
import org.readutf.game.engine.event.listener.RegisteredListener
import org.readutf.game.engine.event.listener.TypedGameListener
import org.readutf.game.engine.schedular.GameTask
import org.readutf.game.engine.team.GameTeam
import org.readutf.game.engine.types.Result
import kotlin.reflect.KClass

typealias GenericStage = Stage<*, *>

abstract class Stage<ARENA : Arena<*>, TEAM : GameTeam>(
    open val game: Game<ARENA, TEAM>,
    val previousStage: Stage<ARENA, TEAM>?,
) {
    val startTime = System.currentTimeMillis()
    val registeredListeners = LinkedHashMap<KClass<out Event>, MutableList<RegisteredListener>>()

    open fun onStart(): Result<Unit> = Result.empty()

    open fun onFinish(): Result<Unit> = Result.empty()

    fun registerRawListener(
        registeredListener: RegisteredListener,
        type: KClass<out Event>,
    ) {
        registeredListeners
            .getOrPut(type) { mutableListOf() }
            .add(registeredListener)

        GameEventManager.registerListener(game, type, registeredListener)
    }

    inline fun <reified T : Event> registerListener(
        priority: Int = 50,
        gameListener: TypedGameListener<T>,
    ) {
        registerRawListener(
            RegisteredListener(
                gameListener = gameListener,
                ignoreCancelled = false,
                ignoreSpectators = false,
                priority = priority,
            ),
            T::class,
        )
    }

    fun registerAll(any: Any) {
        val scan = scan(any).getOrThrow()

        scan.forEach { (type, listener) ->
            registerRawListener(listener, type)
        }
    }

    fun schedule(gameTask: GameTask): GameTask {
        game.scheduler.schedule(this, gameTask)
        return gameTask
    }

    fun endStage() = game.startNextStage()

    fun endStage(stageCreator: StageCreator<ARENA, TEAM>) = game.startNextStage(stageCreator)

    internal fun unregisterListeners() {
        registeredListeners.forEach { (type, listeners) ->
            listeners.forEach { listener ->
                GameEventManager.unregisterEvent(game, type, listener)
            }
        }
    }
}
