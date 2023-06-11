package me.kvalbrus.multibans.api.managers

import me.kvalbrus.multibans.api.punishment.Punishment
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget
import java.util.*
import kotlin.jvm.Throws

interface PunishmentManager {

    @Throws(Exception::class)
    fun <T : Punishment> getPlayerHistory(name: String) : List<T>

    @Throws(Exception::class)
    fun <T : Punishment> getPlayerHistory(uuid: UUID) : List<T>

    @Throws(Exception::class)
    fun <T : Punishment> getPlayerHistory(uuid: UUID, clazz: Class<T>): List<T>

    @Throws(Exception::class)
    fun <T : Punishment> getCreatorHistory(creatorName: String): List<T>

    @Throws(Exception::class)
    fun <T : Punishment> getActivePunishments(uuid: UUID): List<T>

    @Throws(Exception::class)
    fun <T : Punishment> getActivePunishments(uuid: UUID, clazz: Class<T>): List<T>

    @Throws(Exception::class)
    fun hasPunishment(id: String): Boolean

    @Throws(Exception::class)
    fun getPunishment(i: String): Punishment

    @Throws(Exception::class)
    fun hasActiveBan(uuid: UUID): Boolean

    @Throws(Exception::class)
    fun hasActiveBan(name: String): Boolean

    @Throws(Exception::class)
    fun hasActiveBanIp(uuid: UUID): Boolean

    @Throws(Exception::class)
    fun hasActiveBanIp(name: String): Boolean

    @Throws(Exception::class)
    fun hasActiveChatMute(uuid: UUID): Boolean

    @Throws(Exception::class)
    fun hasActiveChatMute(name: String): Boolean

    @Throws(Exception::class)
    fun generatePunishment(
        type: PunishmentType,
        target: PunishmentTarget,
        creator: PunishmentExecutor,
        duration: Long,
        reason: String
    ): Punishment

    @Throws(Exception::class)
    fun generatePunishment(
        type: PunishmentType,
        target: PunishmentTarget,
        creator: PunishmentExecutor,
        duration: Long,
        reason: String,
        comment: String?,
        servers: List<String?>
    ): Punishment
}