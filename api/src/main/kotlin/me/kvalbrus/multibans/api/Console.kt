package me.kvalbrus.multibans.api

interface Console : CommandSender {

    override fun hasPermission(permission: String): Boolean = true
}