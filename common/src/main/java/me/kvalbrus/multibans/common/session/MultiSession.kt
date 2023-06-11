package me.kvalbrus.multibans.common.session

import me.kvalbrus.multibans.api.OnlinePlayer
import me.kvalbrus.multibans.api.Session
import java.util.*

class MultiSession(private val player: OnlinePlayer) : Session {

    private var _joinTime = 0L
    private var _quitTime = 0L

    override val playerUUID: UUID = this.player.uniqueId
    override val playerName: String = this.player.name
    override val playerIp: String = this.player.hostAddress

    override val joinTime: Long
        get() = this._joinTime

    override val quitTime: Long
        get() = this._quitTime

    fun join() {
        this._joinTime = System.currentTimeMillis()
    }

    fun quit() {
        this._quitTime = System.currentTimeMillis()
    }
}