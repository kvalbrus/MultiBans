package me.kvalbrus.multibans.api

import me.kvalbrus.multibans.api.punishment.Punishment
import java.util.*

interface DataProvider {
    val name: String?

    @Throws(Exception::class) fun initialization()
    fun shutdown()
    @Throws(Exception::class) fun wipe()
    @Throws(Exception::class) fun createPunishment(punishment: Punishment): Boolean
    @Throws(Exception::class) fun createPlayerSession(session: Session)
    @Throws(Exception::class) fun savePunishment(punishment: Punishment): Boolean
    @Throws(Exception::class) fun deletePunishment(punishment: Punishment)
    @Throws(Exception::class) fun hasPunishment(id: String): Boolean
    @Throws(Exception::class) fun getPunishment(id: String): Punishment?
    @Throws(Exception::class) fun <T : Punishment?> getTargetHistory(uuid: UUID?): List<T>
    @Throws(Exception::class) fun <T : Punishment?> getTargetHistory(name: String?): List<T>
    @Throws(Exception::class) fun <T : Punishment?> getTargetHistoryByIp(ip: String): List<T>
    @Throws(Exception::class) fun <T : Punishment?> getCreatorHistory(creator: String?): List<T>
}