package me.kvalbrus.multibans.api.punishment

interface TemporaryPunishment : Punishment, Cancelable {
    
    var duration: Long
    var startedDate: Long
}