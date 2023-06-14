package me.kvalbrus.multibans.api

interface Permissible {

    fun hasPermission(permission: String): Boolean
}