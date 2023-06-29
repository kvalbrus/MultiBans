package me.kvalbrus.multibans.common.session

import me.kvalbrus.multibans.api.OnlinePlayer
import me.kvalbrus.multibans.api.Session
import java.util.*

class MultiSession : Session {

    private var _joinTime = 0L
    private var _quitTime = 0L

    override val playerUUID: UUID
    override val playerName: String
    override val playerIp: String

    override val joinTime: Long
        get() = this._joinTime

    override val quitTime: Long
        get() = this._quitTime

    constructor(player: OnlinePlayer) {
        this.playerUUID = player.uniqueId
        this.playerName = player.name
        this.playerIp = player.hostAddress
    }

    constructor(uuid: UUID, name: String, ip: String, join: Long, quit: Long) {
        this.playerUUID = uuid
        this.playerName = name
        this.playerIp = ip
        this._joinTime = join
        this._quitTime = quit
    }

    fun join() {
        this._joinTime = System.currentTimeMillis()
    }

    fun quit() {
        this._quitTime = System.currentTimeMillis()
    }
}