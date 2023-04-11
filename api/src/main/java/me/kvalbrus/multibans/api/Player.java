package me.kvalbrus.multibans.api;

import java.util.UUID;

public interface Player extends Nameable {

    UUID getUniqueId();
}