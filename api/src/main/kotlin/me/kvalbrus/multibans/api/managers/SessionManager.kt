package me.kvalbrus.multibans.api.managers

import me.kvalbrus.multibans.api.Session
import java.util.UUID

interface SessionManager {

    fun getIPHistory(uuid: UUID): List<String>

    fun getIPHistory(name: String): List<String>

    fun getSessionHistory(uuid: UUID): List<Session>

    fun getSessionHistory(name: String): List<Session>
}