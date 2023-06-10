package me.kvalbrus.multibans.api.punishment.punishments

import me.kvalbrus.multibans.api.punishment.Punishment

enum class PunishmentType(val prefix: String, val clazz: Class<out Punishment?>) {
    TEMP_BAN_IP("TEMP_BAN_IP", TemporaryBanIp::class.java),
    BAN_IP("BAN_IP", PermanentlyBanIp::class.java),
    TEMP_BAN("TEMP_BAN", TemporaryBan::class.java),
    BAN("BAN", PermanentlyBan::class.java),
    TEMP_MUTE("TEMP_MUTE", TemporaryChatMute::class.java),
    MUTE("MUTE", PermanentlyChatMute::class.java),
    KICK("KICK", Kick::class.java)
}