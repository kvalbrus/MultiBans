package me.kvalbrus.multibans.api

import me.kvalbrus.multibans.api.managers.PunishmentManager
import me.kvalbrus.multibans.api.managers.SessionManager

interface MultiBansAPI {

    val punishmentManager: PunishmentManager

    val sessionManager: SessionManager
}