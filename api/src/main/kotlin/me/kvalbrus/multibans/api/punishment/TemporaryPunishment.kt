package me.kvalbrus.multibans.api.punishment

interface TemporaryPunishment : Punishment, Cancelable {
    
    val duration: Long
    val startedDate: Long
}