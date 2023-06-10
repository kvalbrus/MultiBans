package me.kvalbrus.multibans.api

import me.kvalbrus.multibans.api.managers.PunishmentManager

interface MultiBans {
    val punishmentManager: PunishmentManager
}