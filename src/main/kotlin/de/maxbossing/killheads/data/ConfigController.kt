package de.maxbossing.killheads.data

import de.maxbossing.killheads.KILLHEADS
import de.maxbossing.killheads.utils.NameResolver
import de.maxbossing.killheads.utils.debug
import de.maxbossing.killheads.utils.info
import de.maxbossing.killheads.utils.warn
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.w3c.dom.Text
import java.util.*
import kotlin.io.path.*

object ConfigController {

    private val json = Json { prettyPrint = true }

    private val CONFIG_PARENT = KILLHEADS.dataFolder.toPath()
    private val CONFIG_FILE = CONFIG_PARENT.resolve("config.json")
    private val TEXTURES_FILE = CONFIG_PARENT.resolve("textures.json")

    var CONFIG: Config
    var TEXTURES: TextureConfig

    private val CONFIG_PRESET = Config(prefix = "<gray>[</gray><red><bold>Kill</bold></red>Heads<green></green><gray>]</gray> ", debug = false, persistentUUID = UUID.randomUUID().toString())

    fun getEntityTextureConfig(entity: Entity): EntityTextureConfig? {
        if (entity.type == EntityType.PLAYER) return TEXTURES.playerTexture

        val resolvedName = NameResolver.resolve(entity) ?: return null

        return getEntityTextureConfig(resolvedName)
    }

    fun getEntityTextureConfig(string: String): EntityTextureConfig? {
        if (string == "player") return TEXTURES.playerTexture

        val textureConfig = TEXTURES.entityTextures[string]

        if (textureConfig == null) {
            warn("$string does not exist in textures.json!")
            return null
        }

        return textureConfig
    }

    init {
        if (!CONFIG_PARENT.exists())
            CONFIG_PARENT.createDirectories()

        if (!CONFIG_FILE.exists()) {
            warn("config.json does not exist, creating...")
            CONFIG_FILE.createFile()
            CONFIG_FILE.writeText(
                json.encodeToString(
                    serializer = Config.serializer(),
                    value = CONFIG_PRESET
                )
            )
            info("config.json created")
        }

        if (!TEXTURES_FILE.exists()) {
            warn("textures.json does not exist, creating...")
            TEXTURES_FILE.createFile()
            TEXTURES_FILE.writeText(
                this::class.java.getResourceAsStream("/textures.json")!!.reader().readText()
            )
            info("textures.json created")
        }

        CONFIG = Json.decodeFromString(CONFIG_FILE.readText())
        TEXTURES = Json.decodeFromString(TEXTURES_FILE.readText())
    }


    fun save() {
        CONFIG_FILE.writeText(
            json.encodeToString(
                serializer = Config.serializer(),
                value = CONFIG
            )
        )
        TEXTURES_FILE.writeText(
            json.encodeToString(
                serializer = TextureConfig.serializer(),
                value = TEXTURES
            )
        )
    }

    fun reload(hard: Boolean) {
        if (hard)
            TEXTURES_FILE.writeText(
                this::class.java.getResourceAsStream("/textures.json")!!.reader().readText()
            )
        TEXTURES = Json.decodeFromString(TEXTURES_FILE.readText())
    }

}