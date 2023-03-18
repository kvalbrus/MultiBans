package me.kvalbrus.multibans.common.storage;

import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class StorageData {

    @Getter
    @NotNull
    private final String databaseName;

    @Getter
    @NotNull
    private final String address;

    @Getter
    private final int port;

    @Getter
    @NotNull
    private final String username;

    @Getter
    @NotNull
    private final String password;

    @Getter
    private final String properties;

    public StorageData(@NotNull final String databaseName,
                       @NotNull final String address,
                       final int port,
                       @NotNull final String username,
                       @NotNull final String password,
                       final String properties) {
        this.databaseName = databaseName;
        this.address = address;
        this.port = port;
        this.username = username;
        this.password = password;
        this.properties = properties;
    }

    public HikariConfig getHikariConfig() throws ClassNotFoundException {
        HikariConfig config = new HikariConfig();

        Class.forName("com.mysql.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://"
                                   + this.address + ":"
                                   + this.port + "/"
                                   + this.databaseName + "?"
                                   + this.properties);
        config.setUsername(this.username);
        config.setPassword(this.password);

        return config;
    }
}