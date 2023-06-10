package me.kvalbrus.multibans.api.punishment.target

import me.kvalbrus.multibans.api.Player
import me.kvalbrus.multibans.api.punishment.Punishment

interface PunishmentTarget : Player {
    var punishment: Punishment?
}