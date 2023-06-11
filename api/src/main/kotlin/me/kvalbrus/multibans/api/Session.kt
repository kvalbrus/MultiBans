package me.kvalbrus.multibans.api

import java.util.UUID

interface Session {

    val playerUUID: UUID
    val playerName: String
    val playerIp: String
    val joinTime: Long
    val quitTime: Long
}