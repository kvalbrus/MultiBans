package me.kvalbrus.multibans.common.storage;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum DataProviderType {

    MYSQL("MySQL"),
    MARIADB("MariaDB");

    public final String typeName;

    DataProviderType(@NotNull final String typeName) {
        this.typeName = typeName;
    }

    @Nullable
    public static DataProviderType getType(final String name) {
        if (name == null) {
            return null;
        }

        for (DataProviderType type : DataProviderType.values()) {
            if (type.typeName.equalsIgnoreCase(name)) {
                return type;
            }
        }

        return null;
    }
}