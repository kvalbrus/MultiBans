package me.kvalbrus.multibans.api

import java.util.*

interface Player : Nameable {
    val uniqueId: UUID
}