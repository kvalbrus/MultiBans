package me.kvalbrus.multibans.api

interface OnlinePlayer : Player, CommandSender {
    val hostAddress: String
}