package me.kvalbrus.multibans.common.implementations

import me.kvalbrus.multibans.api.Console

abstract class MultiConsole : Console {

    override val name: String = "CONSOLE"

    override fun hasPermission(permission: String): Boolean = true
}