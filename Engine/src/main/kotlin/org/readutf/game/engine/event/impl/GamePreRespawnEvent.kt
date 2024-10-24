package org.readutf.game.engine.event.impl

import net.minestom.server.entity.Player
import org.readutf.game.engine.GenericGame
import org.readutf.game.engine.event.GameEvent

class GamePreRespawnEvent(
    game: GenericGame,
    val player: Player,
) : GameEvent(game)
