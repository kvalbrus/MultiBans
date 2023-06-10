package me.kvalbrus.multibans.api

interface MessageRecipient {
    fun sendMessage(message: String?)
    fun sendMessage(vararg messages: String?)
}
