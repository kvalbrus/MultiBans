package me.kvalbrus.multibans.api

import me.kvalbrus.multibans.api.managers.PunishmentManager

interface MultiBansAPI {
    val punishmentManager: PunishmentManager
}