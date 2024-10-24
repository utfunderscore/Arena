package org.readutf.game.engine.features

import net.minestom.server.event.player.PlayerDisconnectEvent
import org.readutf.game.engine.stage.GenericStage

fun GenericStage.setRemoveOnLeave() {
    this.registerListener<PlayerDisconnectEvent> {
        game.removePlayer(it.player)
    }
}
