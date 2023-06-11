package me.kvalbrus.multibans.common.storage

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import lombok.Getter
import me.kvalbrus.multibans.common.managers.PluginManager
import java.io.File
import java.io.IOException
import java.util.*

class DataProviderSettings(private val pluginManager: PluginManager) {

    var type = DataProviderType.MY_SQL
    val properties: Properties

    init {
        properties = Properties()
    }

    @Synchronized
    fun load(): DataProviderSettings {
        val file = File(pluginManager.dataFolder, PATH)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (exception: IOException) {
                return this
            }
        }
        val toml = Toml().read(file)
        val tomlWriter = TomlWriter.Builder().build()
        val map: MutableMap<String, Any> = HashMap()
        val type = DataProviderType.getType(toml.getString("name"))
        if (type != null) {
            this.type = type
        }
        properties.setProperty(
            "dataSource.databaseName",
            if (toml.getString("databaseName") != null) toml.getString("databaseName") else "MultiPunishments"
        )
        properties.setProperty(
            "dataSource.serverName",
            if (toml.getString("address") != null) toml.getString("address") else "localhost"
        )
        val port =
            if (toml.getLong("port") != null && toml.getLong("port") > 0) toml.getLong("port") else 3306L
        properties.setProperty("dataSource.portNumber", port.toString())
        properties.setProperty(
            "dataSource.user",
            if (toml.getString("user") != null) toml.getString("user") else ""
        )
        properties.setProperty(
            "dataSource.password",
            if (toml.getString("password") != null) toml.getString("password") else ""
        )
        map["name"] = this.type.name
        map["databaseName"] = properties.getProperty("dataSource.databaseName")
        map["address"] = properties.getProperty("dataSource.serverName")
        map["port"] = port
        map["user"] = properties.getProperty("dataSource.user")
        map["password"] = properties.getProperty("dataSource.password")
        try {
            tomlWriter.write(map, file)
        } catch (exception: IOException) {
            return this
        }
        return this
    }

    companion object {
        private const val PATH = "data_provider.toml"
    }
}