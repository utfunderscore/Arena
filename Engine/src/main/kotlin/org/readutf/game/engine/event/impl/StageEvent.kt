package org.readutf.game.engine.event.impl

import org.readutf.game.engine.GenericGame
import org.readutf.game.engine.event.GameEvent
import org.readutf.game.engine.stage.Stage

class StageEvent(
    val stage: Stage,
    game: GenericGame,
) : GameEvent(game)
