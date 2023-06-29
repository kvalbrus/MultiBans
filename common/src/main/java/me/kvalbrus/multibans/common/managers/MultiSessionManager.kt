package me.kvalbrus.multibans.common.managers

import me.kvalbrus.multibans.api.Session
import me.kvalbrus.multibans.api.managers.SessionManager
import java.util.*

class MultiSessionManager(private val pluginManager: PluginManager) : SessionManager {

    override fun getIPHistory(uuid: UUID): List<String> {
        val history = this.pluginManager.dataProvider?.getSessionHistory(uuid) ?: listOf()
        val ipHistory = mutableListOf<String>()
        history.forEach { if (!ipHistory.contains(it.playerIp)) ipHistory.add(it.playerIp) }

        return ipHistory
    }

    override fun getIPHistory(name: String): List<String> {
        val history = this.pluginManager.dataProvider?.getSessionHistory(name) ?: listOf()
        val ipHistory = mutableListOf<String>()
        history.forEach { if (!ipHistory.contains(it.playerIp)) ipHistory.add(it.playerIp) }

        return ipHistory
    }

    override fun getSessionHistory(uuid: UUID): List<Session> {
        return this.pluginManager.dataProvider?.getSessionHistory(uuid) ?: listOf()
    }

    override fun getSessionHistory(name: String): List<Session> {
        return this.pluginManager.dataProvider?.getSessionHistory(name) ?: listOf()
    }
}