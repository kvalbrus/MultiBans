package me.kvalbrus.multibans.common.storage;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum DataProviderType {

    MY_SQL("MySql");

    @Getter
    private final String name;

    DataProviderType(@NotNull final String name) {
        this.name = name;
    }

    @Nullable
    public static DataProviderType getType(final String name) {
        if (name == null) {
            return null;
        }

        for (DataProviderType type : DataProviderType.values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }

        return null;
    }
}