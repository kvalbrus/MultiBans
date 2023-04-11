package me.kvalbrus.multibans.common.implementations;

import me.kvalbrus.multibans.api.Console;
import org.jetbrains.annotations.NotNull;

public abstract class MultiConsole implements Console {

    public MultiConsole() {}

    @NotNull
    @Override
    public String getName() {
        return "console";
    }

}
