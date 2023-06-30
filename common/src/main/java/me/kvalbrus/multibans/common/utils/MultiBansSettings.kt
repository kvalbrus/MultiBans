package me.kvalbrus.multibans.common.utils

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import lombok.Getter
import me.kvalbrus.multibans.common.managers.PluginManager
import java.io.File
import java.io.IOException
import java.util.TimeZone

class MultiBansSettings(private val pluginManager: PluginManager) {

    var prefix: String = "<white>[<gold>MultiBans<white>]</white>"

    var idSize: Long = 0

    var maskIp = false

    var chatMuteCommands: List<String>? = null

    var consoleLog = false

    var timeZone: TimeZone = TimeZone.getDefault()

    @Synchronized
    fun load(): MultiBansSettings {
        val file = File(pluginManager.dataFolder, PATH)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (exception: IOException) {
                // TODO: ???
                return this
            }
        }
        val toml = Toml().read(file)
        val tomlWriter = TomlWriter()
        val map: MutableMap<String, Any?> = HashMap()
        val idSize = toml.getLong("IdSize", 6L)
        if (idSize > 0 && idSize < 100) {
            this.idSize = 6L
        }

        this.prefix = toml.getString("Prefix", this.prefix)
        maskIp = toml.getBoolean("MaskIp", true)
        chatMuteCommands = toml.getList("ChatMuteCommands", ArrayList())
        consoleLog = toml.getBoolean("ConsoleLog", true)
        timeZone = TimeZone.getTimeZone(toml.getString("TimeZone", timeZone.id))

        map["Prefix"] = this.prefix
        map["IdSize"] = this.idSize
        map["MaskIp"] = maskIp
        map["ChatMuteCommands"] = chatMuteCommands
        map["ConsoleLog"] = consoleLog
        map["TimeZone"] = timeZone.id

        try {
            tomlWriter.write(map, file)
        } catch (exception: IOException) {
            return this
        }
        return this
    }

    companion object {
        private const val PATH = "settings.toml"
    }
}