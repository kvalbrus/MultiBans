package me.kvalbrus.multibans.api

import java.util.UUID

interface CommandSender : Permissible, MessageRecipient {

    val uniqueId: UUID?
    val name: String
}